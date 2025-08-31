package com.notification.service.Exception;

public class NotificationException extends RuntimeException {
    public NotificationException(String failedToSendEmail) {

        super(failedToSendEmail);
    }
}
