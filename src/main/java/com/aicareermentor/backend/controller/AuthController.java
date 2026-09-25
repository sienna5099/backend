package com.aicareermentor.backend.controller;

import com.aicareermentor.backend.dto.LoginRequest;
import com.aicareermentor.backend.dto.AuthResult;
import com.aicareermentor.backend.dto.ForgotPasswordRequest;
import com.aicareermentor.backend.dto.ForgotPasswordResponse;
import com.aicareermentor.backend.dto.ResetPasswordRequest;
import com.aicareermentor.backend.dto.SignupRequest;
import com.aicareermentor.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(
            @Valid @RequestBody SignupRequest request) {

        AuthResult result = authService.signup(request);

        Map<String, Object> response = new HashMap<>();

        response.put("success", true);
        response.put("user", result.user());
        response.put("token", result.token());
        response.put("message", "Account created successfully");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResult result = authService.login(request);

        Map<String, Object> response = new HashMap<>();

        response.put("success", true);
        response.put("user", result.user());
        response.put("token", result.token());
        response.put("message", "Login successful");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        authService.logout(extractToken(authorization));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(authService.forgotPassword(request.email()));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(Map.of("success", true, "message", "Password reset successful."));
    }

    private String extractToken(String authorization) {
        return authorization != null && authorization.startsWith("Bearer ")
                ? authorization.substring(7).trim()
                : null;
    }
}
