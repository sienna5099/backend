package com.aicareermentor.backend.dto;

import java.util.List;

public record SkillGapAnalysisResponse(

        String career,

        String summary,

        Integer overallReadiness,

        List<SkillGapAnalysisItem> skills,

        List<String> nextSteps

) {
}