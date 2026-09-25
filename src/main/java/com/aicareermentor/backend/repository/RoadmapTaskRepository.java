package com.aicareermentor.backend.repository;

import com.aicareermentor.backend.entity.RoadmapTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoadmapTaskRepository extends JpaRepository<RoadmapTask, Long> {

    List<RoadmapTask> findByCareer_CareerIdOrderByLevelNumberAscSortOrderAsc(String careerId);
}
