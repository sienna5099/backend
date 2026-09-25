package com.aicareermentor.backend.repository;

import com.aicareermentor.backend.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    Optional<Assessment> findByUserId(Long userId);
}
