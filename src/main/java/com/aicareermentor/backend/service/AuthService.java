package com.aicareermentor.backend.service;

import com.aicareermentor.backend.dto.LoginRequest;
import com.aicareermentor.backend.dto.AuthResult;
import com.aicareermentor.backend.dto.ForgotPasswordResponse;
import com.aicareermentor.backend.dto.ResetPasswordRequest;
import com.aicareermentor.backend.dto.SignupRequest;
import com.aicareermentor.backend.dto.UserResponse;
import com.aicareermentor.backend.entity.User;
import com.aicareermentor.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;

import static com.aicareermentor.backend.config.AuthInterceptor.AUTHENTICATED_USER_ID;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

        public AuthResult signup(SignupRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "An account with this email already exists"
            );
        }

        User user = new User();

        user.setName(request.getName().trim());
        user.setEmail(email);

        // Password is HASHED before being stored.
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        User savedUser = userRepository.save(user);

                return issueSession(new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
                ), savedUser);
    }

        public AuthResult login(LoginRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid email or password"
                        )
                );

        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatches) {
            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }

                return issueSession(new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
                ), user);
        }

        public Long authenticate(String token) {
                if (token == null || token.isBlank()) return null;
                return userRepository.findBySessionToken(token)
                                .filter(user -> user.getSessionExpiresAt() != null
                                                && user.getSessionExpiresAt().isAfter(Instant.now()))
                                .map(User::getId)
                                .orElse(null);
        }

        public boolean ownsUser(HttpServletRequest request, Long userId) {
                return userId != null
                                && userId.equals(request.getAttribute(AUTHENTICATED_USER_ID));
        }

        public void logout(String token) {
                if (token == null || token.isBlank()) return;
                userRepository.findBySessionToken(token).ifPresent(user -> {
                        user.setSessionToken(null);
                        user.setSessionExpiresAt(null);
                        userRepository.save(user);
                });
        }

        public ForgotPasswordResponse forgotPassword(String email) {
                User user = userRepository.findByEmail(email.trim().toLowerCase()).orElse(null);
                if (user == null) {
                        return new ForgotPasswordResponse(
                                        "If an account exists for this email, a reset link has been created.", null);
                }

                String token = UUID.randomUUID().toString();
                user.setResetToken(token);
                user.setResetTokenExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES));
                userRepository.save(user);
                return new ForgotPasswordResponse(
                                "A password reset token has been created.", token);
        }

        public void resetPassword(ResetPasswordRequest request) {
                User user = userRepository.findByResetToken(request.token())
                                .filter(candidate -> candidate.getResetTokenExpiresAt() != null
                                                && candidate.getResetTokenExpiresAt().isAfter(Instant.now()))
                                .orElseThrow(() -> new IllegalArgumentException("Reset token is invalid or expired"));

                user.setPassword(passwordEncoder.encode(request.password()));
                user.setResetToken(null);
                user.setResetTokenExpiresAt(null);
                user.setSessionToken(null);
                user.setSessionExpiresAt(null);
                userRepository.save(user);
        }

        private AuthResult issueSession(UserResponse response, User user) {
                String token = UUID.randomUUID().toString();
                user.setSessionToken(token);
                user.setSessionExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS));
                userRepository.save(user);
                return new AuthResult(response, token);
    }
}
