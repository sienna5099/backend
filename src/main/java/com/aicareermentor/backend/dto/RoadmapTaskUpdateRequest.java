package com.aicareermentor.backend.dto;

import jakarta.validation.constraints.NotNull;

public record RoadmapTaskUpdateRequest(
        @NotNull Boolean completed) {
}
