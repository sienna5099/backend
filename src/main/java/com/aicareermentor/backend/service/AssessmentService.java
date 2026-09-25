package com.aicareermentor.backend.service;

import com.aicareermentor.backend.dto.AssessmentRequest;
import com.aicareermentor.backend.dto.AssessmentResponse;
import com.aicareermentor.backend.entity.Assessment;
import com.aicareermentor.backend.entity.AssessmentAnswer;
import com.aicareermentor.backend.exception.ResourceNotFoundException;
import com.aicareermentor.backend.repository.AssessmentAnswerRepository;
import com.aicareermentor.backend.repository.AssessmentRepository;
import com.aicareermentor.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentAnswerRepository assessmentAnswerRepository;
        private final UserRepository userRepository;

    public AssessmentService(
            AssessmentRepository assessmentRepository,
                        AssessmentAnswerRepository assessmentAnswerRepository,
                        UserRepository userRepository) {

        this.assessmentRepository = assessmentRepository;
        this.assessmentAnswerRepository = assessmentAnswerRepository;
                this.userRepository = userRepository;
    }

    @Transactional
    public AssessmentResponse submitAssessment(AssessmentRequest request) {

        // -----------------------------
        // 1. Validate User ID
        // -----------------------------

        if (request.getUserId() == null || request.getUserId() <= 0) {
            throw new IllegalArgumentException("Valid user ID is required");
        }

        if (!userRepository.existsById(request.getUserId())) {
            throw new ResourceNotFoundException("User not found: " + request.getUserId());
        }

        // -----------------------------
        // 2. Validate answers
        // -----------------------------

        if (request.getAnswers() == null || request.getAnswers().isEmpty()) {
            throw new IllegalArgumentException("Assessment answers are required");
        }

        // We have exactly 30 questions
        if (request.getAnswers().size() != 30) {
            throw new IllegalArgumentException(
                    "Assessment must contain answers for all 30 questions"
            );
        }

        // -----------------------------
        // 3. Define traits
        // -----------------------------

        String[] traits = {
                "analytical",
                "technical",
                "communication",
                "leadership",
                "creative"
        };

        // -----------------------------
        // 4. Create score maps
        // -----------------------------

        Map<String, Double> totals = new LinkedHashMap<>();
        Map<String, Integer> counts = new LinkedHashMap<>();

        for (String trait : traits) {
            totals.put(trait, 0.0);
            counts.put(trait, 0);
        }

        // -----------------------------
        // 5. Process all 30 answers
        // -----------------------------

        for (int questionId = 1; questionId <= 30; questionId++) {

            Integer answer = request.getAnswers().get(questionId);

            if (answer == null) {
                throw new IllegalArgumentException(
                        "Missing answer for question " + questionId
                );
            }

            // Valid values:
            // 2 = Strongly Agree
            // 1 = Agree
            // 0 = Neutral
            // -1 = Disagree

            if (answer < -1 || answer > 2) {
                throw new IllegalArgumentException(
                        "Invalid answer value for question " + questionId
                );
            }

            // Same mapping as frontend:
            // question 1 -> analytical
            // question 2 -> technical
            // question 3 -> communication
            // question 4 -> leadership
            // question 5 -> creative
            // question 6 -> analytical
            // etc.

            String trait = traits[(questionId - 1) % traits.length];

            totals.put(
                    trait,
                    totals.get(trait) + answer
            );

            counts.put(
                    trait,
                    counts.get(trait) + 1
            );
        }

        // -----------------------------
        // 6. Calculate average scores
        // -----------------------------

        Map<String, Double> scores = new LinkedHashMap<>();

        for (String trait : traits) {

            double score = totals.get(trait) / counts.get(trait);

            scores.put(trait, score);
        }

        // -----------------------------
        // 7. Find highest trait
        // -----------------------------

        String topTrait = traits[0];
        double highestScore = scores.get(topTrait);

        for (String trait : traits) {

            double currentScore = scores.get(trait);

            if (currentScore > highestScore) {
                highestScore = currentScore;
                topTrait = trait;
            }
        }

        // -----------------------------
        // 8. Generate result information
        // -----------------------------

        String topInterest;
        String strengths;
        String workPreference;

        switch (topTrait) {

            case "analytical":
                topInterest = "Analyzing data, solving problems";
                strengths = "Analytical thinking, problem solving";
                workPreference = "Data-driven analytical work";
                break;

            case "technical":
                topInterest = "Building and understanding technology";
                strengths = "Technical aptitude, logical reasoning";
                workPreference = "Hands-on technical work";
                break;

            case "communication":
                topInterest = "Explaining ideas and connecting with people";
                strengths = "Communication, storytelling";
                workPreference = "People-facing, collaborative work";
                break;

            case "leadership":
                topInterest = "Organizing teams and driving outcomes";
                strengths = "Leadership, decision-making";
                workPreference = "Ownership-driven, cross-functional work";
                break;

            case "creative":
                topInterest = "Designing and imagining new possibilities";
                strengths = "Creativity, original thinking";
                workPreference = "Open-ended, design-driven work";
                break;

            default:
                topInterest = "Analyzing data, solving problems";
                strengths = "Analytical thinking, problem solving";
                workPreference = "Data-driven analytical work";
        }

        // -----------------------------
        // 9. Check if user already has
        //    an assessment
        // -----------------------------

        Assessment assessment = assessmentRepository
                .findByUserId(request.getUserId())
                .orElse(new Assessment());

        // -----------------------------
        // 10. Save assessment
        // -----------------------------

        assessment.setUserId(request.getUserId());

        assessment.setAnalyticalScore(
                scores.get("analytical")
        );

        assessment.setTechnicalScore(
                scores.get("technical")
        );

        assessment.setCommunicationScore(
                scores.get("communication")
        );

        assessment.setLeadershipScore(
                scores.get("leadership")
        );

        assessment.setCreativeScore(
                scores.get("creative")
        );

        assessment.setTopTrait(topTrait);

        Assessment savedAssessment =
                assessmentRepository.save(assessment);

        // -----------------------------
        // 11. Remove old answers
        //    if assessment is retaken
        // -----------------------------

        assessmentAnswerRepository.deleteByAssessmentId(
                savedAssessment.getId()
        );

        // -----------------------------
        // 12. Save all 30 answers
        // -----------------------------

        for (int questionId = 1; questionId <= 30; questionId++) {

            Integer answerValue =
                    request.getAnswers().get(questionId);

            String trait =
                    traits[(questionId - 1) % traits.length];

            AssessmentAnswer assessmentAnswer =
                    new AssessmentAnswer();

            assessmentAnswer.setAssessmentId(
                    savedAssessment.getId()
            );

            assessmentAnswer.setQuestionId(
                    String.valueOf(questionId)
            );

            assessmentAnswer.setAnswer(
                    String.valueOf(answerValue)
            );

            assessmentAnswer.setTrait(trait);

            assessmentAnswer.setScore(answerValue);

            assessmentAnswerRepository.save(
                    assessmentAnswer
            );
        }

        // -----------------------------
        // 13. Create response score list
        // -----------------------------

        List<AssessmentResponse.Score> scoreList =
                new ArrayList<>();

        for (String trait : traits) {

            scoreList.add(
                    new AssessmentResponse.Score(
                            trait,
                            scores.get(trait)
                    )
            );
        }

        // -----------------------------
        // 14. Return response
        // -----------------------------

        return new AssessmentResponse(
                savedAssessment.getId(),
                savedAssessment.getUserId(),
                topTrait,
                topInterest,
                strengths,
                workPreference,
                scoreList
        );
    }

    public void updateTopTrait(Long assessmentId, String topTrait) {

        if (topTrait == null || topTrait.isBlank()) {
            throw new IllegalArgumentException(
                    "Top trait cannot be null or empty"
            );
        }

        Assessment assessment = assessmentRepository
                .findById(assessmentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Assessment not found: " + assessmentId
                        )
                );

        assessment.setTopTrait(topTrait);

        assessmentRepository.save(assessment);
    }
}