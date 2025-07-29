package com.notification.service.dto;

public class NotificationResponse {

    private String status;
    private String sentAt;
    private String messageId;

    public NotificationResponse(String status, String sentAt, String messageId) {
        this.status = status;
        this.sentAt = sentAt;
        this.messageId = messageId;
    }

    public String getStatus() {
        return status;
    }

    public String getSentAt() {
        return sentAt;
    }

    public String getMessageId() {
        return messageId;
    }
}
