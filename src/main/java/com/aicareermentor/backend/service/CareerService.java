package com.aicareermentor.backend.service;

import com.aicareermentor.backend.entity.Career;
import com.aicareermentor.backend.exception.ResourceNotFoundException;
import com.aicareermentor.backend.repository.CareerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CareerService {

    private final CareerRepository careerRepository;

    public CareerService(CareerRepository careerRepository) {
        this.careerRepository = careerRepository;
    }

    public List<Career> getAllCareers() {
        return careerRepository.findAll();
    }

    public Career getCareerById(String careerId) {

        if (careerId == null || careerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Career ID is required");
        }

        return careerRepository.findByCareerId(careerId.trim())
            .orElseThrow(() ->
                new ResourceNotFoundException("Career not found: " + careerId)
            );
    }
}
