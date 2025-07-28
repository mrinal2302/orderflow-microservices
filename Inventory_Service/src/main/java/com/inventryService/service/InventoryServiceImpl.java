package com.inventryService.service;

import com.inventryService.entity.InventoryEntity;
import com.inventryService.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public InventoryEntity getValueSaveIn(InventoryEntity entity) {
        return inventoryRepository.save(entity);
    }

    @Override
    public InventoryEntity getOrderById(Long id) {
        return inventoryRepository.findById(id).get();
    }

    @Override
    public List<InventoryEntity> getAllOrderId() {
        return inventoryRepository.findAll();
    }

    @Override
    public InventoryEntity updateOrderById(Long id, InventoryEntity entity) {
        InventoryEntity orderEntity = inventoryRepository.findById(id).get();
        orderEntity.setProductName(orderEntity.getProductName());
        orderEntity.setAvailableQuantity(orderEntity.getAvailableQuantity());
        orderEntity.setPrice(orderEntity.getPrice());
        return inventoryRepository.save(orderEntity);
    }

    @Override
    public void delInventory(Long id) {
        inventoryRepository.deleteById(id);
    }

    @Override
    public List<InventoryEntity> findByOrderProductNameStartingWithIgnoringCase(String name) {
        return inventoryRepository.findByProductNameStartingWithIgnoringCase(name);

    }

}
