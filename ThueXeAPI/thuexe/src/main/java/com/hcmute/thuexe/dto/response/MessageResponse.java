package com.hcmute.thuexe.dto.response;

import java.time.OffsetDateTime;

import com.hcmute.thuexe.model.enums.MessageStatus;
import com.hcmute.thuexe.model.enums.MessageType;

import lombok.Data;

@Data
public class MessageResponse {
    private Long messageId;
    private Long conversationId;
    private Long senderId;
    private Long receiverId;
    private String content;             // nếu là TEXT
    private Long sharedPostId;         // nếu là POST
    private MessageType messageType;
    private MessageStatus status;
    private OffsetDateTime timestamp;


    // Constructor đầy đủ
    public MessageResponse(){

    }
    
    public MessageResponse(Long messageId, Long conversationId, Long senderId, Long receiverId, String content, Long sharedPostId, MessageType messageType, MessageStatus status, OffsetDateTime timestamp) {
        this.messageId = messageId;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.sharedPostId = sharedPostId;
        this.messageType = messageType;
        this.status = status;
        this.timestamp = timestamp;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSharedPostId() {
        return sharedPostId;
    }

    public void setSharedPostId(Long sharedPostId) {
        this.sharedPostId = sharedPostId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

}
