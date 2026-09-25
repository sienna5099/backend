package com.aicareermentor.backend.service;

import com.aicareermentor.backend.entity.AssessmentAnswer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AssessmentResultService {

    public Map<String, Integer> calculateTraitScores(
            List<AssessmentAnswer> answers) {

        Map<String, Integer> traitScores = new HashMap<>();

        for (AssessmentAnswer answer : answers) {

            if (answer.getTrait() == null || answer.getScore() == null) {
                continue;
            }

            traitScores.merge(
                    answer.getTrait(),
                    answer.getScore(),
                    Integer::sum
            );
        }

        return traitScores;
    }

    public String findTopTrait(Map<String, Integer> traitScores) {

        String topTrait = null;
        int highestScore = Integer.MIN_VALUE;

        for (Map.Entry<String, Integer> entry : traitScores.entrySet()) {

            if (entry.getValue() > highestScore) {
                highestScore = entry.getValue();
                topTrait = entry.getKey();
            }
        }

        return topTrait;
    }
}
