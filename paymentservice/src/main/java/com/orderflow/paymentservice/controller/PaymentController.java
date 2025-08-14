package com.orderflow.paymentservice.controller;

import com.orderflow.paymentservice.dto.PaymentNotifyStatus;
import com.orderflow.paymentservice.entity.PaymentEntity;
import com.orderflow.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/savePaymentData")
    public ResponseEntity<String> savePaymentData(@Valid @RequestBody PaymentEntity entity) {
        paymentService.savePaymentData(entity);
        return ResponseEntity.ok("Payment data saved successfully");
    }


    @GetMapping("/getAllData")
    public ResponseEntity<List<PaymentEntity>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPaymentDetails());
    }

    @PutMapping("/updateByOrderId/{orderId}")
    public ResponseEntity<PaymentEntity> updateByOrderId(@RequestBody PaymentEntity entity, @PathVariable Long orderId) {
        PaymentEntity paymentUpdate = paymentService.updatePaymentDetails(entity, orderId);
        return new ResponseEntity<>(paymentUpdate, HttpStatus.FOUND);
    }

    @DeleteMapping("/deleteByPaymentId/{PaymentId}")
    public ResponseEntity<String> deleteOrderDetails(@PathVariable Long PaymentId) {
        paymentService.deleteByOrderId(PaymentId);
        return ResponseEntity.ok("deleted data");
    }
    

    @GetMapping("/getPaymentByOrderId/{orderId}")
    public ResponseEntity<PaymentEntity> getPaymentByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }
    @GetMapping("/notify/{orderId}")
    public ResponseEntity<PaymentNotifyStatus> sendNotification(@PathVariable Long orderId) {

        return ResponseEntity.ok(paymentService.sendNotify(orderId));
    }

}
