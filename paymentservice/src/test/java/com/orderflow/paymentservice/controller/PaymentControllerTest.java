package com.orderflow.paymentservice.controller;

import com.orderflow.paymentservice.dto.OrderResponse;
import com.orderflow.paymentservice.entity.PaymentEntity;
import com.orderflow.paymentservice.model.PaymentMethod;
import com.orderflow.paymentservice.model.PaymentStatus;
import com.orderflow.paymentservice.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaymentService paymentService;

    private PaymentEntity paymentEntity;
    private OrderResponse orderResponse;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


        paymentEntity = PaymentEntity.builder()
                .paymentId(1L)
                .orderId(101L)
                .amount(500.0)
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .paymentStatus(PaymentStatus.SUCCESS)
                .emailAddress("test@example.com")enum
                .build();


        orderResponse = new OrderResponse();
        orderResponse.setOrderId(101L);
        orderResponse.setAmount(500.0);
        orderResponse.setPaymentMode("UPI");
        orderResponse.setEmail("test@example.com");

    }



    @Test
    void testProcessPayment_Success() {
        when(paymentService.processPaymentFromOrder(any(OrderResponse.class)))
                .thenReturn("Payment processed successfully");

        ResponseEntity<String> response = paymentController.processPayment(orderResponse);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Payment processed successfully", response.getBody());
        verify(paymentService, times(1)).processPaymentFromOrder(any(OrderResponse.class));
    }

    @Test
    void testProcessPayment_Failure() {
        when(paymentService.processPaymentFromOrder(any(OrderResponse.class)))
                .thenReturn("Payment failed");

        ResponseEntity<String> response = paymentController.processPayment(orderResponse);

        assertEquals("Payment failed", response.getBody());
    }

    @Test
    void testProcessPayment_Exception() {
        when(paymentService.processPaymentFromOrder(any(OrderResponse.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        assertThrows(RuntimeException.class,
                () -> paymentController.processPayment(orderResponse));
    }



    @Test
    void testGetAllPayments_Success() {
        when(paymentService.getAllPaymentDetails())
                .thenReturn(Arrays.asList(paymentEntity));

        ResponseEntity<List<PaymentEntity>> response = paymentController.getAllPayments();

        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetAllPayments_Failure_EmptyList() {
        when(paymentService.getAllPaymentDetails())
                .thenReturn(Collections.emptyList());

        ResponseEntity<List<PaymentEntity>> response = paymentController.getAllPayments();

        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetAllPayments_Exception() {
        when(paymentService.getAllPaymentDetails())
                .thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class,
                () -> paymentController.getAllPayments());
    }



    @Test
    void testUpdateByOrderId_Success() {
        when(paymentService.updatePaymentDetails(any(PaymentEntity.class), anyLong()))
                .thenReturn(paymentEntity);

        ResponseEntity<PaymentEntity> response = paymentController.updateByOrderId(paymentEntity, 101L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(101L, response.getBody().getOrderId());
    }

    @Test
    void testUpdateByOrderId_Failure() {
        when(paymentService.updatePaymentDetails(any(PaymentEntity.class), anyLong()))
                .thenReturn(null);

        ResponseEntity<PaymentEntity> response = paymentController.updateByOrderId(paymentEntity, 101L);

        assertNull(response.getBody());
    }

    @Test
    void testUpdateByOrderId_Exception() {
        when(paymentService.updatePaymentDetails(any(PaymentEntity.class), anyLong()))
                .thenThrow(new RuntimeException("Update failed"));

        assertThrows(RuntimeException.class,
                () -> paymentController.updateByOrderId(paymentEntity, 101L));
    }



    @Test
    void testDeleteOrderDetails_Success() {
        doNothing().when(paymentService).deleteByPaymentId(anyLong());

        ResponseEntity<String> response = paymentController.deleteOrderDetails(1L);

        assertEquals("deleted data", response.getBody());
    }

    @Test
    void testDeleteOrderDetails_Failure() {
        doThrow(new IllegalArgumentException("Payment not found"))
                .when(paymentService).deleteByPaymentId(anyLong());

        assertThrows(IllegalArgumentException.class,
                () -> paymentController.deleteOrderDetails(1L));
    }

    @Test
    void testDeleteOrderDetails_Exception() {
        doThrow(new RuntimeException("Unexpected error"))
                .when(paymentService).deleteByPaymentId(anyLong());

        assertThrows(RuntimeException.class,
                () -> paymentController.deleteOrderDetails(1L));
    }



    @Test
    void testGetPaymentByOrderId_Success() {
        when(paymentService.getPaymentByOrderId(anyLong()))
                .thenReturn(paymentEntity);

        ResponseEntity<PaymentEntity> response = paymentController.getPaymentByOrderId(101L);

        assertEquals(101L, response.getBody().getOrderId());
    }

    @Test
    void testGetPaymentByOrderId_Failure() {
        when(paymentService.getPaymentByOrderId(anyLong()))
                .thenReturn(null);

        ResponseEntity<PaymentEntity> response = paymentController.getPaymentByOrderId(101L);

        assertNull(response.getBody());
    }

    @Test
    void testGetPaymentByOrderId_Exception() {
        when(paymentService.getPaymentByOrderId(anyLong()))
                .thenThrow(new RuntimeException("Database failure"));

        assertThrows(RuntimeException.class,
                () -> paymentController.getPaymentByOrderId(101L));
    }
}