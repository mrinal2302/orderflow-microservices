package com.inventryService.controllerProject;

import com.inventryService.entity.InventoryEntity;
import com.inventryService.model.ReverseStockRequest;
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

    @GetMapping("/get_inventory/{id}")
    private ResponseEntity<InventoryEntity> getInventoryById(@PathVariable Long id) {
        InventoryEntity inventoryEntity = inventoryService.getInventoryById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(inventoryEntity);
    }

    @GetMapping("/getAll")
    private ResponseEntity<List<InventoryEntity>> getAllinventoryId() {
        List<InventoryEntity> inventoryEntities = inventoryService.getAllinventoryId();
        return ResponseEntity.status(HttpStatus.OK).body(inventoryEntities);
    }

    @PutMapping("/upateinventory/{id}")
    private ResponseEntity<InventoryEntity> updateInventoryById(@PathVariable Long id, @RequestBody InventoryEntity entity) {
        InventoryEntity inventoryEntities = inventoryService.updateInventoryById(id, entity);
        return ResponseEntity.status(HttpStatus.OK).body(inventoryEntities);
    }

    @DeleteMapping("/delinventory/{id}")
    public void delInventory(@PathVariable Long id) {
        inventoryService.delInventoryBYId(id);
    }

    @GetMapping("/getinventoryByProductName/{productName}")
    private ResponseEntity<List<InventoryEntity>> findByProductNameStartingWithIgnoringCase(@PathVariable String productName) {
        List<InventoryEntity> inventoryEntities = inventoryService.findByProductNameStartingWithIgnoringCase(productName);
        return ResponseEntity.status(HttpStatus.FOUND).body(inventoryEntities);
    }

    private ResponseEntity<String> reverseStock(@RequestBody ReverseStockRequest request) {
        try {
            inventoryService.reverseStock(request.getProductId(), request.getQuantity());
            return ResponseEntity.ok("Stock is reserved");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
    }


}
