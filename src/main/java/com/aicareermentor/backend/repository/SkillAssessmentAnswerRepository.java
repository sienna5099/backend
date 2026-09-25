package com.aicareermentor.backend.repository;

import com.aicareermentor.backend.entity.SkillAssessmentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SkillAssessmentAnswerRepository
        extends JpaRepository<SkillAssessmentAnswer, Long> {

    List<SkillAssessmentAnswer> findByUser_IdAndCareer_CareerId(
            Long userId,
            String careerId);

    Optional<SkillAssessmentAnswer> findByUser_IdAndCareer_CareerIdAndQuestion_Id(
            Long userId,
            String careerId,
            Long questionId);
}
