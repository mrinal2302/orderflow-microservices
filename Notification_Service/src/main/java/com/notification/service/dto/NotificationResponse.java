package com.notification.service.dto;

import java.time.LocalDateTime;

public class NotificationResponse {

    private long messageID;
    private String status;
    private LocalDateTime sentAt;

    public NotificationResponse(long messageID) {
        this.messageID = messageID;
        this.status = status;
        this.sentAt = LocalDateTime.now();
    }

    public long getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
