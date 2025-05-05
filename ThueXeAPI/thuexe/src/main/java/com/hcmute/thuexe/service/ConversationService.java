package com.hcmute.thuexe.service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.hcmute.thuexe.dto.response.ConversationResponse;
import com.hcmute.thuexe.exception.ResourceNotFoundException;
import com.hcmute.thuexe.model.Conversation;
import com.hcmute.thuexe.model.Message;
import com.hcmute.thuexe.model.User;
import com.hcmute.thuexe.repository.ConversationRepository;
import com.hcmute.thuexe.repository.MessageRepository;
import com.hcmute.thuexe.repository.UserRepository;

@Service
public class ConversationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    public List<ConversationResponse> getAllConversationsForUser(Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userRepository.findByAccount_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        List<Conversation> conversations = conversationRepository.findAllByUser(currentUser);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return conversations.stream()
                .map(c -> {
                    User receiver = c.getUser1().equals(currentUser) ? c.getUser2() : c.getUser1();

                    Message lastMessage = messageRepository.findTopByConversationOrderByTimestampDesc(c).orElse(null);

                    String lastContent = lastMessage != null ? lastMessage.getContent() : "";
                    String lastTime = lastMessage != null ? lastMessage.getTimestamp().atZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime().format(formatter): "";

                    return new ConversationResponse(
                            c.getConversationId(),
                            receiver.getUserId(),
                            receiver.getName(),
                            receiver.getImageUrl(),
                            lastContent,
                            lastTime
                    );
                })
                .collect(Collectors.toList());
    }
}
