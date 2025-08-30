package com.inventryService.controllerProject;

import com.inventryService.entity.InventoryEntity;
import com.inventryService.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/post_save")
    public ResponseEntity<InventoryEntity> getValueSaveIn(@RequestBody InventoryEntity entity) {
        InventoryEntity inventory = inventoryService.getValueSaveIn(entity);
        return ResponseEntity.status(HttpStatus.OK).body(inventory);
    }

    @GetMapping("/get_inventory/{id}")
    public ResponseEntity<InventoryEntity> getInventoryById(@PathVariable Long id) {
        InventoryEntity inventoryEntity = inventoryService.getInventoryById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(inventoryEntity);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<InventoryEntity>> getAllinventoryId() {
        List<InventoryEntity> inventoryEntities = inventoryService.getAllinventoryId();
        return ResponseEntity.status(HttpStatus.OK).body(inventoryEntities);
    }

    @PutMapping("/upateinventory/{id}")
    public ResponseEntity<InventoryEntity> updateInventoryById(@PathVariable Long id, @RequestBody InventoryEntity entity) {
        InventoryEntity inventoryEntities = inventoryService.updateInventoryById(id, entity);
        return ResponseEntity.status(HttpStatus.OK).body(inventoryEntities);
    }

    @DeleteMapping("/delinventory/{id}")
    public void delInventory(@PathVariable Long id) {
        inventoryService.delInventoryBYId(id);
    }

    @GetMapping("/getinventoryByProductName/{productName}")
    public ResponseEntity<List<InventoryEntity>> findByProductNameStartingWithIgnoringCase(@PathVariable String productName) {
        List<InventoryEntity> inventoryEntities = inventoryService.findByProductNameStartingWithIgnoringCase(productName);
        return ResponseEntity.status(HttpStatus.FOUND).body(inventoryEntities);
    }

    @PutMapping("/inventory_successfully/{id}")
    public ResponseEntity<Optional<InventoryEntity>> inventorySuccessById(@PathVariable Long id, @RequestBody InventoryEntity entity) {
        Optional<InventoryEntity> inventory = inventoryService.inventorySuccessById(id);
        return ResponseEntity.status(HttpStatus.OK).body(inventory);
    }


    @PutMapping("/outOfStock")
    public ResponseEntity<Optional<InventoryEntity>> wentOutOfStock(@PathVariable Long id) {
        Optional<InventoryEntity> inventoryEntity = inventoryService.wentOutOfStock(id);
        return ResponseEntity.status(HttpStatus.OK).body(inventoryEntity);

    }

    @PutMapping("/sendingId")
    public ResponseEntity<String> sendById(@PathVariable Long id) {
        String response = inventoryService.sendById(id);
        return ResponseEntity.ok(response);
    }
}



