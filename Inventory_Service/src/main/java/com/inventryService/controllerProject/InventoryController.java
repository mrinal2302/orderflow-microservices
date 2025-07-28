package com.inventryService.controllerProject;

import com.inventryService.entity.InventoryEntity;
import com.inventryService.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/post_save")
    private ResponseEntity<InventoryEntity> getValueSaveIn(@RequestBody InventoryEntity entity) {
        InventoryEntity inventory = inventoryService.getValueSaveIn(entity);
        return ResponseEntity.status(HttpStatus.OK).body(inventory);
    }

    @GetMapping("/get_order/{id}")
    private ResponseEntity<InventoryEntity> getOrderById(@PathVariable Long id) {
        InventoryEntity inventoryEntity = inventoryService.getOrderById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(inventoryEntity);
    }

    @GetMapping("/getAll")
    private ResponseEntity<List<InventoryEntity>> getAllOrderId() {
        List<InventoryEntity> orderEntities = inventoryService.getAllOrderId();
        return ResponseEntity.status(HttpStatus.OK).body(orderEntities);
    }

    @PutMapping("/upateOrder/{id}")
    private ResponseEntity<InventoryEntity> updateOrderById(@PathVariable Long id, @RequestBody InventoryEntity entity) {
        InventoryEntity inventoryEntities = inventoryService.updateOrderById(id, entity);
        return ResponseEntity.status(HttpStatus.OK).body(inventoryEntities);
    }

    @DeleteMapping("/delOrder/{id}")
    public void delInventory(@PathVariable Long id) {
        inventoryService.delInventory(id);
    }

    @GetMapping("/getOrderByProductName")
    private ResponseEntity<List<InventoryEntity>> findByOrderProductNameStartingWithIgnoringCase(String name) {
        List<InventoryEntity> inventoryEntities = inventoryService.findByOrderProductNameStartingWithIgnoringCase(name);
        return ResponseEntity.status(HttpStatus.FOUND).body(inventoryEntities);
    }


}
