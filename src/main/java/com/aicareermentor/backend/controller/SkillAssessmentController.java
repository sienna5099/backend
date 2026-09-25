package com.aicareermentor.backend.controller;

import com.aicareermentor.backend.dto.SkillAssessmentQuestionResponse;
import com.aicareermentor.backend.dto.SkillAssessmentResultResponse;
import com.aicareermentor.backend.dto.SkillAssessmentSubmitRequest;
import com.aicareermentor.backend.dto.SkillAssessmentSubmitResponse;
import com.aicareermentor.backend.service.SkillAssessmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skill-assessment")
@CrossOrigin(origins = "http://localhost:5173")
public class SkillAssessmentController {

    private final SkillAssessmentService skillAssessmentService;

    public SkillAssessmentController(SkillAssessmentService skillAssessmentService) {
        this.skillAssessmentService = skillAssessmentService;
    }

    @GetMapping("/questions/{careerId}")
    public ResponseEntity<List<SkillAssessmentQuestionResponse>> getQuestions(
            @PathVariable String careerId) {
        return ResponseEntity.ok(skillAssessmentService.getQuestions(careerId));
    }

    @PostMapping("/submit")
    public ResponseEntity<SkillAssessmentSubmitResponse> submit(
            @Valid @RequestBody SkillAssessmentSubmitRequest request) {
        return ResponseEntity.ok(skillAssessmentService.submit(request));
    }

    @GetMapping("/result/{userId}/{careerId}")
    public ResponseEntity<SkillAssessmentResultResponse> getResult(
            @PathVariable Long userId,
            @PathVariable String careerId) {
        return ResponseEntity.ok(skillAssessmentService.getResult(userId, careerId));
    }

}
