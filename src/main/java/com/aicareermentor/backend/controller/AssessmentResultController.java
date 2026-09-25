package com.aicareermentor.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

import com.aicareermentor.backend.entity.Assessment;
import com.aicareermentor.backend.entity.AssessmentAnswer;
import com.aicareermentor.backend.repository.AssessmentRepository;
import com.aicareermentor.backend.service.AssessmentAnswerService;
import com.aicareermentor.backend.service.AssessmentResultService;
import com.aicareermentor.backend.service.AssessmentService;
import com.aicareermentor.backend.service.AuthService;

@RestController
@RequestMapping("/api/assessment-results")
@CrossOrigin(origins = "http://localhost:5173")
public class AssessmentResultController {

    private final AssessmentAnswerService assessmentAnswerService;
    private final AssessmentResultService assessmentResultService;
    private final AssessmentService assessmentService;
        private final AssessmentRepository assessmentRepository;
        private final AuthService authService;

    public AssessmentResultController(
            AssessmentAnswerService assessmentAnswerService,
            AssessmentResultService assessmentResultService,
            AssessmentService assessmentService,
            AssessmentRepository assessmentRepository,
            AuthService authService) {

        this.assessmentAnswerService = assessmentAnswerService;
        this.assessmentResultService = assessmentResultService;
        this.assessmentService = assessmentService;
        this.assessmentRepository = assessmentRepository;
        this.authService = authService;
    }

    @GetMapping("/{assessmentId}")
    public ResponseEntity<Map<String, Object>> getAssessmentResult(
                        @PathVariable Long assessmentId,
                        HttpServletRequest request) {

                Assessment assessment = assessmentRepository.findById(assessmentId).orElse(null);
                if (assessment == null || !authService.ownsUser(request, assessment.getUserId())) {
                        return ResponseEntity.status(403).build();
                }

        return ResponseEntity.ok(buildResult(assessment));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getAssessmentResultForUser(
                        @PathVariable Long userId,
                        HttpServletRequest request) {

                if (!authService.ownsUser(request, userId)) {
                        return ResponseEntity.status(403).build();
                }

                Assessment assessment = assessmentRepository.findByUserId(userId).orElse(null);
                if (assessment == null) {
                        return ResponseEntity.notFound().build();
                }

                return ResponseEntity.ok(buildResult(assessment));
    }

    private Map<String, Object> buildResult(Assessment assessment) {
        List<AssessmentAnswer> answers =
                assessmentAnswerService.getAnswersByAssessmentId(assessment.getId());

        Map<String, Integer> traitScores =
                assessmentResultService.calculateTraitScores(answers);

        String topTrait =
                assessmentResultService.findTopTrait(traitScores);

        assessmentService.updateTopTrait(assessment.getId(), topTrait);

        Map<String, Object> result = new HashMap<>();
        result.put("assessmentId", assessment.getId());
        result.put("traitScores", traitScores);
        result.put("topTrait", topTrait);

        return result;
    }
}
