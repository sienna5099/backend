package com.aicareermentor.backend.repository;

import com.aicareermentor.backend.entity.AssessmentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentAnswerRepository extends JpaRepository<AssessmentAnswer, Long> {

    List<AssessmentAnswer> findByAssessmentId(Long assessmentId);

    void deleteByAssessmentId(Long assessmentId);
}