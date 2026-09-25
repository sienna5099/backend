package com.aicareermentor.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SkillAssessmentSubmitRequest(
        @NotNull(message = "User ID is required") Long userId,
        @NotBlank(message = "Career ID is required") String careerId,
        @NotEmpty(message = "Skill assessment answers are required")
        @Valid List<SkillAssessmentAnswerRequest> answers) {
}
