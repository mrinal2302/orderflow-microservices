package com.notification.service.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "OrderNotificationTable")

public class OrderNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long messageId;
    private String orderId;
    private String recipientEmail;
    private String message;
    private LocalDateTime sentAt;
    private String status;

    public OrderNotification() {
    }

    public OrderNotification(long messageId, String orderId, String recipient, String message, LocalDateTime sentAt, String status) {
        this.messageId = messageId;
        this.orderId = orderId;
        this.recipientEmail = recipientEmail;
        this.message = message;
        this.sentAt = sentAt;
        this.status = status;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
