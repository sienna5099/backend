package com.aicareermentor.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aicareermentor.backend.entity.Mentor;
import com.aicareermentor.backend.service.MentorService;

@RestController
@RequestMapping("/api/mentors")
@CrossOrigin(origins = "http://localhost:5173")
public class MentorController {

    private final MentorService mentorService;

    public MentorController(MentorService mentorService) {
        this.mentorService = mentorService;
    }

    @PostMapping
    public ResponseEntity<Mentor> createMentor(
            @RequestBody Mentor mentor) {

        return ResponseEntity.ok(
                mentorService.saveMentor(mentor)
        );
    }

    @GetMapping
    public ResponseEntity<List<Mentor>> getAllMentors() {

        return ResponseEntity.ok(
                mentorService.getAllMentors()
        );
    }

    @GetMapping("/available")
    public ResponseEntity<List<Mentor>> getAvailableMentors() {

        return ResponseEntity.ok(
                mentorService.getAvailableMentors()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mentor> getMentorById(
            @PathVariable Long id) {

        return mentorService.getMentorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<Mentor>> getMentorsBySpecialization(
            @PathVariable String specialization) {

        return ResponseEntity.ok(
                mentorService.getMentorsBySpecialization(specialization)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMentor(
            @PathVariable Long id) {

        mentorService.deleteMentor(id);

        return ResponseEntity.noContent().build();
    }
}
