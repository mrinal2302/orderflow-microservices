package com.notification.service.controller;

import com.notification.service.entity.OrderNotification;
import com.notification.service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/notify")
    public OrderNotification saveConfirmation(@RequestBody OrderNotification notification) {

        return notificationService.saveConfirmation(notification);
    }

}
