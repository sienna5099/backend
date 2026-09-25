package com.aicareermentor.backend.dto;

import java.util.List;

public class AssessmentResponse {

    private Long assessmentId;
    private Long userId;

    private String topTrait;
    private String topInterest;
    private String strengths;
    private String workPreference;

    private List<Score> scores;

    public AssessmentResponse() {
    }

    public AssessmentResponse(
            Long assessmentId,
            Long userId,
            String topTrait,
            String topInterest,
            String strengths,
            String workPreference,
            List<Score> scores
    ) {
        this.assessmentId = assessmentId;
        this.userId = userId;
        this.topTrait = topTrait;
        this.topInterest = topInterest;
        this.strengths = strengths;
        this.workPreference = workPreference;
        this.scores = scores;
    }

    public Long getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Long assessmentId) {
        this.assessmentId = assessmentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTopTrait() {
        return topTrait;
    }

    public void setTopTrait(String topTrait) {
        this.topTrait = topTrait;
    }

    public String getTopInterest() {
        return topInterest;
    }

    public void setTopInterest(String topInterest) {
        this.topInterest = topInterest;
    }

    public String getStrengths() {
        return strengths;
    }

    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }

    public String getWorkPreference() {
        return workPreference;
    }

    public void setWorkPreference(String workPreference) {
        this.workPreference = workPreference;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    public static class Score {

        private String trait;
        private Double score;

        public Score() {
        }

        public Score(String trait, Double score) {
            this.trait = trait;
            this.score = score;
        }

        public String getTrait() {
            return trait;
        }

        public void setTrait(String trait) {
            this.trait = trait;
        }

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }
    }
}
