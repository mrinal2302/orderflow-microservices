package com.notification.service.service;

import com.notification.service.dto.NotificationRequest;
import com.notification.service.dto.NotificationResponse;
import com.notification.service.entity.OrderNotification;
import com.notification.service.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class NotificationService {
    @Value("${notification.orderId}")
    private String defaultOrderId;
    @Autowired
    private NotificationRepository notificationRepository;


    public NotificationResponse createNotification(NotificationRequest request) {

        OrderNotification notification = new OrderNotification();
        notification.setOrderId(defaultOrderId);
        notification.setRecipientEmail(request.getRecipient());
        notification.setMessageId(request.getMessageId());
        notification.setStatus(request.getStatus());
        notification.setSentAt(LocalDateTime.now());
        notification.setMessage(request.getMessage());
        notificationRepository.save(notification);

        String sentAtStr = notification.getSentAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        return new NotificationResponse(
                notification.getStatus(),
                sentAtStr,
                notification.getMessageId()

        );


    }
}


