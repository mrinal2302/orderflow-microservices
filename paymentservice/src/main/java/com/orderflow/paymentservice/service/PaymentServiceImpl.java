package com.orderflow.paymentservice.service;

import com.orderflow.paymentservice.dto.PaymentNotifyStatus;
import com.orderflow.paymentservice.entity.PaymentEntity;
import com.orderflow.paymentservice.exceptionHandler.OrderIdNotFoundException;
import com.orderflow.paymentservice.exceptionHandler.PaymentNotFound;
import com.orderflow.paymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {


    @Autowired
    private PaymentRepository paymentRepository;


    @Override
    public void savePaymentData(PaymentEntity entity) {
        try {
            paymentRepository.save(entity);
        } catch (Exception e) {
            throw new OrderIdNotFoundException("failed to save data");
        }

    }

    @Override
    public PaymentEntity updatePaymentDetails(PaymentEntity entity, Long orderId) {
        PaymentEntity payment = paymentRepository.findByOrderId(orderId);
        if (payment == null) {
            throw new OrderIdNotFoundException("Payment not found for Order ID: " + orderId);
        }
        payment.setPaymentMethod(entity.getPaymentMethod());
        payment.setPaymentStatus(entity.getPaymentStatus());
        payment.setAmount(entity.getAmount());

        try {
            return paymentRepository.save(payment);
        } catch (Exception e) {
            throw new OrderIdNotFoundException("Failed to update payment: " + e.getMessage());
        }
    }


    @Override
    public List<PaymentEntity> getAllPaymentDetails() {
        try {
            return paymentRepository.findAll();
        } catch (Exception e) {
            throw new PaymentNotFound("failed to get payment details: " + e.getMessage());
        }
    }

    @Override
    public void deleteByOrderId(Long Orderid) {
        try {
            paymentRepository.deleteById(Orderid);
        } catch (Exception e) {
            throw new OrderIdNotFoundException("failed to delete");
        }
    }

    @Override
    public PaymentEntity getPaymentByOrderId(Long orderId) {
        PaymentEntity payment = paymentRepository.findByOrderId(orderId);
        if (payment == null) {
            throw new PaymentNotFound("Payment not found for Order ID: " + orderId);
        }
        return payment;
    }

    @Override
    public PaymentNotifyStatus sendNotify(Long orderId) {
        PaymentEntity entity = getPaymentByOrderId(orderId);
        if (entity == null) {
            throw new IllegalArgumentException("Payment entity not found for orderId: " + orderId);
        }
        try {
            return PaymentNotifyStatus.builder()
                    .orderId(entity.getOrderId())
                    .emailAddress(entity.getEmailAddress())
                    .paymentStatus(String.valueOf(entity.getPaymentStatus()))
                    .build();
        } catch (NullPointerException e) {
            throw new IllegalStateException("Failed to process payment notification due to missing required fields", e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while processing payment notification", e);
        }
    }


}