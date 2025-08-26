package com.notification.service.service;

import com.notification.service.Exception.NotificationException;
import com.notification.service.configuration.InventoryClient;
import com.notification.service.dto.InventoryResponse;
import com.notification.service.dto.NotificationResponse;
import com.notification.service.entity.OrderNotification;
import com.notification.service.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        MockitoAnnotations.openMocks(this);
        notificationService.fromEmail = "test@company.com";
        notificationService.adminEmail = "admin@example.com";
        notificationService.lowInventoryThreshold = 5;
    }


    @Test
    void testNotifyUser_SuccessScenario() {
        // Arrange
        String recipient = "customer@example.com";
        String orderId = "ORD123";
        String status = "SUCCESS";
        String productID = "abc123";
        int quantity = 10;

        OrderNotification savedNotification = new OrderNotification();
        savedNotification.setOrderId(orderId);
        savedNotification.setRecipientEmail(recipient);
        savedNotification.setStatus(status);
        savedNotification.setSentAt(LocalDateTime.now());
        savedNotification.setMessageId(1L);

        when(notificationRepository.save(any(OrderNotification.class))).thenReturn(savedNotification);
        when(inventoryClient.getInventoryByProductId(productID)).thenReturn(new InventoryResponse(productID, 3)); // simulate low stock

        NotificationResponse response = notificationService.notifyUserBasedOnOrderStatus(recipient, orderId, status, productID, quantity);

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals(orderId, response.getOrderId());
    }


    @Test
    void testNotifyUser_FailedScenario() {
        // Arrange
        String recipient = "customer@example.com";
        String orderId = "ORD456";
        String status = "FAILED";
        String productID = "abc123";
        int quantity = 10;

        OrderNotification savedNotification = new OrderNotification();
        savedNotification.setOrderId(orderId);
        savedNotification.setRecipientEmail(recipient);
        savedNotification.setStatus(status);
        savedNotification.setSentAt(LocalDateTime.now());
        savedNotification.setMessageId(2L);

        when(notificationRepository.save(any(OrderNotification.class))).thenReturn(savedNotification);

        NotificationResponse response = notificationService.notifyUserBasedOnOrderStatus(recipient, orderId, status, productID, quantity);

        assertNotNull(response);
        assertEquals("FAILED", response.getStatus());
        assertEquals(orderId, response.getOrderId());
    }

    @Test
    void testNotifyUser_ExceptionWhileSendingEmail() {
        // Arrange
        String recipient = "customer@example.com";
        String orderId = "ORD789";
        String status = "SUCCESS";
        String productID = "abc123";
        int quantity = 10;

        doThrow(new RuntimeException("SMTP server not available"))
                .when(javaMailSender).send(any(SimpleMailMessage.class));

        NotificationException exception = assertThrows(NotificationException.class, () -> {
            notificationService.notifyUserBasedOnOrderStatus(recipient, orderId, status, productID, quantity);
        });

        assertEquals("Failed to send Email", exception.getMessage());
    }
}
