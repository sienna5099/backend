package com.aicareermentor.backend.dto;

public record SkillAssessmentSubmitResponse(
        boolean success,
        Long userId,
        String careerId,
        int savedAnswers,
        String message) {
}
