package com.aicareermentor.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.aicareermentor.backend.entity.Mentor;
import com.aicareermentor.backend.repository.MentorRepository;

@Service
public class MentorService {

    private final MentorRepository mentorRepository;

    public MentorService(MentorRepository mentorRepository) {
        this.mentorRepository = mentorRepository;
    }

    public Mentor saveMentor(Mentor mentor) {
        return mentorRepository.save(mentor);
    }

    public List<Mentor> getAllMentors() {
        return mentorRepository.findAll();
    }

    public List<Mentor> getAvailableMentors() {
        return mentorRepository.findByAvailableTrue();
    }

    public Optional<Mentor> getMentorById(Long id) {
        return mentorRepository.findById(id);
    }

    public List<Mentor> getMentorsBySpecialization(String specialization) {
        return mentorRepository.findBySpecializationIgnoreCase(specialization);
    }

    public void deleteMentor(Long id) {
        mentorRepository.deleteById(id);
    }
}
