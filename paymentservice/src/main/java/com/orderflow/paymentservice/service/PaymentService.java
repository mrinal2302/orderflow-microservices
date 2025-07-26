package com.orderflow.paymentservice.service;

import com.orderflow.paymentservice.entity.PaymentEntity;
import jakarta.persistence.Id;

import java.util.List;

public interface PaymentService {

    PaymentEntity save(PaymentEntity entity);

    PaymentEntity updatePayment(PaymentEntity entity, Long orderId);

    List<PaymentEntity> getAll();

    void deleteById(Long id);
}
