package com.aicareermentor.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(
        name = "career_skills",
        uniqueConstraints = @UniqueConstraint(columnNames = {"career_id", "skill_id"})
)
public class CareerSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "career_id", nullable = false)
    @JsonIgnore
    private Career career;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(nullable = false)
    private Integer priority;

    public CareerSkill() {
    }

    public CareerSkill(Career career, Skill skill, Integer priority) {
        this.career = career;
        this.skill = skill;
        this.priority = priority;
    }

    public Long getId() {
        return id;
    }

    public Career getCareer() {
        return career;
    }

    public Skill getSkill() {
        return skill;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setCareer(Career career) {
        this.career = career;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
