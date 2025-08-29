package com.example.orderservice.dto;

import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    private Long orderId;
    private String productId;
    private String productName;
    private long quantity;
    private OrderStatus status;
    private PaymentMode paymentMode;

    public PaymentRequest(long orderId, String productId, String customerName, long quantity, OrderStatus status,
                          PaymentMode paymentMode) {
    }
}
