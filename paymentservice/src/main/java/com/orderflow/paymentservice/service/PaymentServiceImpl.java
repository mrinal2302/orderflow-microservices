package com.orderflow.paymentservice.service;

import com.orderflow.paymentservice.entity.PaymentEntity;
import com.orderflow.paymentservice.exceptionHandler.CustomException;
import com.orderflow.paymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public PaymentEntity save(PaymentEntity entity) {
        try {
            return paymentRepository.save(entity);
        } catch (Exception e) {
            throw new CustomException("failed to save data");
        }

    }
    @Override
    public PaymentEntity updatePayment(PaymentEntity entity, Long orderId) {
        PaymentEntity payment = paymentRepository.findByOrderId(orderId);
        if (payment == null) {
            throw new CustomException("Payment not found for Order ID: " + orderId);
        }

        payment.setPaymentId(entity.getPaymentId());
        payment.setPaymentMethod(entity.getPaymentMethod());
        payment.setPaymentStatus(entity.getPaymentStatus());
        payment.setAmount(entity.getAmount());

        try {
            return paymentRepository.save(payment);
        } catch (Exception e) {
            throw new CustomException("Failed to update payment");
        }
    }


    @Override
    public List<PaymentEntity> getAll() {
        try {
            return paymentRepository.findAll();
        } catch (Exception e) {
            throw new CustomException("failed to get payment details");
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            paymentRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomException("failed to delete");
        }
    }


}