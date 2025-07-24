package com.orderflow.paymentservice.controller;

import com.orderflow.paymentservice.entity.PaymentEntity;
import com.orderflow.paymentservice.service.PaymentService;
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
    public ResponseEntity<PaymentEntity> create(@RequestBody PaymentEntity entity) {
        PaymentEntity paymentProcess = paymentService.save(entity);
        return new ResponseEntity<>(paymentProcess, HttpStatus.CREATED);
    }

    @GetMapping("/getById/{paymentId}")
    public ResponseEntity<PaymentEntity> getById(@PathVariable Long paymentId) {
        PaymentEntity paymentProcess = paymentService.getByOrderId(paymentId);
        return new ResponseEntity<>(paymentProcess, HttpStatus.FOUND);
    }

    @GetMapping("/getAllData")
    public ResponseEntity<List<PaymentEntity>> getAll() {
        return ResponseEntity.ok(paymentService.getAll());
    }

    @GetMapping("/getByOrderid/{orderId}")
    public ResponseEntity<PaymentEntity> getByOrderId(@PathVariable Long orderId) {
        PaymentEntity paymentProcess = paymentService.getByOrderId(orderId);
        return new ResponseEntity<>(paymentProcess, HttpStatus.FOUND);
    }

    @PutMapping("/updateById/{orderId}")
    public ResponseEntity<PaymentEntity> updateById(@RequestBody PaymentEntity entity, @PathVariable Long orderId) {
        PaymentEntity paymentUpdate = paymentService.updatePayment(entity, orderId);
        if (paymentUpdate != null) {
            return new ResponseEntity<>(paymentUpdate, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        PaymentEntity payment = paymentService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
