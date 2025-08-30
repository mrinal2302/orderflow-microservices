package com.inventryService.feign;

import com.inventryService.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URL;

@FeignClient(name="NotificationService", url = "http://localhost:9093/notificationService")
public interface NotificationClient {

    @PostMapping("/sendingId")
    String sendById(@RequestBody NotificationRequest notificationRequest);


}
