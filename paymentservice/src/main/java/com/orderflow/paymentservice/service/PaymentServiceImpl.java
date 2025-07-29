package com.orderflow.paymentservice.service;

import com.orderflow.paymentservice.entity.PaymentEntity;
import com.orderflow.paymentservice.exceptionHandler.OrderIdNotFoundException;
import com.orderflow.paymentservice.exceptionHandler.PaymentNotFound;
import com.orderflow.paymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        payment.setPaymentId(entity.getPaymentId());
        payment.setPaymentMethod(entity.getPaymentMethod());
        payment.setPaymentStatus(entity.getPaymentStatus());
        payment.setAmount(entity.getAmount());

        try {
            return paymentRepository.save(payment);
        } catch (Exception e) {
            throw new OrderIdNotFoundException("Failed to update payment");
        }
    }


    @Override
    public List<PaymentEntity> getAllPaymentDetails() {
        try {
            return paymentRepository.findAll();
        } catch (Exception e) {
            throw new PaymentNotFound("failed to get payment details");
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


}