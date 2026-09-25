package com.aicareermentor.backend.controller;

import com.aicareermentor.backend.dto.SkillGapAnalysisResponse;
import com.aicareermentor.backend.dto.SkillGapAnalyzeRequest;
import com.aicareermentor.backend.dto.SkillGapResponse;
import com.aicareermentor.backend.service.SkillAssessmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/skill-gap")
@CrossOrigin(origins = "http://localhost:5173")
public class SkillGapController {

    private final SkillAssessmentService skillAssessmentService;

    public SkillGapController(
            SkillAssessmentService skillAssessmentService) {

        this.skillAssessmentService =
                skillAssessmentService;
    }

    /*
     * Existing rule-based endpoint.
     */
    @GetMapping("/{userId}/{careerId}")
    public ResponseEntity<SkillGapResponse> getSkillGap(
            @PathVariable Long userId,
            @PathVariable String careerId) {

        return ResponseEntity.ok(
                skillAssessmentService.getSkillGap(
                        userId,
                        careerId));
    }

    /*
     * New Groq AI endpoint.
     */
    @PostMapping("/analyze")
    public ResponseEntity<SkillGapAnalysisResponse> analyzeSkillGap(
            @Valid @RequestBody SkillGapAnalyzeRequest request) {

        return ResponseEntity.ok(
                skillAssessmentService.analyzeSkillGap(
                        request));
    }
}