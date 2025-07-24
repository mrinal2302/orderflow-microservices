package com.orderflow.paymentservice.service;

import com.orderflow.paymentservice.entity.PaymentEntity;

import java.util.List;

public interface PaymentService {

    PaymentEntity save(PaymentEntity entity);

    PaymentEntity getByOrderId(Long orderId);

    PaymentEntity updatePayment(PaymentEntity entity, Long orderId);

    List<PaymentEntity> getAll();

    PaymentEntity deleteById(Long id);
}
