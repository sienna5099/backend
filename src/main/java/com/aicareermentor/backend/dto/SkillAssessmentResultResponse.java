package com.aicareermentor.backend.dto;

import java.util.List;

public record SkillAssessmentResultResponse(
        boolean assessmentCompleted,
        String careerId,
        List<SkillAssessmentSkillResult> skills) {
}
