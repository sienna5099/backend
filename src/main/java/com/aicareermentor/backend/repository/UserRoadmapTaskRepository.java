package com.aicareermentor.backend.repository;

import com.aicareermentor.backend.entity.UserRoadmapTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoadmapTaskRepository extends JpaRepository<UserRoadmapTask, Long> {

    java.util.List<UserRoadmapTask> findByUserIdAndRoadmapTask_Career_CareerId(Long userId, String careerId);

    Optional<UserRoadmapTask> findByUserIdAndRoadmapTask_Id(Long userId, Long roadmapTaskId);
}
