package com.aicareermentor.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public class AssessmentRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotEmpty(message = "Assessment answers are required")
    @Valid
    private Map<Integer, Integer> answers;

    public AssessmentRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Map<Integer, Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Integer, Integer> answers) {
        this.answers = answers;
    }
}
