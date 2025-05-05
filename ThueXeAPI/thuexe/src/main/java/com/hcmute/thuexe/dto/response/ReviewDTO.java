package com.hcmute.thuexe.dto.response;

public class ReviewDTO {
    private int rating;
    private String comment;
    private Long userId;
    private String createdAt;

    public ReviewDTO(int rating, String comment, Long userId, String createdAt) {
        this.rating = rating;
        this.comment = comment;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
}
