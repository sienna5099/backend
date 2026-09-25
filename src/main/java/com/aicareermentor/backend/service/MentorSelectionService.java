package com.aicareermentor.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aicareermentor.backend.entity.Mentor;

@Service
public class MentorSelectionService {

    private final MentorService mentorService;

    public MentorSelectionService(MentorService mentorService) {
        this.mentorService = mentorService;
    }

    public List<Mentor> findMentorsForTrait(String topTrait) {

        if (topTrait == null || topTrait.isBlank()) {
            return List.of();
        }

        String specialization = mapTraitToSpecialization(topTrait);

        return mentorService.getMentorsBySpecialization(specialization);
    }

    private String mapTraitToSpecialization(String topTrait) {

        switch (topTrait.toLowerCase()) {

            case "analytical":
                return "Data Analytics";

            case "technical":
                return "Software Development";

            case "communication":
                return "Product Management";

            case "leadership":
                return "Product Management";

            case "creative":
                return "Product Management";

            default:
                return topTrait;
        }
    }
}