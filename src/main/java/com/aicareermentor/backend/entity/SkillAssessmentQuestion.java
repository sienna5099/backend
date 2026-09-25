package com.aicareermentor.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(
        name = "skill_assessment_questions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"career_id", "sequence_number"})
        }
)
public class SkillAssessmentQuestion {

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

    @Column(nullable = false, length = 500)
    private String question;

    @Column(name = "sequence_number", nullable = false)
    private Integer sequenceNumber;

    @Column(nullable = false)
    private Boolean active = true;

    public SkillAssessmentQuestion() {
    }

    public SkillAssessmentQuestion(
            Career career,
            Skill skill,
            String question,
            Integer sequenceNumber,
            Boolean active) {
        this.career = career;
        this.skill = skill;
        this.question = question;
        this.sequenceNumber = sequenceNumber;
        this.active = active;
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

    public String getQuestion() {
        return question;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public Boolean getActive() {
        return active;
    }
}
