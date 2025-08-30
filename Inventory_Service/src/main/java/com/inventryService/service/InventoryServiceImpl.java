package com.inventryService.service;

import com.inventryService.dto.NotificationRequest;
import com.inventryService.entity.InventoryEntity;
import com.inventryService.feign.NotificationClient;
import com.inventryService.globalExceptionHandler.StockNotFoundException;
import com.inventryService.globalExceptionHandler.WentOutOfStockException;
import com.inventryService.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    NotificationClient notificationClient;

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

    @Override
    public Optional<InventoryEntity> inventorySuccessById(Long id) {
        return inventoryRepository.findById(id);
    }

    @Override
    public Optional<InventoryEntity> wentOutOfStock(Long id) {
        return Optional.ofNullable(inventoryRepository.findById(id).orElseThrow(() -> new WentOutOfStockException("The stock is not available")));
    }

    @Override
    public String sendById(Long id) {
        InventoryEntity entity = inventoryRepository.findById(id).orElseThrow(() -> new WentOutOfStockException("Id not found "));
        NotificationRequest request = new NotificationRequest();
        request.setProductId(entity.getProductId());
        request.setProductName(entity.getProductName());
        request.setAvailableQuantity(entity.getAvailableQuantity());
        return notificationClient.sendById(request);
    }
}

