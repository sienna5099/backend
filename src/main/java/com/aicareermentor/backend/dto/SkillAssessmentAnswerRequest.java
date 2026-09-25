package com.aicareermentor.backend.dto;

import jakarta.validation.constraints.NotNull;

public record SkillAssessmentAnswerRequest(
        @NotNull(message = "Question ID is required") Long questionId,
        @NotNull(message = "Skill answer value is required") Integer value) {
}
