package com.notification.service.service;

import com.notification.service.entity.OrderNotification;
import com.notification.service.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;


    public OrderNotification saveConfirmation(OrderNotification notification) {
        return notificationRepository.save(notification);
    }
}



