package com.hcmute.thuexe.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.hcmute.thuexe.dto.request.MessageRequest;
import com.hcmute.thuexe.dto.response.MessageResponse;
import com.hcmute.thuexe.exception.ResourceNotFoundException;
import com.hcmute.thuexe.model.Conversation;
import com.hcmute.thuexe.model.Message;
import com.hcmute.thuexe.model.User;
import com.hcmute.thuexe.model.enums.MessageStatus;
import com.hcmute.thuexe.repository.ConversationRepository;
import com.hcmute.thuexe.repository.MessageRepository;
import com.hcmute.thuexe.repository.UserRepository;

@Service
public class MessageService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    public MessageResponse sendMessage(MessageRequest request, Authentication authentication) {
        String username;
    
        // 1. Ưu tiên lấy từ token nếu có (REST API)
        if (authentication != null) {
            username = authentication.getName();
        }
        // 2. Nếu không có (WebSocket), lấy từ request
        else if (request.getSenderUsername() != null) {
            username = request.getSenderUsername(); // thêm field này vào DTO
        } else {
            throw new IllegalArgumentException("Không xác định được người gửi.");
        }
    
        User sender = userRepository.findByAccount_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("Người gửi không tồn tại"));
    
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new ResourceNotFoundException("Người nhận không tồn tại"));
    
        if (sender.getUserId().equals(receiver.getUserId())) {
            throw new IllegalArgumentException("Không thể gửi tin nhắn cho chính mình.");
        }
    
        Conversation conversation = conversationRepository
                .findByUser1AndUser2OrUser2AndUser1(sender, receiver, sender, receiver)
                .orElseGet(() -> {
                    Conversation newConversation = new Conversation();
                    newConversation.setUser1(sender);
                    newConversation.setUser2(receiver);
                    return conversationRepository.save(newConversation);
                });
                
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(request.getContent());
        message.setMessageType(request.getMessageType());
        message.setSharedPostId(request.getSharedPostId());
        message.setStatus(MessageStatus.SENDING);
    
        Message saved = messageRepository.save(message);
    
        return new MessageResponse(
                saved.getMessageId(),
                conversation.getConversationId(),
                sender.getUserId(),
                receiver.getUserId(),
                saved.getContent(),
                saved.getSharedPostId(),
                saved.getMessageType(),
                saved.getStatus(),
                saved.getTimestamp()
        );
    }    

    public List<MessageResponse> getMessagesByConversation(Long conversationId, Authentication authentication) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuộc hội thoại"));

        List<Message> messages = messageRepository.findByConversationOrderByTimestampAsc(conversation);

        return messages.stream().map(msg -> new MessageResponse(
                msg.getMessageId(),
                conversationId,
                msg.getSender().getUserId(),
                msg.getReceiver().getUserId(),
                msg.getContent(),
                msg.getSharedPostId(),
                msg.getMessageType(),
                msg.getStatus(),
                msg.getTimestamp()
        )).collect(Collectors.toList());
    }
}
