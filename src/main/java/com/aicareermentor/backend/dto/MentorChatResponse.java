package com.aicareermentor.backend.dto;

public record MentorChatResponse(
        Long messageId,
        String role,
        String content) {
}
