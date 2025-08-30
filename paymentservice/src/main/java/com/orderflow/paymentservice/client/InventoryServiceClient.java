package com.orderflow.paymentservice.client;

import com.orderflow.paymentservice.dto.InventoryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventoryservice", url = "${INVENTORY_SERVICE_URL:http://localhost:8082}")
public interface InventoryServiceClient {

    @PostMapping("/inventory/update")
    void inventoryUpdate(@RequestBody InventoryRequest inventoryRequest);
}
