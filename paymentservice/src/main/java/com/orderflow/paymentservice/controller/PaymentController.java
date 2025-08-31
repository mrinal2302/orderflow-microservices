package com.orderflow.paymentservice.controller;

import com.orderflow.paymentservice.dto.OrderResponse;
import com.orderflow.paymentservice.entity.PaymentEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.orderflow.paymentservice.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<String> processPayment(@Valid @RequestBody OrderResponse orderResponse) {
        return ResponseEntity.ok(paymentService.processPaymentFromOrder(orderResponse));
    }

    @GetMapping("/getAllData")
    public ResponseEntity<List<PaymentEntity>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPaymentDetails());
    }

    @PutMapping("/updateByOrderId/{orderId}")
    public ResponseEntity<PaymentEntity> updateByOrderId(@Valid @RequestBody PaymentEntity entity, @PathVariable Long orderId) {
        PaymentEntity paymentUpdate = paymentService.updatePaymentDetails(entity, orderId);
        return new ResponseEntity<>(paymentUpdate, HttpStatus.OK);
    }
    @GetMapping("/getPaymentByOrderId/{orderId}")
    public ResponseEntity<PaymentEntity> getPaymentByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }


}
