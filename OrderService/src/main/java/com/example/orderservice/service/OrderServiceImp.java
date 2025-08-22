package com.example.orderservice.service;

import com.example.orderservice.dto.PaymentRequest;
import com.example.orderservice.entities.OrderEntity;
import com.example.orderservice.exceptionhandler.OrderIdNotFoundException;
import com.example.orderservice.feign.PaymentClient;
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
    @Autowired
    private PaymentClient paymentClient;

    /*@Override
    public String savedOrder(OrderEntity orderEntity) {

        OrderEntity savedOrder = orderRepository.save(orderEntity);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId(savedOrder.getOrderId());
        paymentRequest.setProductId(savedOrder.getProductId());
        paymentRequest.setProductName(savedOrder.getProductName());
        paymentRequest.setQuantity(savedOrder.getQuantity());
        paymentRequest.setStatus(savedOrder.getStatus());
        paymentRequest.setPaymentMode(savedOrder.getPaymentMode());

        String paymentResponse = paymentClient.processPayment(paymentRequest);

        return "Order Saved In The Cart. " + paymentResponse;
    }*/
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
                .orElseThrow(() -> new OrderIdNotFoundException("OrderID " + id + " is Invalid"));

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", order.getOrderId());
        map.put("quantity", order.getQuantity());
        map.put("status", order.getStatus());
        map.put("email", order.getEmail());
        map.put("message", order.getMessage());
        return map;
    }

    @Override
    public String sendOrderForPayment(Long orderId) {

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderIdNotFoundException("OrderID " + orderId + " is Invalid"));

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId(order.getOrderId());
        paymentRequest.setProductId(order.getProductId());
        paymentRequest.setProductName(order.getProductName());
        paymentRequest.setQuantity(order.getQuantity());
        paymentRequest.setStatus(order.getStatus());
        paymentRequest.setPaymentMode(order.getPaymentMode());

        return paymentClient.processPayment(paymentRequest);
    }

   /* public PaymentRequest getOrderDetailsForPayment(long orderId) {

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderIdNotFoundException("OrderID " + orderId + " is Invalid"));

        PaymentRequest paymentRequest = new PaymentRequest();

        paymentRequest.setOrderId(order.getOrderId());
        paymentRequest.setProductId(order.getProductId());
        paymentRequest.setProductName(order.getProductName());
        paymentRequest.setQuantity(order.getQuantity());
        paymentRequest.setStatus(order.getStatus());
        paymentRequest.setPaymentMode(order.getPaymentMode());

        return paymentRequest;
    }*/
}