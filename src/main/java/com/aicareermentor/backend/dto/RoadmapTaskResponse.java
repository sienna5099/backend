package com.aicareermentor.backend.dto;

public record RoadmapTaskResponse(
        Long id,
        String title,
        String description,
        boolean completed) {
}
