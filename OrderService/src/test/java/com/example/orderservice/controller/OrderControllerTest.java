package com.example.orderservice.controller;

import com.example.orderservice.dto.PaymentRequest;
import com.example.orderservice.entities.OrderEntity;
import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.enums.PaymentMode;
import com.example.orderservice.exceptionhandler.OrderIdNotFoundException;
import com.example.orderservice.service.OrderServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderServiceImp orderServiceImp;
    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveOrder_Success() {
        OrderEntity order = new OrderEntity();
        when(orderServiceImp.savedOrder(order)).thenReturn("Order placed successfully");

        ResponseEntity<String> response = orderController.savedOrder(order);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Order placed successfully", response.getBody());
        verify(orderServiceImp, times(1)).savedOrder(order);
    }

    @Test
    void testSaveOrder_Failure() {
        OrderEntity order = new OrderEntity();
        when(orderServiceImp.savedOrder(order)).thenReturn(null);

        ResponseEntity<String> response = orderController.savedOrder(order);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNull(response.getBody());
        verify(orderServiceImp, times(1)).savedOrder(order);
    }

    @Test
    void testSaveOrder_Exception() {
        OrderEntity order = new OrderEntity();
        when(orderServiceImp.savedOrder(order)).thenThrow(new RuntimeException("OrderDataNotSaved error"));

        assertThrows(RuntimeException.class, () -> orderController.savedOrder(order));
        verify(orderServiceImp, times(1)).savedOrder(order);
    }

    @Test
    void testGetAllOrders_Success() {
        List<Map<String, Object>> mockOrders = new ArrayList<>();
        mockOrders.add(Map.of("id", 1, "status", "SUCCESS"));
        when(orderServiceImp.getAllTheOrders()).thenReturn(mockOrders);

        ResponseEntity<List<Map<String, Object>>> response = orderController.getAllTheOrders();

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(orderServiceImp, times(1)).getAllTheOrders();
    }

    @Test
    void testGetAllOrders_Failure() {
        when(orderServiceImp.getAllTheOrders()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Map<String, Object>>> response = orderController.getAllTheOrders();

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(orderServiceImp, times(1)).getAllTheOrders();
    }

    @Test
    void testGetAllOrders_Exception() {
        when(orderServiceImp.getAllTheOrders()).thenThrow(new RuntimeException("All Order Service unavailable"));

        assertThrows(RuntimeException.class, () -> orderController.getAllTheOrders());
        verify(orderServiceImp, times(1)).getAllTheOrders();
    }

    @Test
    void testGetOrderById_Success() {
        Map<String, Object> mockOrder = Map.of("id", 1, "status", "SUCCESS");
        when(orderServiceImp.getOrderById(1L)).thenReturn(mockOrder);

        ResponseEntity<Map<String, Object>> response = orderController.getOrderById(1L);

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals("SUCCESS", response.getBody().get("status"));
        verify(orderServiceImp, times(1)).getOrderById(1L);
    }

    @Test
    void testGetOrderById_Failure() {
        when(orderServiceImp.getOrderById(1L)).thenReturn(null);

        ResponseEntity<Map<String, Object>> response = orderController.getOrderById(1L);

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(orderServiceImp, times(1)).getOrderById(1L);
    }

    @Test
    void testGetOrderById_Exception() {
        when(orderServiceImp.getOrderById(1L)).thenThrow(new RuntimeException("Order Not Found"));

        assertThrows(RuntimeException.class, () -> orderController.getOrderById(1L));
        verify(orderServiceImp, times(1)).getOrderById(1L);
    }
/*

    @Test
    void testGetOrderDetailsForPayment_Success() {
        long orderId = 1L;
        PaymentRequest mockPaymentRequest = new PaymentRequest(
                orderId,
                "101",
                "Laptop",
                2,
                OrderStatus.PENDING,
                PaymentMode.CREDIT_CARD
        );

        when(orderServiceImp.getOrderDetailsForPayment(orderId)).thenReturn(mockPaymentRequest);

        PaymentRequest response = orderController.getOrderDetailsForPayment(orderId);

        assertNotNull(response);
        verify(orderServiceImp, times(1)).getOrderDetailsForPayment(orderId);
    }

    @Test
    void testGetOrderDetailsForPayment_Failure() {
        long orderId = 1L;
        when(orderServiceImp.getOrderDetailsForPayment(orderId)).thenReturn(null);

        PaymentRequest response = orderController.getOrderDetailsForPayment(orderId);

        assertNull(response);
        verify(orderServiceImp, times(1)).getOrderDetailsForPayment(orderId);
    }

    @Test
    void testGetOrderDetailsForPayment_Exception() {
        long orderId = 1L;
        when(orderServiceImp.getOrderDetailsForPayment(orderId))
                .thenThrow(new RuntimeException("Payment details not found"));

        assertThrows(RuntimeException.class, () -> orderController.getOrderDetailsForPayment(orderId));
        verify(orderServiceImp, times(1)).getOrderDetailsForPayment(orderId);
    }
*/


    @Test
    void testPayForOrder_Success() {

        Long orderId = 1L;
        String mockResponse = "Payment processed successfully for OrderID: 1";

        when(orderServiceImp.sendOrderForPayment(orderId)).thenReturn(mockResponse);

        ResponseEntity<String> response = orderController.payForOrder(orderId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());

        verify(orderServiceImp, times(1)).sendOrderForPayment(orderId);
    }

    @Test
    void testPayForOrder_Failure() {

        Long orderId = 2L;
        String errorResponse = "Payment failed for OrderID: 2";

        when(orderServiceImp.sendOrderForPayment(orderId)).thenReturn(errorResponse);

        ResponseEntity<String> response = orderController.payForOrder(orderId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(errorResponse, response.getBody());
        verify(orderServiceImp, times(1)).sendOrderForPayment(orderId);
    }

    @Test
    void testPayForOrder_OrderNotFoundException() {

        Long orderId = 99L;

        when(orderServiceImp.sendOrderForPayment(orderId))
                .thenThrow(new OrderIdNotFoundException("OrderID " + orderId + " is Invalid"));

        OrderIdNotFoundException exception = assertThrows(
                OrderIdNotFoundException.class,
                () -> orderController.payForOrder(orderId)
        );

        assertEquals("OrderID 99 is Invalid", exception.getMessage());
        verify(orderServiceImp, times(1)).sendOrderForPayment(orderId);
    }
}
