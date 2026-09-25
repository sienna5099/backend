package com.aicareermentor.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "careers")
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String careerId;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 500)
    private String tagline;

    @Column(length = 2000)
    private String description;

    @Column(length = 1000)
    private String growth;

    @Column(length = 255)
    private String salaryRange;

    @Column(length = 1000)
    private String skills;

    @Column(length = 1500)
    private String responsibilities;

    @Column(length = 255)
    private String avgSalary;

    @Column(length = 100)
    private String demand;

    @Column(length = 150)
    private String topLocation;

    @Column(length = 1000)
    private String globalOpportunities;

    @Column(nullable = false)
    private Integer analyticalScore;

    @Column(nullable = false)
    private Integer technicalScore;

    @Column(nullable = false)
    private Integer communicationScore;

    @Column(nullable = false)
    private Integer leadershipScore;

    @Column(nullable = false)
    private Integer creativeScore;

    @OneToMany(mappedBy = "career")
    @JsonIgnore
    private List<CareerSkill> careerSkills = new ArrayList<>();

    public Career() {
    }

    public Long getId() {
        return id;
    }

    public String getCareerId() {
        return careerId;
    }

    public String getTitle() {
        return title;
    }

    public String getTagline() {
        return tagline;
    }

    public String getDescription() {
        return description;
    }

    public String getGrowth() {
        return growth;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public String getSkills() {
        return skills;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public String getAvgSalary() {
        return avgSalary;
    }

    public String getDemand() {
        return demand;
    }

    public String getTopLocation() {
        return topLocation;
    }

    public String getGlobalOpportunities() {
        return globalOpportunities;
    }

    public Integer getAnalyticalScore() {
        return analyticalScore;
    }

    public Integer getTechnicalScore() {
        return technicalScore;
    }

    public Integer getCommunicationScore() {
        return communicationScore;
    }

    public Integer getLeadershipScore() {
        return leadershipScore;
    }

    public Integer getCreativeScore() {
        return creativeScore;
    }

    public List<CareerSkill> getCareerSkills() {
        return careerSkills;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCareerId(String careerId) {
        this.careerId = careerId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGrowth(String growth) {
        this.growth = growth;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public void setAvgSalary(String avgSalary) {
        this.avgSalary = avgSalary;
    }

    public void setDemand(String demand) {
        this.demand = demand;
    }

    public void setTopLocation(String topLocation) {
        this.topLocation = topLocation;
    }

    public void setGlobalOpportunities(String globalOpportunities) {
        this.globalOpportunities = globalOpportunities;
    }

    public void setAnalyticalScore(Integer analyticalScore) {
        this.analyticalScore = analyticalScore;
    }

    public void setTechnicalScore(Integer technicalScore) {
        this.technicalScore = technicalScore;
    }

    public void setCommunicationScore(Integer communicationScore) {
        this.communicationScore = communicationScore;
    }

    public void setLeadershipScore(Integer leadershipScore) {
        this.leadershipScore = leadershipScore;
    }

    public void setCreativeScore(Integer creativeScore) {
        this.creativeScore = creativeScore;
    }
}
