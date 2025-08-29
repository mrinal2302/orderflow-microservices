package com.inventryService.service;

import com.inventryService.entity.InventoryEntity;

import java.util.List;
import java.util.Optional;

public interface InventoryService {

    InventoryEntity getValueSaveIn(InventoryEntity entity);


    InventoryEntity getInventoryById(Long id);

    List<InventoryEntity> getAllinventoryId();

    InventoryEntity updateInventoryById(Long id, InventoryEntity entity);

    void delInventoryBYId(Long id);

    List<InventoryEntity> findByProductNameStartingWithIgnoringCase(String productName);

    Optional<InventoryEntity> inventorySuccessById(Long id);

    Optional<InventoryEntity> wentOutOfStock(Long id);

    String sendById(Long id);
}


