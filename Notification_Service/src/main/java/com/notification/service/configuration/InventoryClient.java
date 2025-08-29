package com.notification.service.configuration;

import com.notification.service.dto.InventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventory-service", url = "http://localhost:9093")
public interface InventoryClient {


    @GetMapping("/inventory/{productId}")

    InventoryResponse getInventoryByProductId(@PathVariable("productId") Long productId);
}
