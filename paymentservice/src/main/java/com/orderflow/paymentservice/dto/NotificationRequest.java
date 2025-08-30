package com.orderflow.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequest {
    private Long productId;
    private Long orderId;
    private String email;
    private String paymentStatus;
    private Double amount;
}
