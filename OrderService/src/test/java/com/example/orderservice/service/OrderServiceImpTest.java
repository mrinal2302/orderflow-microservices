package com.example.orderservice.service;

import com.example.orderservice.dto.PaymentRequest;
import com.example.orderservice.entities.OrderEntity;
import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.enums.PaymentMode;
import com.example.orderservice.exceptionhandler.OrderIdNotFoundException;
import com.example.orderservice.feign.PaymentClient;
import com.example.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImpTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderServiceImp orderServiceImp;
    @Mock
    private PaymentClient paymentClient;
    private OrderEntity mockOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockOrder = new OrderEntity();
        mockOrder.setOrderId(1L);
        mockOrder.setProductId("101");
        mockOrder.setProductName("Laptop");
        mockOrder.setQuantity(2);
        mockOrder.setStatus(OrderStatus.PENDING);
        mockOrder.setPaymentMode(PaymentMode.CREDIT_CARD);
        mockOrder.setEmail("test@example.com");
        mockOrder.setMessage("Order placed");
    }

    @Test
    void testSavedOrder_Success() {
        when(orderRepository.save(mockOrder)).thenReturn(mockOrder);

        String response = orderServiceImp.savedOrder(mockOrder);

        assertEquals("Order Saved In The Cart", response);
        verify(orderRepository, times(1)).save(mockOrder);
    }

    @Test
    void testSavedOrder_Failure() {
        when(orderRepository.save(null))
                .thenThrow(new IllegalArgumentException("Entity must not be null"));

        assertThrows(IllegalArgumentException.class, () -> orderServiceImp.savedOrder(null));
        verify(orderRepository, never()).save(any(OrderEntity.class));
    }

    @Test
    void testSavedOrder_Exception() {
        when(orderRepository.save(mockOrder)).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> orderServiceImp.savedOrder(mockOrder));
        verify(orderRepository, times(1)).save(mockOrder);
    }

    @Test
    void testGetAllTheOrders_Success() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(mockOrder));

        List<Map<String, Object>> orders = orderServiceImp.getAllTheOrders();

        assertEquals(1, orders.size());
        assertEquals(1L, orders.get(0).get("orderId"));
        assertEquals(OrderStatus.PENDING, orders.get(0).get("status"));
        assertEquals("test@example.com", orders.get(0).get("email"));
        assertEquals("Order placed", orders.get(0).get("message"));

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetAllTheOrders_Failure_EmptyList() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        List<Map<String, Object>> orders = orderServiceImp.getAllTheOrders();

        assertNotNull(orders);
        assertTrue(orders.isEmpty());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetAllTheOrders_Exception() {
        when(orderRepository.findAll()).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> orderServiceImp.getAllTheOrders());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetOrderById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));

        Map<String, Object> result = orderServiceImp.getOrderById(1L);

        assertEquals(1L, result.get("orderId"));
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOrderById_Failure_InvalidId() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OrderIdNotFoundException.class, () -> orderServiceImp.getOrderById(99L));
        verify(orderRepository, times(1)).findById(99L);
    }

    @Test
    void testGetOrderById_Exception() {
        when(orderRepository.findById(1L)).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> orderServiceImp.getOrderById(1L));
        verify(orderRepository, times(1)).findById(1L);
    }
/*
    @Test
    void testGetOrderDetailsForPayment_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));

        PaymentRequest request = orderServiceImp.getOrderDetailsForPayment(1L);

        assertNotNull(request);
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOrderDetailsForPayment_Failure_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderIdNotFoundException.class, () -> orderServiceImp.getOrderDetailsForPayment(1L));
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOrderDetailsForPayment_Exception() {
        when(orderRepository.findById(1L)).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> orderServiceImp.getOrderDetailsForPayment(1L));
        verify(orderRepository, times(1)).findById(1L);
    }*/


    @Test
    void testSendOrderForPayment_Success() {

        Long orderId = 1L;
        OrderEntity mockOrder = new OrderEntity();
        mockOrder.setOrderId(orderId);
        mockOrder.setProductId("101");
        mockOrder.setProductName("Laptop");
        mockOrder.setQuantity(2);
        mockOrder.setStatus(OrderStatus.PENDING);
        mockOrder.setPaymentMode(PaymentMode.CREDIT_CARD);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        when(paymentClient.processPayment(any(PaymentRequest.class)))
                .thenReturn("Payment processed successfully for OrderID: " + orderId);

        String result = orderServiceImp.sendOrderForPayment(orderId);

        assertEquals("Payment processed successfully for OrderID: 1", result);

        verify(orderRepository, times(1)).findById(orderId);
        verify(paymentClient, times(1)).processPayment(any(PaymentRequest.class));
    }

    @Test
    void testSendOrderForPayment_OrderNotFound() {

        Long invalidOrderId = 99L;
        when(orderRepository.findById(invalidOrderId)).thenReturn(Optional.empty());

        OrderIdNotFoundException exception = assertThrows(
                OrderIdNotFoundException.class,
                () -> orderServiceImp.sendOrderForPayment(invalidOrderId)
        );

        assertEquals("OrderID 99 is Invalid", exception.getMessage());

        verify(orderRepository, times(1)).findById(invalidOrderId);
        verify(paymentClient, never()).processPayment(any());
    }

    @Test
    void testSendOrderForPayment_PaymentServiceException() {
        Long orderId = 2L;

        OrderEntity mockOrder = new OrderEntity();
        mockOrder.setOrderId(orderId);
        mockOrder.setProductId("202");
        mockOrder.setProductName("Phone");
        mockOrder.setQuantity(1);
        mockOrder.setStatus(OrderStatus.PENDING);
        mockOrder.setPaymentMode(PaymentMode.UPI);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        when(paymentClient.processPayment(any(PaymentRequest.class)))
                .thenThrow(new RuntimeException("Payment Service Down"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> orderServiceImp.sendOrderForPayment(orderId)
        );
        assertEquals("Payment Service Down", exception.getMessage());
        verify(orderRepository, times(1)).findById(orderId);
        verify(paymentClient, times(1)).processPayment(any(PaymentRequest.class));
    }
}
