package com.notification.service.service;

import com.notification.service.Exception.NotificationException;
import com.notification.service.configuration.InventoryClient;
import com.notification.service.dto.InventoryResponse;
import com.notification.service.dto.PaymentResponse;
import com.notification.service.entity.OrderNotification;
import com.notification.service.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private InventoryClient inventoryClient;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(notificationService, "lowInventoryThreshold", 5);
        ReflectionTestUtils.setField(notificationService, "adminEmail", "admin@example.com");
        ReflectionTestUtils.setField(notificationService, "fromEmail", "noreply@example.com");
    }

    @Test
    void notifyUserBasedOnOrderStatus_success() throws Exception {
        // Given
        String orderId = "ORD123";
        String recipient = "user@example.com";
        String status = "SUCCESS";
        long productId = 101L;
        int quantity = 2;

        OrderNotification savedNotification = new OrderNotification();
        savedNotification.setOrderId(orderId);
        savedNotification.setRecipientEmail(recipient);
        savedNotification.setStatus(status);
        savedNotification.setSentAt(LocalDateTime.now());
        savedNotification.setMessage("Success message");
        savedNotification.setMessageId(123L);

        Mockito.when(notificationRepository.save(Mockito.any(OrderNotification.class)))
                .thenReturn(savedNotification);

        // When
        PaymentResponse response = notificationService.notifyUserBasedOnOrderStatus(
                recipient, orderId, status, productId, quantity);

        // Then
        assertEquals("SUCCESS", response.getStatus());
        assertEquals(orderId, response.getOrderId());
        assertNotNull(response.getSentAt());
        assertTrue(response.getMessage().contains("processed successfully"));
    }

    @Test
    void notifyUserBasedOnOrderStatus_failedPayment() throws Exception {
        // Given
        String orderId = "ORD456";
        String recipient = "user@example.com";
        String status = "FAILED";
        long productId = 101L;
        int quantity = 2;

        OrderNotification savedNotification = new OrderNotification();
        savedNotification.setOrderId(orderId);
        savedNotification.setRecipientEmail(recipient);
        savedNotification.setStatus(status);
        savedNotification.setSentAt(LocalDateTime.now());
        savedNotification.setMessage("Payment failed message");
        savedNotification.setMessageId(123L);

        Mockito.when(notificationRepository.save(Mockito.any(OrderNotification.class)))
                .thenReturn(savedNotification);

        // When
        PaymentResponse response = notificationService.notifyUserBasedOnOrderStatus(
                recipient, orderId, status, productId, quantity);

        // Then
        assertEquals("FAILED", response.getStatus());
        assertTrue(response.getMessage().contains("payment failed"));
    }

    @Test
    void notifyUserBasedOnOrderStatus_emailException() {
        // Given
        String orderId = "ORD789";
        String recipient = "user@example.com";
        String status = "SUCCESS";
        long productId = 101L;
        int quantity = 2;

        // Simulate email send failure
        Mockito.doThrow(new RuntimeException("SMTP Error"))
                .when(javaMailSender).send(Mockito.any(SimpleMailMessage.class));

        // Then
        assertThrows(NotificationException.class, () -> {
            notificationService.notifyUserBasedOnOrderStatus(recipient, orderId, status, productId, quantity);
        });
    }

    @Test
    void getStockAvailability_lowStock_sendsAlert() {
        // Given
        long productId = 100L;
        InventoryResponse inventoryResponse = new InventoryResponse();
        inventoryResponse.setAvailableStock(2);  // Threshold is 5

        Mockito.when(inventoryClient.getInventoryByProductId(productId))
                .thenReturn(inventoryResponse);

        // When
        String result = notificationService.getStockAvailability(productId);

        // Then
        assertEquals("Stock is sufficient. No alert needed.", result);
        Mockito.verify(javaMailSender, Mockito.times(1)).send(Mockito.any(SimpleMailMessage.class));
    }

    @Test
    void getStockAvailability_sufficientStock_noAlert() {
        // Given
        long productId = 101L;
        InventoryResponse inventoryResponse = new InventoryResponse();
        inventoryResponse.setAvailableStock(10);  // Above threshold

        Mockito.when(inventoryClient.getInventoryByProductId(productId))
                .thenReturn(inventoryResponse);

        // When
        String result = notificationService.getStockAvailability(productId);

        // Then
        assertEquals("Stock is sufficient. No alert needed.", result);
        Mockito.verify(javaMailSender, Mockito.never()).send(Mockito.any(SimpleMailMessage.class));
    }
}
