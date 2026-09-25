package com.aicareermentor.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aicareermentor.backend.entity.Mentor;

public interface MentorRepository extends JpaRepository<Mentor, Long> {

    List<Mentor> findByAvailableTrue();

    List<Mentor> findBySpecializationIgnoreCase(String specialization);
}
