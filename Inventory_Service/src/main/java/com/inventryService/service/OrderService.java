package com.inventryService.service;

import com.inventryService.entity.OrderEntity;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {

    OrderEntity getValueSaveIn(OrderEntity entity);


    OrderEntity getOrderById(Long id);

    List<OrderEntity> getAllOrderId();

    OrderEntity updateOrderById(Long id, OrderEntity entity);

    void delOrder(Long id);

    List<OrderEntity> findByOrderProductNameStartingWithIgnoringCase(String name);

    //List<OrderEntity> findByOrderPriceGreaterThan();

    //List<OrderEntity> findByOrderPriceGreaterThan(BigDecimal ordPrice);
}


