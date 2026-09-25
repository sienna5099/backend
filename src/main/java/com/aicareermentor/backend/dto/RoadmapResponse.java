package com.aicareermentor.backend.dto;

import java.util.List;

public record RoadmapResponse(
        String careerId,
        String careerTitle,
        int overallProgress,
        int currentLevel,
        List<RoadmapLevelResponse> levels,
        List<RoadmapTaskResponse> recentTasks) {
}
