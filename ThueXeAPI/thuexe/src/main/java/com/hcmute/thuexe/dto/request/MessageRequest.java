package com.hcmute.thuexe.dto.request;

import com.hcmute.thuexe.model.enums.MessageType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageRequest {

    @NotNull(message = "ID người nhận không được để trống")
    private Long receiverId;

    @NotNull(message = "Loại tin nhắn không được để trống")
    private MessageType messageType;

    private String content;       // Chỉ dùng khi là TEXT
    private Long sharedPostId;    // Chỉ dùng khi là POST
    private String senderUsername;

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
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

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }
}
