package com.hcmute.thuexe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcmute.thuexe.dto.request.MessageRequest;
import com.hcmute.thuexe.dto.response.ConversationResponse;
import com.hcmute.thuexe.dto.response.MessageResponse;
import com.hcmute.thuexe.dto.response.UserProfileResponse;
import com.hcmute.thuexe.dto.response.UserSearchResponse;
import com.hcmute.thuexe.service.ConversationService;
import com.hcmute.thuexe.service.MessageService;
import com.hcmute.thuexe.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * API: GET /api/user/profile
     * Lấy thông tin người dùng hiện tại (username từ token)
     */
    @GetMapping("/profile")
    public UserProfileResponse getProfile(Authentication authentication) {
        return userService.getProfile(authentication);
    }

    /**
     * API: GET /api/user/conversations
     * Lấy tất cả đoạn hội thoại của user đang login
     */
    @GetMapping("/conversations")
    public List<ConversationResponse> getAllConversations(Authentication authentication) {
        return conversationService.getAllConversationsForUser(authentication);
    }

    /**
     * API: POST /api/user/messages
     * Gửi tin nhắn mới (TEXT hoặc POST)
     */
    @PostMapping("/message")
    public MessageResponse sendMessage(@RequestBody MessageRequest request, Authentication authentication) {
        MessageResponse response = messageService.sendMessage(request, authentication);

        // ✅ Gửi realtime cho tất cả client đang subscribe hội thoại này
        messagingTemplate.convertAndSend("/topic/conversations/" + response.getConversationId(), response);

        return response;
    }

    /**
     * API: GET /api/user/messages/{conversationId}
     * Lấy danh sách tin nhắn trong một đoạn hội thoại
     */
    @GetMapping("/messages/{conversationId}")
    public List<MessageResponse> getMessagesByConversation(
            @PathVariable Long conversationId,
            Authentication authentication) {
        return messageService.getMessagesByConversation(conversationId, authentication);
    }

    /**
     * API: GET /api/user/search?keyword=...
     * Tìm kiếm người dùng theo tên hoặc email (loại trừ chính mình)
     */
    @GetMapping("/search")
    public List<UserSearchResponse> searchUsers(String keyword, Authentication authentication) {
        return userService.searchUsers(keyword, authentication);
    }
}
