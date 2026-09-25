package com.aicareermentor.backend.controller;

import com.aicareermentor.backend.dto.RecommendationResponse;
import com.aicareermentor.backend.service.RecommendationService;
import com.aicareermentor.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/careers/recommendations")
@CrossOrigin(origins = "http://localhost:5173")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final AuthService authService;

    public RecommendationController(RecommendationService recommendationService, AuthService authService) {
        this.recommendationService = recommendationService;
        this.authService = authService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<RecommendationResponse>> getRecommendations(
            @PathVariable Long userId,
            HttpServletRequest request) {
        if (!authService.ownsUser(request, userId)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(recommendationService.getRecommendations(userId));
    }
}
