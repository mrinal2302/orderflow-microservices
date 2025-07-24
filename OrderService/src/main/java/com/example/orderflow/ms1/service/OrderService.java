package com.example.orderflow.ms1.service;

import com.example.orderflow.ms1.entities.OrderEntity;

import java.util.List;
import java.util.Map;

public interface OrderService {

    String savedOrder(OrderEntity orderEntity);


    List<Map<String, Object>> getAllTheOrders();
}
