package com.aicareermentor.backend.controller;

import com.aicareermentor.backend.dto.MentorChatRequest;
import com.aicareermentor.backend.dto.MentorChatResponse;
import com.aicareermentor.backend.service.MentorChatService;
import com.aicareermentor.backend.service.AuthService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mentor-chat")
@CrossOrigin(origins = "http://localhost:5173")
public class MentorChatController {

    private final MentorChatService mentorChatService;
    private final AuthService authService;

    public MentorChatController(MentorChatService mentorChatService, AuthService authService) {
        this.mentorChatService = mentorChatService;
        this.authService = authService;
    }

    @GetMapping("/{userId}/{mentorId}")
    public ResponseEntity<List<MentorChatResponse>> getHistory(
            @PathVariable Long userId,
            @PathVariable Long mentorId,
            HttpServletRequest request) {
        if (!authService.ownsUser(request, userId)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(mentorChatService.getHistory(userId, mentorId));
    }

    @PostMapping
    public ResponseEntity<MentorChatResponse> sendMessage(
            @Valid @RequestBody MentorChatRequest request,
            HttpServletRequest httpRequest) {
        if (!authService.ownsUser(httpRequest, request.userId())) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(mentorChatService.sendMessage(request));
    }
}
