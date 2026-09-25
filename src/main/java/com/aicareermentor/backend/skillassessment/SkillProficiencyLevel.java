package com.aicareermentor.backend.skillassessment;

public enum SkillProficiencyLevel {
    NEVER,
    BEGINNER,
    BASIC,
    COMFORTABLE,
    ADVANCED;

    public static SkillProficiencyLevel fromScore(int score) {
        return switch (score) {
            case 1 -> NEVER;
            case 2 -> BEGINNER;
            case 3 -> BASIC;
            case 4 -> COMFORTABLE;
            case 5 -> ADVANCED;
            default -> throw new IllegalArgumentException("Skill score must be between 1 and 5");
        };
    }

    public String label() {
        return switch (this) {
            case NEVER -> "Never / No experience";
            case BEGINNER -> "Beginner";
            case BASIC -> "Basic / Some experience";
            case COMFORTABLE -> "Comfortable";
            case ADVANCED -> "Advanced";
        };
    }

    public String status() {
        return switch (this) {
            case COMFORTABLE, ADVANCED -> "STRONG";
            case BASIC -> "DEVELOPING";
            case NEVER, BEGINNER -> "NEEDS_DEVELOPMENT";
        };
    }
}
