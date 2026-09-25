package com.aicareermentor.backend.controller;

import com.aicareermentor.backend.dto.AssessmentRequest;
import com.aicareermentor.backend.dto.AssessmentResponse;
import com.aicareermentor.backend.service.AuthService;
import com.aicareermentor.backend.service.AssessmentService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assessment")
@CrossOrigin(origins = "http://localhost:5173")
public class AssessmentController {

    private final AssessmentService assessmentService;
    private final AuthService authService;

    public AssessmentController(AssessmentService assessmentService, AuthService authService) {
        this.assessmentService = assessmentService;
        this.authService = authService;
    }

    @PostMapping("/submit")
    public ResponseEntity<AssessmentResponse> submitAssessment(
            @Valid @RequestBody AssessmentRequest request
            , HttpServletRequest httpRequest
    ) {
        if (!authService.ownsUser(httpRequest, request.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        AssessmentResponse response =
                assessmentService.submitAssessment(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
