package com.aicareermentor.backend.dto;

import java.util.List;

public record SkillGapAnalysisItem(

        String skillName,

        Integer currentLevel,

        Integer requiredLevel,

        Integer gap,

        String priority,

        String reason,

        List<String> recommendations

) {
}