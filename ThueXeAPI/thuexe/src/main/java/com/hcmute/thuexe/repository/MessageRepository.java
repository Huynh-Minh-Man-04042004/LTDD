package com.hcmute.thuexe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmute.thuexe.model.Conversation;
import com.hcmute.thuexe.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationOrderByTimestampAsc(Conversation conversation);
    Optional<Message> findTopByConversationOrderByTimestampDesc(Conversation conversation);
}

