package com.aicareermentor.backend;

import com.aicareermentor.backend.entity.Career;
import com.aicareermentor.backend.entity.RoadmapTask;
import com.aicareermentor.backend.repository.CareerRepository;
import com.aicareermentor.backend.repository.RoadmapTaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.List;

@Configuration
public class RoadmapDataInitializer {

    @Bean
    @Order(2)
    CommandLineRunner initializeRoadmapTasks(
            CareerRepository careerRepository,
            RoadmapTaskRepository roadmapTaskRepository) {
        return args -> {
            if (roadmapTaskRepository.count() > 0) return;

            for (Career career : careerRepository.findAll()) {
                int order = 1;
                roadmapTaskRepository.save(new RoadmapTask(
                        career, 1, "Career Assessment",
                        "Understand your assessment signal and the direction it points toward.",
                        "Complete the 30-question career assessment",
                        "Use your existing assessment result to establish your starting point.", order++));
                roadmapTaskRepository.save(new RoadmapTask(
                        career, 2, "Current Level",
                        "Review the strengths and recommendation that shape your route.",
                        "Review your " + career.getTitle() + " recommendation",
                        "Understand why this career was recommended and what it asks of you.", order++));

                List<String> skills = Arrays.stream((career.getSkills() == null ? "" : career.getSkills()).split(","))
                        .map(String::trim)
                        .filter(skill -> !skill.isBlank())
                        .toList();
                int skillOrder = 1;
                for (String skill : skills) {
                    roadmapTaskRepository.save(new RoadmapTask(
                            career, 3, "Skill Development",
                            "Build the capabilities this career requires, one focused skill at a time.",
                            "Develop " + skill,
                            "Practice and apply " + skill + " until you can use it confidently.", skillOrder++));
                }
                roadmapTaskRepository.save(new RoadmapTask(
                        career, 4, "Projects",
                        "Turn your growing capability into evidence you can show.",
                        "Complete a portfolio project for " + career.getTitle(),
                        career.getResponsibilities(), 1));
                roadmapTaskRepository.save(new RoadmapTask(
                        career, 5, "Portfolio & Resume",
                        "Package your evidence so employers can understand your value.",
                        "Prepare your " + career.getTitle() + " portfolio and resume",
                        "Connect your project evidence to the responsibilities of this career.", 1));
                roadmapTaskRepository.save(new RoadmapTask(
                        career, 6, "Interview Preparation",
                        "Practice explaining your skills, decisions, and project impact.",
                        "Complete a " + career.getTitle() + " mock interview",
                        "Use the existing mock interview workflow to rehearse your next step.", 1));
                roadmapTaskRepository.save(new RoadmapTask(
                        career, 7, "Job Ready",
                        "Bring your evidence, story, and next action together.",
                        "Start applying to " + career.getTitle() + " roles",
                        "Review your completed roadmap and begin a focused application search.", 1));
            }
        };
    }
}
