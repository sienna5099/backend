package com.aicareermentor.backend.service;

import com.aicareermentor.backend.dto.RecommendationResponse;
import com.aicareermentor.backend.entity.Assessment;
import com.aicareermentor.backend.entity.Career;
import com.aicareermentor.backend.exception.ResourceNotFoundException;
import com.aicareermentor.backend.repository.AssessmentRepository;
import com.aicareermentor.backend.repository.CareerRepository;
import com.aicareermentor.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecommendationService {

    private static final List<String> TRAITS = List.of(
            "analytical",
            "technical",
            "communication",
            "leadership",
            "creative"
    );

    private static final Map<String, Map<String, Double>> CAREER_WEIGHTS = createCareerWeights();

    private final UserRepository userRepository;
    private final AssessmentRepository assessmentRepository;
    private final CareerRepository careerRepository;

    public RecommendationService(
            UserRepository userRepository,
            AssessmentRepository assessmentRepository,
            CareerRepository careerRepository) {
        this.userRepository = userRepository;
        this.assessmentRepository = assessmentRepository;
        this.careerRepository = careerRepository;
    }

    public List<RecommendationResponse> getRecommendations(Long userId) {
        if (userId == null || !userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found: " + userId);
        }

        Assessment assessment = assessmentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Completed assessment not found for user: " + userId));

        Map<String, Double> studentScores = getAssessmentScores(assessment);
        List<RecommendationResponse> recommendations = new ArrayList<>();

        for (Career career : careerRepository.findAll()) {
            recommendations.add(buildRecommendation(career, studentScores));
        }

        recommendations.sort(Comparator
                .comparingInt(RecommendationResponse::getMatchPercentage)
                .reversed());

        return recommendations;
    }

    private RecommendationResponse buildRecommendation(
            Career career,
            Map<String, Double> studentScores) {

        Map<String, Double> weights = CAREER_WEIGHTS.get(career.getCareerId());
        if (weights == null) {
            throw new IllegalArgumentException(
                    "Recommendation weights are missing for career: " + career.getCareerId());
        }

        Map<String, Double> careerScores = getCareerScores(career);
        double weightedSimilarity = 0;

        for (String trait : TRAITS) {
            double difference = Math.abs(studentScores.get(trait) - careerScores.get(trait));
            double similarity = 100 - difference;
            weightedSimilarity += (similarity / 100) * weights.get(trait);
        }

        int matchPercentage = (int) Math.round(clamp(weightedSimilarity, 0, 100));

        return new RecommendationResponse(
                career.getCareerId(),
                career.getTitle(),
                matchPercentage,
                findStrongestTraits(studentScores, careerScores, weights),
                findGrowthAreas(studentScores, careerScores));
    }

    private Map<String, Double> getAssessmentScores(Assessment assessment) {
        Map<String, Double> scores = new LinkedHashMap<>();
        scores.put("analytical", assessment.getAnalyticalScore());
        scores.put("technical", assessment.getTechnicalScore());
        scores.put("communication", assessment.getCommunicationScore());
        scores.put("leadership", assessment.getLeadershipScore());
        scores.put("creative", assessment.getCreativeScore());

        for (Map.Entry<String, Double> entry : scores.entrySet()) {
            Double score = entry.getValue();
            if (score == null || score.isNaN() || score.isInfinite() || score < -1 || score > 2) {
                throw new IllegalArgumentException(
                        "Invalid assessment score for trait: " + entry.getKey());
            }
            entry.setValue(normalizeAssessmentScore(score));
        }

        return scores;
    }

    private Map<String, Double> getCareerScores(Career career) {
        Map<String, Double> scores = new HashMap<>();
        scores.put("analytical", toValidCareerScore("analytical", career.getAnalyticalScore()));
        scores.put("technical", toValidCareerScore("technical", career.getTechnicalScore()));
        scores.put("communication", toValidCareerScore("communication", career.getCommunicationScore()));
        scores.put("leadership", toValidCareerScore("leadership", career.getLeadershipScore()));
        scores.put("creative", toValidCareerScore("creative", career.getCreativeScore()));
        return scores;
    }

    private double toValidCareerScore(String trait, Integer score) {
        if (score == null || score < 0 || score > 100) {
            throw new IllegalArgumentException("Invalid career score for trait: " + trait);
        }
        return score;
    }

    private List<String> findStrongestTraits(
            Map<String, Double> studentScores,
            Map<String, Double> careerScores,
            Map<String, Double> weights) {
        return TRAITS.stream()
                .sorted(Comparator.comparingDouble(
                trait -> {
                    double similarity = 100 - Math.abs(
                        studentScores.get(trait) - careerScores.get(trait));
                    return studentScores.get(trait) * weights.get(trait) * similarity;
                }).reversed())
                .limit(3)
                .toList();
    }

    private List<String> findGrowthAreas(
            Map<String, Double> studentScores,
            Map<String, Double> careerScores) {
        return TRAITS.stream()
                .filter(trait -> careerScores.get(trait) - studentScores.get(trait) >= 10)
                .sorted(Comparator.comparingDouble(
                        trait -> careerScores.get(trait) - studentScores.get(trait)).reversed())
                .limit(2)
                .toList();
    }

    private double normalizeAssessmentScore(double score) {
        return ((score + 1) / 3) * 100;
    }

    private double clamp(double value, double minimum, double maximum) {
        return Math.max(minimum, Math.min(maximum, value));
    }

    private static Map<String, Map<String, Double>> createCareerWeights() {
        Map<String, Map<String, Double>> weights = new HashMap<>();
        weights.put("data-analyst", weights(40, 25, 15, 5, 15));
        weights.put("software-developer", weights(20, 40, 10, 5, 25));
        weights.put("project-manager", weights(15, 5, 30, 35, 15));
        weights.put("product-manager", weights(20, 10, 25, 25, 20));
        weights.put("hr-specialist", weights(10, 5, 35, 30, 20));
        weights.put("cybersecurity-analyst", weights(30, 40, 10, 5, 15));
        return weights;
    }

    private static Map<String, Double> weights(
            double analytical,
            double technical,
            double communication,
            double leadership,
            double creative) {
        Map<String, Double> weights = new LinkedHashMap<>();
        weights.put("analytical", analytical);
        weights.put("technical", technical);
        weights.put("communication", communication);
        weights.put("leadership", leadership);
        weights.put("creative", creative);
        return weights;
    }
}
