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
    public InventoryEntity getInventoryById(Long id) {
        return inventoryRepository.findById(id).get();
    }

    @Override
    public List<InventoryEntity> getAllinventoryId() {
        return inventoryRepository.findAll();
    }

    @Override
    public InventoryEntity updateInventoryById(Long id, InventoryEntity entity) {
        InventoryEntity inventoryEntity = inventoryRepository.findById(id).get();
        inventoryEntity.setProductName(inventoryEntity.getProductName());
        inventoryEntity.setAvailableQuantity(inventoryEntity.getAvailableQuantity());
        inventoryEntity.setPrice(inventoryEntity.getPrice());
        return inventoryRepository.save(inventoryEntity);
    }

    @Override
    public void delInventoryBYId(Long id) {
        inventoryRepository.deleteById(id);
    }

    @Override
    public List<InventoryEntity> findByProductNameStartingWithIgnoringCase(String productName) {
        return inventoryRepository.findByProductNameStartingWithIgnoringCase(productName);

    }

}
