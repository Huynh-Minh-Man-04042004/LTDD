package com.hcmute.ltdd.model.request;

public class UpdateStatusRequest {
    private Long bookingId;
    private String status;
    private String reason;

    public UpdateStatusRequest(Long bookingId, String status, String reason) {
        this.bookingId = bookingId;
        this.status = status;
        this.reason = reason;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public String getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }
}
