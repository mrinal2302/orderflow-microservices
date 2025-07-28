package com.example.orderservice.service;

import com.example.orderservice.entities.OrderEntity;
import com.example.orderservice.exceptionhandler.IdNotFoundException;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


        return orderRepository.findAll().stream().map(order -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("orderId", order.getOrderId());
                    map.put("quantity", order.getQuantity());
                    map.put("status", order.getStatus());
                    map.put("email", order.getEmail());
                    map.put("message", order.getMessage());
                    return map;
                })
                .collect(Collectors.toList());
    }


    public Map<String, Object> getOrderById(long id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("OrderID " + id + " is Invalid"));

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", order.getOrderId());
        map.put("quantity", order.getQuantity());
        map.put("status", order.getStatus());
        map.put("email", order.getEmail());
        map.put("message", order.getMessage());
        return map;
    }

}





