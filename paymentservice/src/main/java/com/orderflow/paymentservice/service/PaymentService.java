package com.orderflow.paymentservice.service;

import com.orderflow.paymentservice.dto.OrderResponse;
import com.orderflow.paymentservice.entity.PaymentEntity;

import java.util.List;

public interface PaymentService {

    String processPaymentFromOrder(OrderResponse orderResponse);

    PaymentEntity updatePaymentDetails(PaymentEntity entity, Long orderId);

    List<PaymentEntity> getAllPaymentDetails();

    void deleteByPaymentId(Long paymentId);

    PaymentEntity getPaymentByOrderId(Long orderId);
}
