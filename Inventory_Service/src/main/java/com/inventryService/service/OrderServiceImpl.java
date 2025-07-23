package com.inventryService.service;

import com.inventryService.entity.OrderEntity;
import com.inventryService.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements  OrderService{
@Autowired
    private OrderRepository orderRepository;

    @Override
    public OrderEntity getValueSaveIn(OrderEntity entity) {
        return orderRepository.save(entity);
    }

    @Override
    public OrderEntity getOrderById(Long id) {
        return orderRepository.findById(id).get();
    }

    @Override
    public List<OrderEntity> getAllOrderId() {
        return orderRepository.findAll();
    }

    @Override
    public OrderEntity updateOrderById(Long id, OrderEntity entity) {
    OrderEntity orderEntity = orderRepository.findById(id).get();
    orderEntity.setProductName(orderEntity.getProductName());
    orderEntity.setAvailableQuantity(orderEntity.getAvailableQuantity());
    orderEntity.setPrice(orderEntity.getPrice());
    return  orderRepository.save(orderEntity);
    }

    @Override
    public void delOrder(Long id) {
         orderRepository.deleteById(id);
    }

    @Override
    public List<OrderEntity> findByOrderProductNameStartingWithIgnoringCase(String name) {
        return orderRepository.findByProductNameStartingWithIgnoringCase(name);

    }

}
