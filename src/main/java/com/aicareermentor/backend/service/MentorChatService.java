package com.aicareermentor.backend.service;

import com.aicareermentor.backend.dto.MentorChatRequest;
import com.aicareermentor.backend.dto.MentorChatResponse;
import com.aicareermentor.backend.entity.Assessment;
import com.aicareermentor.backend.entity.Mentor;
import com.aicareermentor.backend.entity.MentorChatMessage;
import com.aicareermentor.backend.entity.Profile;
import com.aicareermentor.backend.exception.ResourceNotFoundException;
import com.aicareermentor.backend.repository.AssessmentRepository;
import com.aicareermentor.backend.repository.MentorChatMessageRepository;
import com.aicareermentor.backend.repository.MentorRepository;
import com.aicareermentor.backend.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MentorChatService {

    private final RestClient restClient = RestClient.builder().build();
    private final MentorRepository mentorRepository;
    private final ProfileRepository profileRepository;
    private final AssessmentRepository assessmentRepository;
    private final MentorChatMessageRepository messageRepository;

    @Value("${groq.api.url}")
    private String groqApiUrl;

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.model}")
    private String groqModel;

    public MentorChatService(
            MentorRepository mentorRepository,
            ProfileRepository profileRepository,
            AssessmentRepository assessmentRepository,
            MentorChatMessageRepository messageRepository) {
        this.mentorRepository = mentorRepository;
        this.profileRepository = profileRepository;
        this.assessmentRepository = assessmentRepository;
        this.messageRepository = messageRepository;
    }

    public List<MentorChatResponse> getHistory(Long userId, Long mentorId) {
        validateIds(userId, mentorId);
        return messageRepository.findByUserIdAndMentorIdOrderByCreatedAtAsc(userId, mentorId)
                .stream()
                .map(message -> new MentorChatResponse(
                        message.getId(), message.getRole(), message.getContent()))
                .toList();
    }

    public MentorChatResponse sendMessage(MentorChatRequest request) {
        validateIds(request.userId(), request.mentorId());
        Mentor mentor = mentorRepository.findById(request.mentorId())
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found: " + request.mentorId()));

        MentorChatMessage userMessage = saveMessage(
                request.userId(), request.mentorId(), "user", request.message().trim());
        List<MentorChatMessage> history = messageRepository
                .findByUserIdAndMentorIdOrderByCreatedAtAsc(request.userId(), request.mentorId());

        try {
            String reply = requestGroqReply(request.userId(), mentor, history);
            MentorChatMessage assistantMessage = saveMessage(
                    request.userId(), request.mentorId(), "assistant", reply);
            return new MentorChatResponse(
                    assistantMessage.getId(), assistantMessage.getRole(), assistantMessage.getContent());
        } catch (RuntimeException exception) {
            messageRepository.delete(userMessage);
            throw exception;
        }
    }

    private String requestGroqReply(
            Long userId,
            Mentor mentor,
            List<MentorChatMessage> history) {
        if (groqApiKey == null || groqApiKey.isBlank()) {
            throw new IllegalStateException("GROQ_API_KEY is not configured.");
        }

        String profileContext = profileRepository.findByUserId(userId)
                .map(this::profileContext)
                .orElse("No career profile has been provided yet.");
        String assessmentContext = assessmentRepository.findByUserId(userId)
                .map(this::assessmentContext)
                .orElse("No assessment has been completed yet.");

        String systemPrompt = "You are a supportive AI career mentor specializing in "
                + mentor.getSpecialization() + ". Give practical, honest, concise advice. "
                + "Use the user's context when relevant, do not invent achievements, and ask "
                + "one useful follow-up question when more information is needed.\n\n"
                + "USER PROFILE:\n" + profileContext + "\n\n"
                + "ASSESSMENT:\n" + assessmentContext;

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", systemPrompt));
        history.stream()
                .skip(Math.max(0, history.size() - 12L))
                .forEach(message -> messages.add(Map.of(
                        "role", "assistant".equals(message.getRole()) ? "assistant" : "user",
                        "content", message.getContent())));

        Map<String, Object> body = new HashMap<>();
        body.put("model", groqModel);
        body.put("messages", messages);
        body.put("temperature", 0.4);
        body.put("max_completion_tokens", 600);

        try {
            JsonNode response = restClient.post()
                    .uri(groqApiUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + groqApiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(JsonNode.class);
            String content = response == null
                    ? ""
                    : response.path("choices").path(0).path("message").path("content").asString();
            if (content == null || content.isBlank()) {
                throw new IllegalStateException("Groq returned an empty mentor response.");
            }
            return content.trim();
        } catch (RestClientResponseException exception) {
            throw new IllegalStateException(
                    "Groq mentor request failed. HTTP status: " + exception.getStatusCode(), exception);
        } catch (JacksonException exception) {
            throw new IllegalStateException("Could not parse the Groq mentor response.", exception);
        }
    }

    private MentorChatMessage saveMessage(Long userId, Long mentorId, String role, String content) {
        MentorChatMessage message = new MentorChatMessage();
        message.setUserId(userId);
        message.setMentorId(mentorId);
        message.setRole(role);
        message.setContent(content);
        return messageRepository.save(message);
    }

    private String profileContext(Profile profile) {
        return "Qualification: " + profile.getQualification()
                + "; field: " + profile.getField()
                + "; interests: " + profile.getInterests()
                + "; goal: " + profile.getGoal();
    }

    private String assessmentContext(Assessment assessment) {
        return "Top trait: " + assessment.getTopTrait()
                + "; analytical: " + assessment.getAnalyticalScore()
                + "; technical: " + assessment.getTechnicalScore()
                + "; communication: " + assessment.getCommunicationScore()
                + "; leadership: " + assessment.getLeadershipScore()
                + "; creative: " + assessment.getCreativeScore();
    }

    private void validateIds(Long userId, Long mentorId) {
        if (userId == null || userId <= 0 || mentorId == null || mentorId <= 0) {
            throw new IllegalArgumentException("Valid user and mentor IDs are required.");
        }
    }
}
