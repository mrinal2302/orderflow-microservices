package com.orderflow.paymentservice.service;

import com.orderflow.paymentservice.entity.PaymentEntity;

import java.util.List;

public interface PaymentService {

    void saveData(PaymentEntity entity);

    PaymentEntity updatePaymentDetails(PaymentEntity entity, Long orderId);

    List<PaymentEntity> getAllPaymentDetails();

    void deleteByOrderId(Long Orderid);
}
