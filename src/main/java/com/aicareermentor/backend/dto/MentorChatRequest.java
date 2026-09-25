package com.aicareermentor.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MentorChatRequest(
        @NotNull Long userId,
        @NotNull Long mentorId,
        @NotBlank String message) {
}
