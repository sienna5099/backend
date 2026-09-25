package com.aicareermentor.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record SkillGapAnalyzeRequest(

        @NotNull(message = "User ID is required")
        @Positive(message = "User ID must be positive")
        Long userId,

        @NotBlank(message = "Career ID is required")
        String careerId,

        @NotNull(message = "Skills are required")
        @Valid
        List<SkillInput> skills,

        String resumeContext

) {

    public record SkillInput(

            @NotBlank(message = "Skill name is required")
            String name,

            @NotNull(message = "Skill value is required")
            @Min(value = 0, message = "Skill value cannot be less than 0")
            @Max(value = 100, message = "Skill value cannot be greater than 100")
            Integer value

    ) {
    }
}