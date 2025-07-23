package com.inventryService.controllerProject;

import com.inventryService.entity.OrderEntity;
import com.inventryService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/req")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/post_save")
    private  ResponseEntity<OrderEntity> getValueSaveIn(@RequestBody OrderEntity entity){
        OrderEntity orderEntity = orderService.getValueSaveIn(entity);
      return   ResponseEntity.status(HttpStatus.OK).body(orderEntity);
    }

    @GetMapping("/get_order/{id}")
    private  ResponseEntity<OrderEntity> getOrderById(@PathVariable Long id){
        OrderEntity orderEntity = orderService.getOrderById(id);
        return  ResponseEntity.status(HttpStatus.FOUND).body(orderEntity);
    }

    @GetMapping("/getAll")
    private ResponseEntity<List<OrderEntity>> getAllOrderId(){
        List<OrderEntity> orderEntities = orderService.getAllOrderId();
        return ResponseEntity.status(HttpStatus.OK).body(orderEntities);
    }

    @PutMapping("/upateOrder/{id}")
    private  ResponseEntity<OrderEntity> updateOrderById(@PathVariable Long id,@RequestBody OrderEntity entity){
        OrderEntity orderEntities = orderService.updateOrderById(id,entity);
        return ResponseEntity.status(HttpStatus.OK).body(orderEntities);
    }
    @DeleteMapping("/delOrder/{id}")
    public void delOrder(@PathVariable Long id) {
        orderService.delOrder(id);
    }

    @GetMapping("/getOrderByProductName")
    private ResponseEntity<List<OrderEntity>> findByOrderProductNameStartingWithIgnoringCase(String name){
        List<OrderEntity> orderEntities = orderService.findByOrderProductNameStartingWithIgnoringCase(name);
        return  ResponseEntity.status(HttpStatus.FOUND).body(orderEntities);
    }


}
