package com.aicareermentor.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(
        name = "skill_assessment_answers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "career_id", "question_id"})
        }
)
public class SkillAssessmentAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "career_id", nullable = false)
    @JsonIgnore
    private Career career;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    @JsonIgnore
    private SkillAssessmentQuestion question;

    @Column(nullable = false)
    private Integer value;

    public SkillAssessmentAnswer() {
    }

    public SkillAssessmentAnswer(
            User user,
            Career career,
            SkillAssessmentQuestion question,
            Integer value) {
        this.user = user;
        this.career = career;
        this.question = question;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Career getCareer() {
        return career;
    }

    public SkillAssessmentQuestion getQuestion() {
        return question;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
