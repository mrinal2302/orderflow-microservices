package com.orderflow.paymentservice.service;

import com.orderflow.paymentservice.entity.PaymentEntity;
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
        return paymentRepository.save(entity);
    }

    @Override
    public PaymentEntity getByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    @Override
    public PaymentEntity updatePayment(PaymentEntity entity, Long orderId) {
        PaymentEntity existingPayment = paymentRepository.findByOrderId(orderId);
        if (existingPayment != null) {
            entity.setPaymentId(existingPayment.getPaymentId());
            return paymentRepository.save(entity);
        }
        return null;
    }

    @Override
    public List<PaymentEntity> getAll() {
        return paymentRepository.findAll();
    }

    @Override
    public PaymentEntity deleteById(Long id) {
        paymentRepository.deleteById(id);
        return null;
    }


}