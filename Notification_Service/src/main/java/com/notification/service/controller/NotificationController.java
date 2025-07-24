package com.notification.service.controller;

import com.notification.service.dto.NotificationRequest;
import com.notification.service.dto.NotificationResponse;
import com.notification.service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/notify/email")
    public ResponseEntity<NotificationResponse> sendNotification(@RequestBody NotificationRequest request) {

        NotificationResponse response = notificationService.createNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
