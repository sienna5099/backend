package com.aicareermentor.backend.dto;

public class ProfileResponse {

    private Long id;
    private Long userId;
    private String qualification;
    private String field;
    private Integer gradYear;
    private String interests;
    private String goal;

    public ProfileResponse() {
    }

    public ProfileResponse(
            Long id,
            Long userId,
            String qualification,
            String field,
            Integer gradYear,
            String interests,
            String goal
    ) {
        this.id = id;
        this.userId = userId;
        this.qualification = qualification;
        this.field = field;
        this.gradYear = gradYear;
        this.interests = interests;
        this.goal = goal;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getQualification() {
        return qualification;
    }

    public String getField() {
        return field;
    }

    public Integer getGradYear() {
        return gradYear;
    }

    public String getInterests() {
        return interests;
    }

    public String getGoal() {
        return goal;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setGradYear(Integer gradYear) {
        this.gradYear = gradYear;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}
