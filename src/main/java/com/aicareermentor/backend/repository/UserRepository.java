package com.aicareermentor.backend.repository;

import com.aicareermentor.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findBySessionToken(String sessionToken);

    Optional<User> findByResetToken(String resetToken);
}
