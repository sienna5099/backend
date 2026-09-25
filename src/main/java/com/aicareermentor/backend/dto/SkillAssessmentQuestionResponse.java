package com.aicareermentor.backend.dto;

import java.util.List;

public record SkillAssessmentQuestionResponse(
        Long id,
        Long skillId,
        String skillName,
        String question,
        List<SkillAssessmentOptionResponse> options) {
}
