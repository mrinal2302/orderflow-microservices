package com.orderflow.paymentservice.service;

import com.orderflow.paymentservice.client.InventoryServiceClient;
import com.orderflow.paymentservice.client.NotificationServiceClient;
import com.orderflow.paymentservice.dto.InventoryRequest;
import com.orderflow.paymentservice.dto.NotificationRequest;
import com.orderflow.paymentservice.dto.OrderResponse;
import com.orderflow.paymentservice.entity.PaymentEntity;
import com.orderflow.paymentservice.exceptionHandler.OrderIdNotFoundException;
import com.orderflow.paymentservice.exceptionHandler.PaymentNotFound;
import com.orderflow.paymentservice.exceptionHandler.PaymentUpdateException;
import com.orderflow.paymentservice.exceptionHandler.ServiceCommunicationException;
import com.orderflow.paymentservice.model.PaymentStatus;
import com.orderflow.paymentservice.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InventoryServiceClient inventoryServiceClient;
    private final NotificationServiceClient notificationServiceClient;

    public PaymentServiceImpl(PaymentRepository paymentRepository, 
                            InventoryServiceClient inventoryServiceClient, 
                            NotificationServiceClient notificationServiceClient) {
        this.paymentRepository = paymentRepository;
        this.inventoryServiceClient = inventoryServiceClient;
        this.notificationServiceClient = notificationServiceClient;
    }

    private PaymentEntity createAndSavePayment(OrderResponse orderResponse) {
        try {
            PaymentEntity payment = new PaymentEntity();
            payment.setOrderId(orderResponse.getOrderId());
            payment.setAmount(orderResponse.getAmount());
            payment.setPaymentMethod(orderResponse.getPaymentMethod());
            payment.setPaymentStatus(PaymentStatus.PENDING);
            payment.setEmailAddress(orderResponse.getEmail());
            return paymentRepository.save(payment);
        } catch (Exception e) {
            throw new PaymentNotFound("Failed to create payment: " + e.getMessage());
        }
    }

    private void updateInventory(OrderResponse orderResponse) {
        try {
            InventoryRequest inventoryRequest = InventoryRequest.builder()
                .orderId(orderResponse.getOrderId())
                .productId(orderResponse.getProductId())
                .productName(orderResponse.getProductName())
                .quantityOrdered(orderResponse.getQuantityOrdered())
                .paymentStatus(PaymentStatus.SUCCESS.name())
                .build();
        log.info("Calling inventory service for orderId: {}", orderResponse.getOrderId());
        inventoryServiceClient.inventoryUpdate(inventoryRequest);
        log.info("Inventory service call successful for orderId: {}", orderResponse.getOrderId());
        }catch (Exception e) {
            throw new ServiceCommunicationException("Failed to update inventory: " + e.getMessage());
        }
    }

    
    private void sendNotification(OrderResponse orderResponse, PaymentEntity payment) {
        try {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                .orderId(orderResponse.getOrderId())
                .email(orderResponse.getEmail())
                .paymentStatus(PaymentStatus.SUCCESS.name())
                .amount(payment.getAmount())
                .productId(orderResponse.getProductId())
                .build();
        log.info("Calling notification service for orderId: {}", orderResponse.getOrderId());
        notificationServiceClient.sendNotification(notificationRequest);
        log.info("Notification service call successful for orderId: {}", orderResponse.getOrderId());
        }catch (Exception e) {
            throw new ServiceCommunicationException("Failed to send notification: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public String processPaymentFromOrder(OrderResponse orderResponse) {
        PaymentEntity payment = createAndSavePayment(orderResponse);
        try {

            updateInventory(orderResponse);

            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            paymentRepository.save(payment);
            

            sendNotification(orderResponse, payment);
            
            return "Payment processed successfully for order: " + orderResponse.getOrderId();
            
        } catch (Exception e) {
            log.error("Error during payment processing for orderId: {}. Updating payment status to FAILED.", 
                    payment.getOrderId(), e);
            payment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new ServiceCommunicationException("Payment processing failed: " + e.getMessage());
        }
    }

    @Override
    public PaymentEntity updatePaymentDetails(PaymentEntity entity, Long orderId) {
        PaymentEntity payment = paymentRepository.findByOrderId(orderId);
        if (payment == null) {
            throw new OrderIdNotFoundException("Payment not found for Order ID: " + orderId);
        }
        payment.setPaymentStatus(entity.getPaymentStatus());
        payment.setAmount(entity.getAmount());

        try {
            return paymentRepository.save(payment);
        } catch (Exception e) {
            throw new PaymentUpdateException("Failed to update payment: " + e.getMessage());
        }
    }

    @Override
    public List<PaymentEntity> getAllPaymentDetails() {
        try {
            return paymentRepository.findAll();
        } catch (Exception ex) {
            throw new PaymentNotFound("Failed to get payment details: " + ex.getMessage());
        }
    }
    @Override
    public PaymentEntity getPaymentByOrderId(Long orderId) {
        try {
            PaymentEntity payment = paymentRepository.findByOrderId(orderId);
            if (payment == null) {
                throw new PaymentNotFound("Payment not found for Order ID: " + orderId);
            }
            return payment;
        } catch (Exception ex) {
            throw new PaymentNotFound("Error retrieving payment details: " + ex.getMessage());
        }
    }
}