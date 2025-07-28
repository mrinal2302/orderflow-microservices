package com.inventryService.service;

import com.inventryService.entity.InventoryEntity;
import com.inventryService.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {
    @Autowired
    private InventoryRepository orderRepository;

    @Override
    public InventoryEntity getValueSaveIn(InventoryEntity entity) {
        return orderRepository.save(entity);
    }

    @Override
    public InventoryEntity getOrderById(Long id) {
        return orderRepository.findById(id).get();
    }

    @Override
    public List<InventoryEntity> getAllOrderId() {
        return orderRepository.findAll();
    }

    @Override
    public InventoryEntity updateOrderById(Long id, InventoryEntity entity) {
        InventoryEntity orderEntity = orderRepository.findById(id).get();
        orderEntity.setProductName(orderEntity.getProductName());
        orderEntity.setAvailableQuantity(orderEntity.getAvailableQuantity());
        orderEntity.setPrice(orderEntity.getPrice());
        return orderRepository.save(orderEntity);
    }

    @Override
    public void delOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<InventoryEntity> findByOrderProductNameStartingWithIgnoringCase(String name) {
        return orderRepository.findByProductNameStartingWithIgnoringCase(name);

    }

}
