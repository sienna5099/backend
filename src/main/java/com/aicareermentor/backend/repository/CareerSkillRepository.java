package com.aicareermentor.backend.repository;

import com.aicareermentor.backend.entity.CareerSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CareerSkillRepository extends JpaRepository<CareerSkill, Long> {

    boolean existsByCareer_IdAndSkill_Id(Long careerId, Long skillId);

    List<CareerSkill> findByCareer_CareerIdOrderByPriorityAsc(String careerId);
}
