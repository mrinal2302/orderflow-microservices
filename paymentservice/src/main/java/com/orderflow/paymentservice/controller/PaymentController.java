package com.orderflow.paymentservice.controller;

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

    @PostMapping("/process")
    public ResponseEntity<String> create(@Valid @RequestBody PaymentEntity entity) {
        paymentService.save(entity);
        return ResponseEntity.ok("created");
    }


    @GetMapping("/getAllData")
    public ResponseEntity<List<PaymentEntity>> getAll() {
        return ResponseEntity.ok(paymentService.getAll());
    }

    @GetMapping("/getByOrderId/{orderId}")
    public ResponseEntity<PaymentEntity> getById(@PathVariable Long orderId) {
        PaymentEntity paymentProcess = paymentService.getByOrderId(orderId);
        return new ResponseEntity<>(paymentProcess, HttpStatus.FOUND);
    }


    @PutMapping("/updateByOrderId/{orderId}")
    public ResponseEntity<PaymentEntity> updateById(@RequestBody PaymentEntity entity, @PathVariable Long orderId) {
        PaymentEntity paymentUpdate = paymentService.updatePayment(entity, orderId);
        return new ResponseEntity<>(paymentUpdate, HttpStatus.FOUND);
    }

    @DeleteMapping("/deleteByPaymentId/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        paymentService.deleteById(id); // perform the delete action
        return ResponseEntity.ok("deleted data");
    }

}


