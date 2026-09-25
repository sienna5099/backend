package com.aicareermentor.backend.service;

import com.aicareermentor.backend.dto.RoadmapLevelResponse;
import com.aicareermentor.backend.dto.RoadmapResponse;
import com.aicareermentor.backend.dto.RoadmapTaskResponse;
import com.aicareermentor.backend.dto.RoadmapTaskUpdateRequest;
import com.aicareermentor.backend.entity.Career;
import com.aicareermentor.backend.entity.RoadmapTask;
import com.aicareermentor.backend.entity.UserRoadmapTask;
import com.aicareermentor.backend.exception.ResourceNotFoundException;
import com.aicareermentor.backend.repository.AssessmentRepository;
import com.aicareermentor.backend.repository.CareerRepository;
import com.aicareermentor.backend.repository.RoadmapTaskRepository;
import com.aicareermentor.backend.repository.UserRoadmapTaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoadmapService {

    private final CareerRepository careerRepository;
    private final RoadmapTaskRepository roadmapTaskRepository;
    private final UserRoadmapTaskRepository userRoadmapTaskRepository;
    private final AssessmentRepository assessmentRepository;

    public RoadmapService(
            CareerRepository careerRepository,
            RoadmapTaskRepository roadmapTaskRepository,
            UserRoadmapTaskRepository userRoadmapTaskRepository,
            AssessmentRepository assessmentRepository) {
        this.careerRepository = careerRepository;
        this.roadmapTaskRepository = roadmapTaskRepository;
        this.userRoadmapTaskRepository = userRoadmapTaskRepository;
        this.assessmentRepository = assessmentRepository;
    }

    @Transactional(readOnly = true)
    public RoadmapResponse getRoadmap(Long userId, String careerId) {
        Career career = getCareer(careerId);
        List<RoadmapTask> tasks = roadmapTaskRepository
                .findByCareer_CareerIdOrderByLevelNumberAscSortOrderAsc(career.getCareerId());
        if (tasks.isEmpty()) {
            throw new IllegalArgumentException("No roadmap tasks are configured for career: " + career.getCareerId());
        }

        boolean assessmentCompleted = assessmentRepository.findByUserId(userId).isPresent();
        List<UserRoadmapTask> savedTasks = userRoadmapTaskRepository
            .findByUserIdAndRoadmapTask_Career_CareerId(userId, career.getCareerId());
        Map<Long, Boolean> savedCompletion = savedTasks
                .stream()
                .collect(Collectors.toMap(item -> item.getRoadmapTask().getId(), UserRoadmapTask::getCompleted));

        Map<Integer, List<RoadmapTask>> tasksByLevel = tasks.stream()
                .collect(Collectors.groupingBy(RoadmapTask::getLevelNumber, LinkedHashMap::new, Collectors.toList()));
        List<RoadmapLevelResponse> levels = new ArrayList<>();
        boolean previousLevelCompleted = true;
        int currentLevel = 7;
        int completedTotal = 0;
        int total = 0;

        for (Map.Entry<Integer, List<RoadmapTask>> entry : tasksByLevel.entrySet()) {
            int levelNumber = entry.getKey();
            List<RoadmapTask> levelTasks = entry.getValue();
            List<RoadmapTaskResponse> taskResponses = levelTasks.stream()
                    .map(task -> new RoadmapTaskResponse(
                            task.getId(),
                            task.getTitle(),
                            task.getDescription(),
                            isCompleted(task, assessmentCompleted, savedCompletion)))
                    .toList();
            int completed = (int) taskResponses.stream().filter(RoadmapTaskResponse::completed).count();
            int levelTotal = taskResponses.size();
            boolean levelCompleted = completed == levelTotal;
            String status = levelCompleted ? "COMPLETED" : previousLevelCompleted ? "CURRENT" : "LOCKED";
            if ("CURRENT".equals(status) && currentLevel == 7) currentLevel = levelNumber;
            levels.add(new RoadmapLevelResponse(
                    levelNumber,
                    levelTasks.get(0).getLevelTitle(),
                    levelTasks.get(0).getLevelDescription(),
                    status,
                    percentage(completed, levelTotal),
                    completed,
                    levelTotal,
                    taskResponses));
            completedTotal += completed;
            total += levelTotal;
            previousLevelCompleted = levelCompleted;
        }

        if (levels.stream().noneMatch(level -> "CURRENT".equals(level.status())) && !levels.isEmpty()) {
            currentLevel = levels.get(levels.size() - 1).levelNumber();
        }

        List<RoadmapTaskResponse> recentTasks = savedTasks.stream()
            .filter(item -> Boolean.TRUE.equals(item.getCompleted()))
            .sorted(Comparator.comparing(UserRoadmapTask::getCompletedAt, Comparator.nullsLast(Comparator.reverseOrder())))
            .limit(5)
            .map(item -> new RoadmapTaskResponse(
                item.getRoadmapTask().getId(),
                item.getRoadmapTask().getTitle(),
                item.getRoadmapTask().getDescription(),
                true))
            .toList();

        return new RoadmapResponse(
                career.getCareerId(),
                career.getTitle(),
                percentage(completedTotal, total),
                currentLevel,
            levels,
            recentTasks);
    }

    @Transactional
    public RoadmapResponse updateTask(Long userId, String careerId, Long taskId, RoadmapTaskUpdateRequest request) {
        RoadmapResponse roadmap = getRoadmap(userId, careerId);
        RoadmapTask task = roadmapTaskRepository.findById(taskId)
                .filter(candidate -> candidate.getCareer().getCareerId().equals(careerId))
                .orElseThrow(() -> new ResourceNotFoundException("Roadmap task not found: " + taskId));
        if (task.getLevelNumber() <= 2) {
            throw new IllegalArgumentException("Assessment milestones are completed from assessment progress.");
        }
        if (task.getLevelNumber() > roadmap.currentLevel()) {
            throw new IllegalArgumentException("Complete the current roadmap level before starting this task.");
        }

        UserRoadmapTask saved = userRoadmapTaskRepository
                .findByUserIdAndRoadmapTask_Id(userId, taskId)
                .orElseGet(() -> new UserRoadmapTask(userId, task, false, null));
        saved.setCompleted(request.completed());
        saved.setCompletedAt(request.completed() ? Instant.now() : null);
        userRoadmapTaskRepository.save(saved);
        return getRoadmap(userId, careerId);
    }

    private Career getCareer(String careerId) {
        if (careerId == null || careerId.isBlank()) {
            throw new IllegalArgumentException("Career ID is required");
        }
        return careerRepository.findByCareerId(careerId.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Career not found: " + careerId));
    }

    private boolean isCompleted(RoadmapTask task, boolean assessmentCompleted, Map<Long, Boolean> savedCompletion) {
        if (task.getLevelNumber() <= 2) return assessmentCompleted;
        return Boolean.TRUE.equals(savedCompletion.get(task.getId()));
    }

    private int percentage(int completed, int total) {
        return total == 0 ? 0 : (int) Math.round((completed * 100.0) / total);
    }
}
