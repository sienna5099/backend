package com.aicareermentor.backend.dto;

import java.util.List;

public record RoadmapLevelResponse(
        int levelNumber,
        String title,
        String description,
        String status,
        int progress,
        int completedTasks,
        int totalTasks,
        List<RoadmapTaskResponse> tasks) {
}
