package com.example.orderservice.service;

import com.example.orderservice.dto.PaymentRequest;
import com.example.orderservice.entities.OrderEntity;

import java.util.List;
import java.util.Map;

public interface OrderService {
    String savedOrder(OrderEntity orderEntity);
    List<Map<String, Object>> getAllTheOrders();
    Map<String, Object> getOrderById(long id);

    //public PaymentRequest getOrderDetailsForPayment(long orderId);
    String sendOrderForPayment(Long orderId);

}