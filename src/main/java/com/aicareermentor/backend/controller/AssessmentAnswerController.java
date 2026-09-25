package com.aicareermentor.backend.controller;

import com.aicareermentor.backend.entity.AssessmentAnswer;
import com.aicareermentor.backend.service.AssessmentAnswerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessment-answers")
@CrossOrigin(origins = "http://localhost:5173")
public class AssessmentAnswerController {

    private final AssessmentAnswerService assessmentAnswerService;

    public AssessmentAnswerController(
            AssessmentAnswerService assessmentAnswerService) {
        this.assessmentAnswerService = assessmentAnswerService;
    }

    @PostMapping
    public ResponseEntity<AssessmentAnswer> saveAnswer(
            @RequestBody AssessmentAnswer answer) {

        return ResponseEntity.ok(
                assessmentAnswerService.saveAnswer(answer)
        );
    }

    @GetMapping("/assessment/{assessmentId}")
    public ResponseEntity<List<AssessmentAnswer>> getAnswers(
            @PathVariable Long assessmentId) {

        return ResponseEntity.ok(
                assessmentAnswerService.getAnswersByAssessmentId(assessmentId)
        );
    }
}
