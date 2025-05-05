package com.hcmute.thuexe.dto.response;

public class ConversationResponse {
    private Long conversationId;
    private Long receiverId;
    private String receiverName;
    private String receiverImage;
    private String lastMessageContent;
    private String lastMessageTimestamp;

    public ConversationResponse() {}

    public ConversationResponse(Long conversationId, Long receiverId, String receiverName, String receiverImage, String lastMessageContent, String lastMessageTimestamp) {
        this.conversationId = conversationId;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.receiverImage = receiverImage;
        this.lastMessageContent = lastMessageContent;
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverImage() {
        return receiverImage;
    }

    public void setReceiverImage(String receiverImage) {
        this.receiverImage = receiverImage;
    }

    public String getLastMessageContent() {
        return lastMessageContent;
    }

    public void setLastMessageContent(String lastMessageContent) {
        this.lastMessageContent = lastMessageContent;
    }

    public String getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(String lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }
}
