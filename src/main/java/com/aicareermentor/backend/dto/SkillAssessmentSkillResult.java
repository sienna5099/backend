package com.aicareermentor.backend.dto;

public record SkillAssessmentSkillResult(
        Long skillId,
        String skillName,
        Double score,
        String level,
        String status) {
}
