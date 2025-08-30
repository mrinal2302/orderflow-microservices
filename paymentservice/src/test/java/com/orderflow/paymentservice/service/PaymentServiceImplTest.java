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
import com.orderflow.paymentservice.model.PaymentMethod;
import com.orderflow.paymentservice.model.PaymentStatus;
import com.orderflow.paymentservice.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private InventoryServiceClient inventoryServiceClient;

    @Mock
    private NotificationServiceClient notificationServiceClient;

    private PaymentEntity paymentEntity;
    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {
        paymentEntity = PaymentEntity.builder()
                .paymentId(1L)
                .orderId(101L)
                .amount(500.0)
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .paymentStatus(PaymentStatus.SUCCESS)
                .emailAddress("test@example.com")
                .build();

        orderResponse = new OrderResponse();
        orderResponse.setOrderId(101L);
        orderResponse.setAmount(500.0);
        orderResponse.setPaymentMode("CREDIT_CARD");
        orderResponse.setEmail("test@example.com");
        orderResponse.setProductId(10L);
        orderResponse.setProductName("Laptop");
        orderResponse.setQuantityOrdered(1);
    }



    @Test
    void testProcessPaymentFromOrder_Success() {
        when(paymentRepository.save(any(PaymentEntity.class))).thenReturn(paymentEntity);
        doNothing().when(inventoryServiceClient).inventoryUpdate(any(InventoryRequest.class));
        doNothing().when(notificationServiceClient).sendNotification(any(NotificationRequest.class));

        String result = paymentService.processPaymentFromOrder(orderResponse);

        assertEquals("Payment processed successfully", result);
        verify(paymentRepository, atLeastOnce()).save(any(PaymentEntity.class));
    }

    @Test
    void testProcessPaymentFromOrder_Failure_ServiceThrowsIllegalArgument() {
        when(paymentRepository.save(any(PaymentEntity.class))).thenReturn(paymentEntity);
        doThrow(new IllegalArgumentException("Invalid product")).when(inventoryServiceClient)
                .inventoryUpdate(any(InventoryRequest.class));

        assertThrows(IllegalArgumentException.class, () -> paymentService.processPaymentFromOrder(orderResponse));
        verify(paymentRepository, atLeastOnce()).save(any(PaymentEntity.class));
    }

    @Test
    void testProcessPaymentFromOrder_Exception_ServiceCommunicationFailure() {
        when(paymentRepository.save(any(PaymentEntity.class))).thenReturn(paymentEntity);
        doThrow(new RuntimeException("Inventory down")).when(inventoryServiceClient)
                .inventoryUpdate(any(InventoryRequest.class));

        ServiceCommunicationException ex = assertThrows(ServiceCommunicationException.class,
                () -> paymentService.processPaymentFromOrder(orderResponse));

        assertTrue(ex.getMessage().contains("Service communication failed"));
    }

    @Test
    void testProcessPaymentFromOrder_notificationFailure_marksFailedAndThrowsServiceCommunication() {
        when(paymentRepository.save(any(PaymentEntity.class))).thenReturn(paymentEntity);
        doNothing().when(inventoryServiceClient).inventoryUpdate(any(InventoryRequest.class));
        doThrow(new RuntimeException("notification down")).when(notificationServiceClient).sendNotification(any());

        ServiceCommunicationException ex = assertThrows(ServiceCommunicationException.class,
                () -> paymentService.processPaymentFromOrder(orderResponse));
        assertTrue(ex.getMessage().contains("Service communication failed"));

        ArgumentCaptor<PaymentEntity> captor = ArgumentCaptor.forClass(PaymentEntity.class);
        verify(paymentRepository, atLeast(2)).save(captor.capture());
        PaymentEntity lastSaved = captor.getAllValues().get(captor.getAllValues().size() - 1);
        assertEquals(PaymentStatus.FAILED, lastSaved.getPaymentStatus());
    }



    @Test
    void testUpdatePaymentDetails_Success() {
        when(paymentRepository.findByOrderId(101L)).thenReturn(paymentEntity);
        when(paymentRepository.save(any(PaymentEntity.class))).thenReturn(paymentEntity);

        PaymentEntity updated = paymentService.updatePaymentDetails(paymentEntity, 101L);

        assertEquals(PaymentStatus.SUCCESS, updated.getPaymentStatus());
        verify(paymentRepository, times(1)).save(paymentEntity);
    }

    @Test
    void testUpdatePaymentDetails_Failure_NotFound() {
        when(paymentRepository.findByOrderId(101L)).thenReturn(null);

        assertThrows(OrderIdNotFoundException.class,
                () -> paymentService.updatePaymentDetails(paymentEntity, 101L));
    }

    @Test
    void testUpdatePaymentDetails_Exception_SaveFails() {
        when(paymentRepository.findByOrderId(101L)).thenReturn(paymentEntity);
        when(paymentRepository.save(any(PaymentEntity.class)))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(PaymentUpdateException.class,
                () -> paymentService.updatePaymentDetails(paymentEntity, 101L));
    }



    @Test
    void testGetAllPaymentDetails_Success() {
        when(paymentRepository.findAll()).thenReturn(Arrays.asList(paymentEntity));

        List<PaymentEntity> payments = paymentService.getAllPaymentDetails();

        assertEquals(1, payments.size());
    }

    @Test
    void testGetAllPaymentDetails_Failure_EmptyList() {
        when(paymentRepository.findAll()).thenReturn(Arrays.asList());

        List<PaymentEntity> payments = paymentService.getAllPaymentDetails();

        assertTrue(payments.isEmpty());
    }



    @Test
    void testDeleteByPaymentId_Success() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(paymentEntity));
        doNothing().when(paymentRepository).delete(paymentEntity);

        paymentService.deleteByPaymentId(1L);

        verify(paymentRepository, times(1)).delete(paymentEntity);
    }

    @Test
    void testDeleteByPaymentId_Failure_NotFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PaymentNotFound.class, () -> paymentService.deleteByPaymentId(1L));
    }



    @Test
    void testGetPaymentByOrderId_Success() {
        when(paymentRepository.findByOrderId(101L)).thenReturn(paymentEntity);

        PaymentEntity result = paymentService.getPaymentByOrderId(101L);

        assertEquals(101L, result.getOrderId());
    }

    @Test
    void testGetPaymentByOrderId_Failure_NotFound() {
        when(paymentRepository.findByOrderId(101L)).thenReturn(null);

        assertThrows(PaymentNotFound.class, () -> paymentService.getPaymentByOrderId(101L));
    }
}