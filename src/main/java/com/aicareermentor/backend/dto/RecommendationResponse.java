package com.aicareermentor.backend.dto;

import java.util.List;

public class RecommendationResponse {

    private String careerId;
    private String careerTitle;
    private int matchPercentage;
    private List<String> strongestTraits;
    private List<String> growthAreas;

    public RecommendationResponse() {
    }

    public RecommendationResponse(
            String careerId,
            String careerTitle,
            int matchPercentage,
            List<String> strongestTraits,
            List<String> growthAreas) {
        this.careerId = careerId;
        this.careerTitle = careerTitle;
        this.matchPercentage = matchPercentage;
        this.strongestTraits = strongestTraits;
        this.growthAreas = growthAreas;
    }

    public String getCareerId() {
        return careerId;
    }

    public void setCareerId(String careerId) {
        this.careerId = careerId;
    }

    public String getCareerTitle() {
        return careerTitle;
    }

    public void setCareerTitle(String careerTitle) {
        this.careerTitle = careerTitle;
    }

    public int getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(int matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    public List<String> getStrongestTraits() {
        return strongestTraits;
    }

    public void setStrongestTraits(List<String> strongestTraits) {
        this.strongestTraits = strongestTraits;
    }

    public List<String> getGrowthAreas() {
        return growthAreas;
    }

    public void setGrowthAreas(List<String> growthAreas) {
        this.growthAreas = growthAreas;
    }
}
