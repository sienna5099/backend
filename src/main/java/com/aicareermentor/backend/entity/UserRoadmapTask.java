package com.aicareermentor.backend.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "user_roadmap_tasks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "roadmap_task_id"})
)
public class UserRoadmapTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "roadmap_task_id", nullable = false)
    private RoadmapTask roadmapTask;

    @Column(nullable = false)
    private Boolean completed = false;

    private Instant completedAt;

    public UserRoadmapTask() {
    }

    public UserRoadmapTask(Long userId, RoadmapTask roadmapTask, Boolean completed, Instant completedAt) {
        this.userId = userId;
        this.roadmapTask = roadmapTask;
        this.completed = completed;
        this.completedAt = completedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public RoadmapTask getRoadmapTask() {
        return roadmapTask;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }
}
