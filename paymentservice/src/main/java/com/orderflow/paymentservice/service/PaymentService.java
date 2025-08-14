package com.orderflow.paymentservice.service;

import com.orderflow.paymentservice.dto.PaymentNotifyStatus;
import com.orderflow.paymentservice.entity.PaymentEntity;

import java.util.List;

public interface PaymentService {

    void savePaymentData(PaymentEntity entity);

    PaymentEntity updatePaymentDetails(PaymentEntity entity, Long orderId);

    List<PaymentEntity> getAllPaymentDetails();

    void deleteByOrderId(Long Orderid);

    PaymentEntity getPaymentByOrderId(Long orderId);

    PaymentNotifyStatus sendNotify(Long orderId);
}
