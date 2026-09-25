package com.aicareermentor.backend.service;

import com.aicareermentor.backend.dto.SkillGapAnalysisResponse;
import com.aicareermentor.backend.dto.SkillGapAnalyzeRequest;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.JacksonException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GroqSkillGapService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${groq.api.url}")
    private String groqApiUrl;

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.model}")
    private String groqModel;

    public GroqSkillGapService(ObjectMapper objectMapper) {
        this.restClient = RestClient.builder().build();
        this.objectMapper = objectMapper;
    }

    public SkillGapAnalysisResponse analyze(
            String careerName,
            List<String> requiredSkills,
            SkillGapAnalyzeRequest request) {

        if (groqApiKey == null || groqApiKey.isBlank()) {
            throw new IllegalStateException(
                    "GROQ_API_KEY is not configured. Check your .env file."
            );
        }

        String userSkillsText = buildUserSkillsText(request.skills());

        String requiredSkillsText =
                String.join(", ", requiredSkills);

        String resumeContext = request.resumeContext();

        if (resumeContext == null || resumeContext.isBlank()) {
            resumeContext =
                    "No additional resume or profile information was provided.";
        }

        String systemPrompt = """
                You are an AI Career Mentor performing a personalized
                Skill Gap Analysis.

                Analyze the user's current skills against the skills
                required for their selected career.

                IMPORTANT RULES:

                1. Analyze the selected career only.
                2. Current skill levels are percentages from 0 to 100.
                3. Required levels should represent a realistic proficiency
                   needed for the selected career.
                4. Calculate gap as requiredLevel - currentLevel.
                5. Never return a negative gap.
                6. Use only HIGH, MEDIUM, or LOW as priority.
                7. Give practical learning recommendations.
                8. Recommendations should be suitable for a student or
                   early-career developer.
                9. Do not invent user experience, projects, certifications,
                   education, or achievements.
                10. Use the resume/profile context only as supporting
                    information.
                11. Return one skill analysis for every required career skill.
                12. Return only valid structured JSON.
                """;

        String userPrompt = """
                Perform a personalized skill gap analysis.

                TARGET CAREER:
                %s

                REQUIRED CAREER SKILLS:
                %s

                USER'S CURRENT SKILL LEVELS:
                %s

                RESUME / PROFILE CONTEXT:
                %s

                For every required skill, provide:

                - skillName
                - currentLevel
                - requiredLevel
                - gap
                - priority
                - reason
                - recommendations

                Give 3 to 5 practical recommendations for each skill.

                Recommendations can include:
                - topics to study
                - practical exercises
                - projects to build
                - concepts to practice
                - certifications when genuinely relevant

                Do not invent course URLs.

                Also provide:
                - career
                - summary
                - overallReadiness
                - nextSteps
                """.formatted(
                careerName,
                requiredSkillsText,
                userSkillsText,
                resumeContext
        );

        Map<String, Object> requestBody = new HashMap<>();

        requestBody.put("model", groqModel);

        requestBody.put(
                "messages",
                List.of(
                        Map.of(
                                "role",
                                "system",
                                "content",
                                systemPrompt
                        ),
                        Map.of(
                                "role",
                                "user",
                                "content",
                                userPrompt
                        )
                )
        );

        requestBody.put("temperature", 0.2);

        requestBody.put(
                "max_completion_tokens",
                3000
        );

        requestBody.put(
                "response_format",
                buildResponseFormat()
        );

        try {

            JsonNode response = restClient
                    .post()
                    .uri(groqApiUrl)
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            "Bearer " + groqApiKey
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(JsonNode.class);

            if (response == null) {
                throw new IllegalStateException(
                        "Groq returned an empty response."
                );
            }

            JsonNode contentNode = response
                    .path("choices")
                    .path(0)
                    .path("message")
                    .path("content");

            if (contentNode.isMissingNode()
                    || contentNode.isNull()) {

                throw new IllegalStateException(
                        "Groq response did not contain analysis content."
                );
            }

            String content = contentNode.asText();

            if (content == null || content.isBlank()) {
                throw new IllegalStateException(
                        "Groq returned empty analysis content."
                );
            }

            return objectMapper.readValue(
                    content,
                    SkillGapAnalysisResponse.class
            );

        } catch (RestClientResponseException e) {

            throw new IllegalStateException(
                    "Groq API request failed. HTTP status: "
                            + e.getStatusCode()
                            + ". Response: "
                            + e.getResponseBodyAsString(),
                    e
            );

        } catch (JacksonException e) {

            throw new IllegalStateException(
                    "Could not parse the structured response returned by Groq.",
                    e
            );
        }
    }

    private String buildUserSkillsText(
            List<SkillGapAnalyzeRequest.SkillInput> skills) {

        if (skills == null || skills.isEmpty()) {
            return "No current skill levels were provided.";
        }

        List<String> result = new ArrayList<>();

        for (SkillGapAnalyzeRequest.SkillInput skill : skills) {

            result.add(
                    skill.name()
                            + ": "
                            + skill.value()
                            + "%"
            );
        }

        return String.join(", ", result);
    }

    private Map<String, Object> buildResponseFormat() {

        Map<String, Object> skillProperties =
                new HashMap<>();

        skillProperties.put(
                "skillName",
                Map.of("type", "string")
        );

        skillProperties.put(
                "currentLevel",
                Map.of("type", "integer")
        );

        skillProperties.put(
                "requiredLevel",
                Map.of("type", "integer")
        );

        skillProperties.put(
                "gap",
                Map.of("type", "integer")
        );

        skillProperties.put(
                "priority",
                Map.of(
                        "type",
                        "string",
                        "enum",
                        List.of(
                                "HIGH",
                                "MEDIUM",
                                "LOW"
                        )
                )
        );

        skillProperties.put(
                "reason",
                Map.of("type", "string")
        );

        skillProperties.put(
                "recommendations",
                Map.of(
                        "type",
                        "array",
                        "items",
                        Map.of("type", "string")
                )
        );

        Map<String, Object> skillSchema =
                new HashMap<>();

        skillSchema.put(
                "type",
                "object"
        );

        skillSchema.put(
                "additionalProperties",
                false
        );

        skillSchema.put(
                "properties",
                skillProperties
        );

        skillSchema.put(
                "required",
                List.of(
                        "skillName",
                        "currentLevel",
                        "requiredLevel",
                        "gap",
                        "priority",
                        "reason",
                        "recommendations"
                )
        );

        Map<String, Object> rootProperties =
                new HashMap<>();

        rootProperties.put(
                "career",
                Map.of("type", "string")
        );

        rootProperties.put(
                "summary",
                Map.of("type", "string")
        );

        rootProperties.put(
                "overallReadiness",
                Map.of("type", "integer")
        );

        rootProperties.put(
                "skills",
                Map.of(
                        "type",
                        "array",
                        "items",
                        skillSchema
                )
        );

        rootProperties.put(
                "nextSteps",
                Map.of(
                        "type",
                        "array",
                        "items",
                        Map.of("type", "string")
                )
        );

        Map<String, Object> rootSchema =
                new HashMap<>();

        rootSchema.put(
                "type",
                "object"
        );

        rootSchema.put(
                "additionalProperties",
                false
        );

        rootSchema.put(
                "properties",
                rootProperties
        );

        rootSchema.put(
                "required",
                List.of(
                        "career",
                        "summary",
                        "overallReadiness",
                        "skills",
                        "nextSteps"
                )
        );

        Map<String, Object> jsonSchema =
                new HashMap<>();

        jsonSchema.put(
                "name",
                "skill_gap_analysis"
        );

        jsonSchema.put(
                "strict",
                true
        );

        jsonSchema.put(
                "schema",
                rootSchema
        );

        return Map.of(
                "type",
                "json_schema",
                "json_schema",
                jsonSchema
        );
    }
}