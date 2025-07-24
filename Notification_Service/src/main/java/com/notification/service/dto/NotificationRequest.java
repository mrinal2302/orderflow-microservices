package com.notification.service.dto;

public class NotificationRequest {

    private String messageId;
    private String orderId;
    private String recipient;
    private String message;
    private String status;

    public NotificationRequest(String messageId, String orderId, String recipient, String message, String status) {
        this.messageId = messageId;
        this.orderId = orderId;
        this.recipient = recipient;
        this.message = message;
        this.status = status;
    }

    public NotificationRequest() {
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

