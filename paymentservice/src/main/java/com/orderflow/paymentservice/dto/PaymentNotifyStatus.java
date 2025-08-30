package com.orderflow.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentNotifyStatus {
    private Long orderId;
    private String emailAddress;
    private String paymentStatus;
}

