package com.inventryService.service;

import com.inventryService.entity.InventoryEntity;

import java.util.List;

public interface InventoryService {

    InventoryEntity getValueSaveIn(InventoryEntity entity);


    InventoryEntity getOrderById(Long id);

    List<InventoryEntity> getAllOrderId();

    InventoryEntity updateOrderById(Long id, InventoryEntity entity);

    void delOrder(Long id);

    List<InventoryEntity> findByOrderProductNameStartingWithIgnoringCase(String name);

    //List<OrderEntity> findByOrderPriceGreaterThan();

    //List<OrderEntity> findByOrderPriceGreaterThan(BigDecimal ordPrice);
}


