package com.aicareermentor.backend.dto;

import java.util.List;

public record SkillGapResponse(
        boolean assessmentCompleted,
        String careerId,
        List<SkillGapSkillResponse> skills) {
}
