package com.aicareermentor.backend.repository;

import com.aicareermentor.backend.entity.MentorChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentorChatMessageRepository extends JpaRepository<MentorChatMessage, Long> {

    List<MentorChatMessage> findByUserIdAndMentorIdOrderByCreatedAtAsc(
            Long userId,
            Long mentorId);
}
