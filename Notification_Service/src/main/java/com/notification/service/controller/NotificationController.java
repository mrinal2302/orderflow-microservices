package com.notification.service.controller;

import com.notification.service.dto.NotificationRequest;
import com.notification.service.dto.NotificationResponse;
import com.notification.service.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class NotificationController {


    private NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/email")
    public ResponseEntity<NotificationResponse> notifySaveOrderStatus(@Valid @RequestBody NotificationRequest request){
      NotificationResponse response=  notificationService.notifyUserBasedOnOrderStatus(request.getRecipient(), request.getOrderId(),
              request.getStatus(), request.getProductId(), request.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }





}
