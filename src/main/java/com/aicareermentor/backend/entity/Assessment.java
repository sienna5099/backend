package com.aicareermentor.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "assessments")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Double analyticalScore;

    @Column(nullable = false)
    private Double technicalScore;

    @Column(nullable = false)
    private Double communicationScore;

    @Column(nullable = false)
    private Double leadershipScore;

    @Column(nullable = false)
    private Double creativeScore;

    @Column(nullable = false, length = 50)
    private String topTrait;

    public Assessment() {
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAnalyticalScore() {
        return analyticalScore;
    }

    public void setAnalyticalScore(Double analyticalScore) {
        this.analyticalScore = analyticalScore;
    }

    public Double getTechnicalScore() {
        return technicalScore;
    }

    public void setTechnicalScore(Double technicalScore) {
        this.technicalScore = technicalScore;
    }

    public Double getCommunicationScore() {
        return communicationScore;
    }

    public void setCommunicationScore(Double communicationScore) {
        this.communicationScore = communicationScore;
    }

    public Double getLeadershipScore() {
        return leadershipScore;
    }

    public void setLeadershipScore(Double leadershipScore) {
        this.leadershipScore = leadershipScore;
    }

    public Double getCreativeScore() {
        return creativeScore;
    }

    public void setCreativeScore(Double creativeScore) {
        this.creativeScore = creativeScore;
    }

    public String getTopTrait() {
        return topTrait;
    }

    public void setTopTrait(String topTrait) {
        this.topTrait = topTrait;
    }
}
