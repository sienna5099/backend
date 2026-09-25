package com.aicareermentor.backend.controller;

import com.aicareermentor.backend.dto.RoadmapResponse;
import com.aicareermentor.backend.dto.RoadmapTaskUpdateRequest;
import com.aicareermentor.backend.service.AuthService;
import com.aicareermentor.backend.service.RoadmapService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roadmap")
@CrossOrigin(origins = "http://localhost:5173")
public class RoadmapController {

    private final RoadmapService roadmapService;
    private final AuthService authService;

    public RoadmapController(RoadmapService roadmapService, AuthService authService) {
        this.roadmapService = roadmapService;
        this.authService = authService;
    }

    @GetMapping("/{userId}/{careerId}")
    public ResponseEntity<RoadmapResponse> getRoadmap(
            @PathVariable Long userId,
            @PathVariable String careerId,
            HttpServletRequest request) {
        requireOwner(request, userId);
        return ResponseEntity.ok(roadmapService.getRoadmap(userId, careerId));
    }

    @PatchMapping("/{userId}/{careerId}/tasks/{taskId}")
    public ResponseEntity<RoadmapResponse> updateTask(
            @PathVariable Long userId,
            @PathVariable String careerId,
            @PathVariable Long taskId,
            @Valid @RequestBody RoadmapTaskUpdateRequest update,
            HttpServletRequest request) {
        requireOwner(request, userId);
        return ResponseEntity.ok(roadmapService.updateTask(userId, careerId, taskId, update));
    }

    private void requireOwner(HttpServletRequest request, Long userId) {
        if (!authService.ownsUser(request, userId)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN, "You cannot access another user's roadmap");
        }
    }
}
