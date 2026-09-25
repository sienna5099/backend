package com.aicareermentor.backend.repository;

import com.aicareermentor.backend.entity.SkillAssessmentQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SkillAssessmentQuestionRepository
        extends JpaRepository<SkillAssessmentQuestion, Long> {

    List<SkillAssessmentQuestion> findByCareer_CareerIdAndActiveTrueOrderBySequenceNumberAsc(
            String careerId);

    Optional<SkillAssessmentQuestion> findByCareer_CareerIdAndSkill_NameAndSequenceNumber(
            String careerId,
            String skillName,
            Integer sequenceNumber);
}
