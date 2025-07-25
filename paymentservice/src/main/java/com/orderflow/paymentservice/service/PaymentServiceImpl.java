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
            throw new CustomException("failed");
        }
//        return paymentRepository.save(entity);

    }

    @Override
    public PaymentEntity getByOrderId(Long orderId) {
        PaymentEntity payment = paymentRepository.findByOrderId(orderId);
        if (payment == null) {
            throw new CustomException("Payment not found for this order id:" + orderId);
        }
        return payment;
    }

    @Override
    public PaymentEntity updatePayment(PaymentEntity entity, Long orderId) {
        PaymentEntity Payment = paymentRepository.findByOrderId(orderId);
        if (Payment != null) {
            entity.setPaymentId(Payment.getPaymentId());
            try {
                return paymentRepository.save(entity);

            } catch (Exception e) {
                throw new CustomException("Failed");
            }
        }
        throw new CustomException("payment not found");
    }

    @Override
    public List<PaymentEntity> getAll() {
        try {
            return paymentRepository.findAll();
        } catch (Exception e) {
            throw new CustomException("failed to get payment");
        }
    }

    @Override
    public PaymentEntity deleteById(Long id) {
        try {
            paymentRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomException("failed to delete");
        }
        return null;
    }


}