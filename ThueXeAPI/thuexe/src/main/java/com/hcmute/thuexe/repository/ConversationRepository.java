package com.hcmute.thuexe.repository;

import com.hcmute.thuexe.model.Conversation;
import com.hcmute.thuexe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("SELECT c FROM Conversation c WHERE (c.user1 = :user1 AND c.user2 = :user2) OR (c.user1 = :user2 AND c.user2 = :user1)")
    Optional<Conversation> findByUser1AndUser2OrUser2AndUser1(User user1, User user2, User user2b, User user1b);

    @Query("SELECT c FROM Conversation c WHERE c.user1 = :user OR c.user2 = :user")
    List<Conversation> findAllByUser(User user);
}
