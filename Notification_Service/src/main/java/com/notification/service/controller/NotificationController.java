package com.notification.service.controller;

import com.notification.service.dto.NotificationRequest;
import com.notification.service.dto.PaymentResponse;
import com.notification.service.service.NotificationService;
import jakarta.validation.Valid;
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
    public ResponseEntity<PaymentResponse> notifyAndSaveOrderStatus(@Valid @RequestBody NotificationRequest request){
      PaymentResponse response=  notificationService.notifyUserBasedOnOrderStatus(request.getRecipient(), request.getOrderId(),
              request.getStatus(), request.getProductId(), request.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/check-stock/{productId}")
    public  ResponseEntity<String> checkTheStockAvailability (@PathVariable Long productId ){
       String result =   notificationService.getStockAvailability(productId);

      return ResponseEntity.ok(result);

}
}
