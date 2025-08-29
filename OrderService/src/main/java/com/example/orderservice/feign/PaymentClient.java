package com.example.orderservice.feign;

import com.example.orderservice.dto.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", url = "http://localhost:9092/payment")
public interface PaymentClient {
    @PostMapping("/process")
    String processPayment(@RequestBody PaymentRequest paymentRequest);
}