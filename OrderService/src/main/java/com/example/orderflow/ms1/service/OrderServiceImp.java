package com.example.orderflow.ms1.service;

import com.example.orderflow.ms1.entities.OrderEntity;
import com.example.orderflow.ms1.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImp implements OrderService {
    @Autowired
    OrderRepository orderRepository;


    @Override
    public String savedOrder(OrderEntity orderEntity) {
        orderRepository.save(orderEntity);
        return "Order Saved In The Cart";
    }

    @Override
    public List<Map<String, Object>> getAllTheOrders() {
        List<OrderEntity> orders = orderRepository.findAll();
        List<Map<String, Object>> response = new ArrayList<>();
        for (OrderEntity order : orders) {
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("OrderId", order.getOrderId());
            orderMap.put("status", order.getStatus());
            orderMap.put("message", order.getMessage());
            response.add(orderMap);
        }

        return response;
    }


}



