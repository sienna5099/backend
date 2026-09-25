package com.aicareermentor.backend.service;

import com.aicareermentor.backend.dto.ProfileRequest;
import com.aicareermentor.backend.dto.ProfileResponse;
import com.aicareermentor.backend.entity.Profile;
import com.aicareermentor.backend.repository.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    // Create or update a user's profile
    public ProfileResponse saveProfile(ProfileRequest request) {

        if (request.getUserId() == null || request.getUserId() <= 0) {
            throw new IllegalArgumentException("Valid user ID is required");
        }

        // Check whether this user already has a profile
        Profile profile = profileRepository
                .findByUserId(request.getUserId())
                .orElse(new Profile());

        // Set/update profile information
        profile.setUserId(request.getUserId());
        profile.setQualification(request.getQualification().trim());
        profile.setField(request.getField().trim());
        profile.setGradYear(request.getGradYear());
        profile.setInterests(request.getInterests().trim());
        profile.setGoal(request.getGoal().trim());

        // Save to MySQL
        Profile savedProfile = profileRepository.save(profile);

        return convertToResponse(savedProfile);
    }

    // Get a user's profile
    public ProfileResponse getProfile(Long userId) {

        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Valid user ID is required");
        }

        Profile profile = profileRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("Profile not found for user ID: " + userId)
                );

        return convertToResponse(profile);
    }

    // Convert Entity → Response DTO
    private ProfileResponse convertToResponse(Profile profile) {

        return new ProfileResponse(
                profile.getId(),
                profile.getUserId(),
                profile.getQualification(),
                profile.getField(),
                profile.getGradYear(),
                profile.getInterests(),
                profile.getGoal()
        );
    }
}
