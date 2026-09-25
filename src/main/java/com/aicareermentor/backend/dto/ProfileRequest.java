package com.aicareermentor.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProfileRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Qualification is required")
    @Size(max = 100, message = "Qualification must not exceed 100 characters")
    private String qualification;

    @NotBlank(message = "Field is required")
    @Size(max = 150, message = "Field must not exceed 150 characters")
    private String field;

    @NotNull(message = "Graduation year is required")
    @Min(value = 2000, message = "Graduation year must be 2000 or later")
    @Max(value = 2100, message = "Graduation year is invalid")
    private Integer gradYear;

    @NotBlank(message = "Interests are required")
    @Size(max = 500, message = "Interests must not exceed 500 characters")
    private String interests;

    @NotBlank(message = "Career goal is required")
    @Size(max = 500, message = "Career goal must not exceed 500 characters")
    private String goal;

    public ProfileRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Integer getGradYear() {
        return gradYear;
    }

    public void setGradYear(Integer gradYear) {
        this.gradYear = gradYear;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}
