package com.hcmute.thuexe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.hcmute.thuexe.dto.request.MessageRequest;
import com.hcmute.thuexe.dto.response.MessageResponse;
import com.hcmute.thuexe.service.MessageService;

@Controller
public class ChatWebSocketController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Nhận tin nhắn qua WebSocket, lưu DB và gửi lại cho các client trong hội thoại
     */
    @MessageMapping("/chat/{conversationId}") // client gửi vào đây: /app/chat/{id}
    public void sendMessage(@DestinationVariable Long conversationId, @Payload MessageRequest request) {
        // Giả định không có Authentication từ STOMP -> dùng tạm userId từ request nếu cần
        MessageResponse savedMessage = messageService.sendMessage(request, null); // null nếu chưa hỗ trợ auth ở đây

        // Gửi tin nhắn này đến tất cả client đang theo dõi cuộc hội thoại
        messagingTemplate.convertAndSend(
            "/topic/conversations/" + conversationId, 
            savedMessage
        );
    }
}
