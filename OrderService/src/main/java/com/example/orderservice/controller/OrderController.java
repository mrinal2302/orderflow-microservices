package com.example.orderservice.controller;

import com.example.orderservice.dto.PaymentRequest;
import com.example.orderservice.entities.OrderEntity;
import com.example.orderservice.service.OrderServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderServiceImp orderServiceImp;

    @PostMapping("/orders")
    public ResponseEntity<String> savedOrder(@Valid @RequestBody OrderEntity orderEntity) {
        String orderEntity1 = orderServiceImp.savedOrder(orderEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderEntity1);
    }

    @GetMapping("/getAllOrder")
    public ResponseEntity<List<Map<String, Object>>> getAllTheOrders() {
        List<Map<String, Object>> order = orderServiceImp.getAllTheOrders();
        return ResponseEntity.status(HttpStatus.FOUND).body(order);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable long id) {
        Map<String, Object> order = orderServiceImp.getOrderById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(order);
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<String> payForOrder(@PathVariable Long orderId) {
        String paymentResponse = orderServiceImp.sendOrderForPayment(orderId);
        return ResponseEntity.ok(paymentResponse);
    }
/*
    @GetMapping("/{id}/payment-details")
    public PaymentRequest getOrderDetailsForPayment(@PathVariable long id) {
        return orderServiceImp.getOrderDetailsForPayment(id);
    }*/
}
