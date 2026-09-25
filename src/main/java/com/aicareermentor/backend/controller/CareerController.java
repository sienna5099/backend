package com.aicareermentor.backend.controller;

import com.aicareermentor.backend.entity.Career;
import com.aicareermentor.backend.service.CareerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/careers")
@CrossOrigin(origins = "http://localhost:5173")
public class CareerController {

    private final CareerService careerService;

    public CareerController(CareerService careerService) {
        this.careerService = careerService;
    }

    @GetMapping
    public ResponseEntity<List<Career>> getAllCareers() {

        List<Career> careers = careerService.getAllCareers();

        return ResponseEntity.ok(careers);
    }

    @GetMapping("/{careerId}")
    public ResponseEntity<Career> getCareerById(
            @PathVariable String careerId
    ) {

        Career career = careerService.getCareerById(careerId);

        return ResponseEntity.ok(career);
    }
}
