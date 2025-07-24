package com.example.orderflow.ms1.controller;

import com.example.orderflow.ms1.entities.OrderEntity;
import com.example.orderflow.ms1.entities.OrderResponse;
import com.example.orderflow.ms1.service.OrderServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/requestorder")
public class OrderContorller {

    @Autowired
    OrderServiceImp orderServiceImp;


    @PostMapping("/orders")
    public ResponseEntity<String> savedOrder(@Valid @RequestBody OrderEntity orderEntity) {
        String orderEntity1 = orderServiceImp.savedOrder(orderEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderEntity1);
    }

    @GetMapping("/getAll")
    public List<Map<String, Object>> getAllTheOrders() {
        List<Map<String, Object>> orderResponse = orderServiceImp.getAllTheOrders();
        System.out.println("hello");
        return ResponseEntity.status(HttpStatus.OK).body(orderResponse).getBody();
    }

}
