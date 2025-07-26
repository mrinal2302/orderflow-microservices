package com.notification.service.service;


import static org.junit.jupiter.api.Assertions.*;

import com.notification.service.dto.NotificationRequest;
import com.notification.service.dto.NotificationResponse;

import com.notification.service.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    public void testCreateNotification() {
        MockitoAnnotations.openMocks(this);

        // Arrange
        NotificationRequest request = new NotificationRequest();
        request.setMessageId("MSG002");
        request.setRecipient("test@example.com");
        request.setMessage("Test message");
        request.setStatus("SENT");

        // Act
        NotificationResponse response = notificationService.createNotification(request);

        // Assert
        assertEquals("SENT", response.getStatus());
        assertEquals("MSG002", response.getMessageId());

    }
}