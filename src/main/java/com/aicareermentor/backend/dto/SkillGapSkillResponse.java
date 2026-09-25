package com.aicareermentor.backend.dto;

public record SkillGapSkillResponse(
        Long skillId,
        String skillName,
        boolean required,
        Double userScore,
        String userLevel,
        String status) {
}
