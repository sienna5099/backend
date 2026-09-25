package com.aicareermentor.backend.service;

import com.aicareermentor.backend.dto.SkillAssessmentAnswerRequest;
import com.aicareermentor.backend.dto.SkillAssessmentOptionResponse;
import com.aicareermentor.backend.dto.SkillAssessmentQuestionResponse;
import com.aicareermentor.backend.dto.SkillAssessmentResultResponse;
import com.aicareermentor.backend.dto.SkillAssessmentSkillResult;
import com.aicareermentor.backend.dto.SkillAssessmentSubmitRequest;
import com.aicareermentor.backend.dto.SkillAssessmentSubmitResponse;
import com.aicareermentor.backend.dto.SkillGapAnalysisResponse;
import com.aicareermentor.backend.dto.SkillGapAnalyzeRequest;
import com.aicareermentor.backend.dto.SkillGapResponse;
import com.aicareermentor.backend.dto.SkillGapSkillResponse;
import com.aicareermentor.backend.entity.Career;
import com.aicareermentor.backend.entity.CareerSkill;
import com.aicareermentor.backend.entity.SkillAssessmentAnswer;
import com.aicareermentor.backend.entity.SkillAssessmentQuestion;
import com.aicareermentor.backend.entity.User;
import com.aicareermentor.backend.exception.ResourceNotFoundException;
import com.aicareermentor.backend.repository.CareerRepository;
import com.aicareermentor.backend.repository.CareerSkillRepository;
import com.aicareermentor.backend.repository.SkillAssessmentAnswerRepository;
import com.aicareermentor.backend.repository.SkillAssessmentQuestionRepository;
import com.aicareermentor.backend.repository.UserRepository;
import com.aicareermentor.backend.skillassessment.SkillProficiencyLevel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SkillAssessmentService {

    private static final List<SkillAssessmentOptionResponse> OPTIONS = List.of(
            new SkillAssessmentOptionResponse(1, "Never / No experience"),
            new SkillAssessmentOptionResponse(2, "Beginner"),
            new SkillAssessmentOptionResponse(3, "Basic / Some experience"),
            new SkillAssessmentOptionResponse(4, "Comfortable"),
            new SkillAssessmentOptionResponse(5, "Advanced"));

    private final CareerRepository careerRepository;
    private final CareerSkillRepository careerSkillRepository;
    private final UserRepository userRepository;
    private final SkillAssessmentQuestionRepository questionRepository;
    private final SkillAssessmentAnswerRepository answerRepository;
    private final GroqSkillGapService groqSkillGapService;

    public SkillAssessmentService(
            CareerRepository careerRepository,
            CareerSkillRepository careerSkillRepository,
            UserRepository userRepository,
            SkillAssessmentQuestionRepository questionRepository,
            SkillAssessmentAnswerRepository answerRepository,
            GroqSkillGapService groqSkillGapService) {

        this.careerRepository = careerRepository;
        this.careerSkillRepository = careerSkillRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.groqSkillGapService = groqSkillGapService;
    }

    @Transactional(readOnly = true)
    public List<SkillAssessmentQuestionResponse> getQuestions(String careerId) {

        Career career = getCareer(careerId);

        return questionRepository
                .findByCareer_CareerIdAndActiveTrueOrderBySequenceNumberAsc(
                        career.getCareerId())
                .stream()
                .map(this::toQuestionResponse)
                .toList();
    }

    @Transactional
    public SkillAssessmentSubmitResponse submit(
            SkillAssessmentSubmitRequest request) {

        Long userId = requirePositiveUserId(request.userId());

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " + userId));

        Career career = getCareer(request.careerId());

        List<SkillAssessmentQuestion> requiredQuestions =
                activeQuestions(career.getCareerId());

        if (requiredQuestions.isEmpty()) {
            throw new IllegalArgumentException(
                    "No active skill assessment questions exist for career: "
                            + career.getCareerId());
        }

        if (request.answers().size() != requiredQuestions.size()) {
            throw new IllegalArgumentException(
                    "Skill assessment must contain answers for all "
                            + requiredQuestions.size()
                            + " questions");
        }

        Map<Long, SkillAssessmentQuestion> questionsById =
                new HashMap<>();

        for (SkillAssessmentQuestion question : requiredQuestions) {
            questionsById.put(
                    question.getId(),
                    question
            );
        }

        Set<Long> submittedQuestionIds =
                new HashSet<>();

        for (SkillAssessmentAnswerRequest answer :
                request.answers()) {

            if (!submittedQuestionIds.add(
                    answer.questionId())) {

                throw new IllegalArgumentException(
                        "Duplicate skill assessment question: "
                                + answer.questionId());
            }

            if (answer.value() == null
                    || answer.value() < 1
                    || answer.value() > 5) {

                throw new IllegalArgumentException(
                        "Skill answer value must be between 1 and 5");
            }

            SkillAssessmentQuestion question =
                    questionsById.get(
                            answer.questionId());

            if (question == null
                    || !Boolean.TRUE.equals(
                    question.getActive())) {

                throw new IllegalArgumentException(
                        "Question does not belong to the selected career: "
                                + answer.questionId());
            }
        }

        if (submittedQuestionIds.size()
                != questionsById.size()) {

            throw new IllegalArgumentException(
                    "All active skill assessment questions must be answered");
        }

        for (SkillAssessmentAnswerRequest answer :
                request.answers()) {

            SkillAssessmentQuestion question =
                    questionsById.get(
                            answer.questionId());

            SkillAssessmentAnswer storedAnswer =
                    answerRepository
                            .findByUser_IdAndCareer_CareerIdAndQuestion_Id(
                                    userId,
                                    career.getCareerId(),
                                    question.getId())
                            .orElseGet(() ->
                                    new SkillAssessmentAnswer(
                                            user,
                                            career,
                                            question,
                                            answer.value()));

            storedAnswer.setValue(
                    answer.value());

            answerRepository.save(
                    storedAnswer);
        }

        return new SkillAssessmentSubmitResponse(
                true,
                userId,
                career.getCareerId(),
                request.answers().size(),
                "Skill assessment submitted successfully");
    }

    @Transactional(readOnly = true)
    public SkillAssessmentResultResponse getResult(
            Long userId,
            String careerId) {

        User user = getUser(userId);

        Career career = getCareer(careerId);

        List<SkillAssessmentAnswer> answers =
                answerRepository
                        .findByUser_IdAndCareer_CareerId(
                                user.getId(),
                                career.getCareerId());

        if (answers.isEmpty()) {
            return new SkillAssessmentResultResponse(
                    false,
                    career.getCareerId(),
                    List.of());
        }

        Map<Long, List<Integer>> scoresBySkill =
                groupScoresBySkill(answers);

        List<SkillAssessmentSkillResult> results =
                new ArrayList<>();

        for (CareerSkill careerSkill :
                careerSkillRepository
                        .findByCareer_CareerIdOrderByPriorityAsc(
                                career.getCareerId())) {

            List<Integer> scores =
                    scoresBySkill.get(
                            careerSkill.getSkill().getId());

            if (scores == null || scores.isEmpty()) {
                continue;
            }

            double average =
                    scores.stream()
                            .mapToInt(Integer::intValue)
                            .average()
                            .orElseThrow();

            int roundedScore =
                    (int) Math.round(average);

            SkillProficiencyLevel level =
                    SkillProficiencyLevel.fromScore(
                            roundedScore);

            results.add(
                    new SkillAssessmentSkillResult(
                            careerSkill.getSkill().getId(),
                            careerSkill.getSkill().getName(),
                            average,
                            level.label(),
                            level.status()));
        }

        return new SkillAssessmentResultResponse(
                true,
                career.getCareerId(),
                results);
    }

    @Transactional(readOnly = true)
    public SkillGapResponse getSkillGap(
            Long userId,
            String careerId) {

        User user = getUser(userId);

        Career career = getCareer(careerId);

        List<SkillAssessmentAnswer> answers =
                answerRepository
                        .findByUser_IdAndCareer_CareerId(
                                user.getId(),
                                career.getCareerId());

        Map<Long, List<Integer>> scoresBySkill =
                groupScoresBySkill(answers);

        boolean completed = !answers.isEmpty();

        List<SkillGapSkillResponse> skills =
                new ArrayList<>();

        for (CareerSkill careerSkill :
                careerSkillRepository
                        .findByCareer_CareerIdOrderByPriorityAsc(
                                career.getCareerId())) {

            List<Integer> scores =
                    scoresBySkill.get(
                            careerSkill.getSkill().getId());

            if (scores == null || scores.isEmpty()) {

                skills.add(
                        new SkillGapSkillResponse(
                                careerSkill.getSkill().getId(),
                                careerSkill.getSkill().getName(),
                                true,
                                null,
                                null,
                                "NOT_ASSESSED"));

                continue;
            }

            double average =
                    scores.stream()
                            .mapToInt(Integer::intValue)
                            .average()
                            .orElseThrow();

            int roundedScore =
                    (int) Math.round(average);

            SkillProficiencyLevel level =
                    SkillProficiencyLevel.fromScore(
                            roundedScore);

            skills.add(
                    new SkillGapSkillResponse(
                            careerSkill.getSkill().getId(),
                            careerSkill.getSkill().getName(),
                            true,
                            average,
                            level.label(),
                            level.status()));
        }

        return new SkillGapResponse(
                completed,
                career.getCareerId(),
                skills);
    }

    /*
     * ============================================================
     * GROQ AI SKILL GAP ANALYSIS
     * ============================================================
     */

    public SkillGapAnalysisResponse analyzeSkillGap(
            SkillGapAnalyzeRequest request) {

        Long userId =
                requirePositiveUserId(
                        request.userId());

        // Make sure the user exists.
        getUser(userId);

        // Make sure the selected career exists.
        Career career =
                getCareer(request.careerId());

        List<CareerSkill> careerSkills =
                careerSkillRepository
                        .findByCareer_CareerIdOrderByPriorityAsc(
                                career.getCareerId());

        if (careerSkills.isEmpty()) {
            throw new IllegalArgumentException(
                    "No required skills are configured for career: "
                            + career.getCareerId());
        }

        List<String> requiredSkills =
                careerSkills
                        .stream()
                        .map(careerSkill ->
                                careerSkill
                                        .getSkill()
                                        .getName())
                        .toList();

        return groqSkillGapService.analyze(
                career.getTitle(),
                requiredSkills,
                request);
    }

    private List<SkillAssessmentQuestion> activeQuestions(
            String careerId) {

        return questionRepository
                .findByCareer_CareerIdAndActiveTrueOrderBySequenceNumberAsc(
                        careerId);
    }

    private Map<Long, List<Integer>> groupScoresBySkill(
            List<SkillAssessmentAnswer> answers) {

        Map<Long, List<Integer>> scoresBySkill =
                new LinkedHashMap<>();

        for (SkillAssessmentAnswer answer :
                answers) {

            scoresBySkill
                    .computeIfAbsent(
                            answer.getQuestion()
                                    .getSkill()
                                    .getId(),
                            ignored ->
                                    new ArrayList<>())
                    .add(
                            answer.getValue());
        }

        return scoresBySkill;
    }

    private SkillAssessmentQuestionResponse toQuestionResponse(
            SkillAssessmentQuestion question) {

        return new SkillAssessmentQuestionResponse(
                question.getId(),
                question.getSkill().getId(),
                question.getSkill().getName(),
                question.getQuestion(),
                OPTIONS);
    }

    private Career getCareer(String careerId) {

        if (careerId == null
                || careerId.isBlank()) {

            throw new IllegalArgumentException(
                    "Career ID is required");
        }

        return careerRepository
                .findByCareerId(
                        careerId.trim())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Career not found: "
                                        + careerId));
    }

    private User getUser(Long userId) {

        Long validUserId =
                requirePositiveUserId(userId);

        return userRepository
                .findById(validUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: "
                                        + validUserId));
    }

    private Long requirePositiveUserId(
            Long userId) {

        if (userId == null
                || userId <= 0) {

            throw new IllegalArgumentException(
                    "Valid user ID is required");
        }

        return userId;
    }
}