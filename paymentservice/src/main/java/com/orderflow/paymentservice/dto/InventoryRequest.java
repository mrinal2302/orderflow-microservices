package com.orderflow.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryRequest {
    private Long orderId;
    private Long productId;
    private String productName;
    private int quantityOrdered;
    private String paymentStatus;
    private String email;
}
