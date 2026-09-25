package com.aicareermentor.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "roadmap_tasks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"career_id", "level_number", "sort_order"})
)
public class RoadmapTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "career_id", nullable = false)
    private Career career;

    @Column(name = "level_number", nullable = false)
    private Integer levelNumber;

    @Column(nullable = false, length = 120)
    private String levelTitle;

    @Column(length = 500)
    private String levelDescription;

    @Column(nullable = false, length = 180)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    public RoadmapTask() {
    }

    public RoadmapTask(Career career, Integer levelNumber, String levelTitle, String levelDescription,
                       String title, String description, Integer sortOrder) {
        this.career = career;
        this.levelNumber = levelNumber;
        this.levelTitle = levelTitle;
        this.levelDescription = levelDescription;
        this.title = title;
        this.description = description;
        this.sortOrder = sortOrder;
    }

    public Long getId() {
        return id;
    }

    public Career getCareer() {
        return career;
    }

    public Integer getLevelNumber() {
        return levelNumber;
    }

    public String getLevelTitle() {
        return levelTitle;
    }

    public String getLevelDescription() {
        return levelDescription;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }
}
