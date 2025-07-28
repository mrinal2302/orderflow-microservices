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
    private InventoryService orderService;

    @PostMapping("/post_save")
    private ResponseEntity<InventoryEntity> getValueSaveIn(@RequestBody InventoryEntity entity) {
        InventoryEntity orderEntity = orderService.getValueSaveIn(entity);
        return ResponseEntity.status(HttpStatus.OK).body(orderEntity);
    }

    @GetMapping("/get_order/{id}")
    private ResponseEntity<InventoryEntity> getOrderById(@PathVariable Long id) {
        InventoryEntity orderEntity = orderService.getOrderById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(orderEntity);
    }

    @GetMapping("/getAll")
    private ResponseEntity<List<InventoryEntity>> getAllOrderId() {
        List<InventoryEntity> orderEntities = orderService.getAllOrderId();
        return ResponseEntity.status(HttpStatus.OK).body(orderEntities);
    }

    @PutMapping("/upateOrder/{id}")
    private ResponseEntity<InventoryEntity> updateOrderById(@PathVariable Long id, @RequestBody InventoryEntity entity) {
        InventoryEntity orderEntities = orderService.updateOrderById(id, entity);
        return ResponseEntity.status(HttpStatus.OK).body(orderEntities);
    }

    @DeleteMapping("/delOrder/{id}")
    public void delOrder(@PathVariable Long id) {
        orderService.delOrder(id);
    }

    @GetMapping("/getOrderByProductName")
    private ResponseEntity<List<InventoryEntity>> findByOrderProductNameStartingWithIgnoringCase(String name) {
        List<InventoryEntity> orderEntities = orderService.findByOrderProductNameStartingWithIgnoringCase(name);
        return ResponseEntity.status(HttpStatus.FOUND).body(orderEntities);
    }


}
