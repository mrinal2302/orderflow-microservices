package com.orderflow.paymentservice.client;

import com.orderflow.paymentservice.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificationservice", url = "${NOTIFICATION_SERVICE_URL:http://localhost:8083}")
public interface NotificationServiceClient {

    @PostMapping("/notification/send")
    void sendNotification(@RequestBody NotificationRequest notificationRequest);
}
