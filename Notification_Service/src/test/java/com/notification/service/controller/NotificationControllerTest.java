package com.notification.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.service.Exception.NotificationException;
import com.notification.service.dto.NotificationRequest;
import com.notification.service.dto.PaymentResponse;
import com.notification.service.service.NotificationService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
@ExtendWith(SpringExtension.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    // --- POST /email success ---
    @Test
    void notifyAndSaveOrderStatus_success() throws Exception {
        NotificationRequest request = new NotificationRequest();
        request.setRecipient("user@example.com");
        request.setOrderId("ORD001");
        request.setStatus("SUCCESS");
        request.setProductId(101L);
        request.setQuantity(2);

        PaymentResponse response = new PaymentResponse(
                "SUCCESS",
                LocalDateTime.now().toString(),
                123l,
                "ORD001",
                "Your order ORD001 has been processed successfully!"
        );

        Mockito.when(notificationService.notifyUserBasedOnOrderStatus(
                        Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt()))
                .thenReturn(response);

        mockMvc.perform(post("/api/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.orderId").value("ORD001"))
                .andExpect(jsonPath("$.message").value("Your order ORD001 has been processed successfully!"));
    }

    // --- POST /email failure scenario ---
    // Assuming failure means order status is FAILED and still returns 201 with different message
    @Test
    void notifyAndSaveOrderStatus_failedStatus() throws Exception {
        NotificationRequest request = new NotificationRequest();
        request.setRecipient("user@example.com");
        request.setOrderId("ORD002");
        request.setStatus("FAILED");
        request.setProductId(102L);
        request.setQuantity(1);

        PaymentResponse response = new PaymentResponse(
                "FAILED",
                LocalDateTime.now().toString(),
                123l,
                "ORD002",
                "Unfortunately, your order ORD002 payment failed. Please try again."
        );

        Mockito.when(notificationService.notifyUserBasedOnOrderStatus(
                        Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt()))
                .thenReturn(response);

        mockMvc.perform(post("/api/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andExpect(jsonPath("$.orderId").value("ORD002"))
                .andExpect(jsonPath("$.message").value("Unfortunately, your order ORD002 payment failed. Please try again."));
    }

    // --- POST /email exception scenario ---
    @Test
    void notifyAndSaveOrderStatus_serviceThrowsException() throws Exception {
        NotificationRequest request = new NotificationRequest();
        request.setRecipient("user@example.com");
        request.setOrderId("ORD003");
        request.setStatus("SUCCESS");
        request.setProductId(103L);
        request.setQuantity(5);

        Mockito.when(notificationService.notifyUserBasedOnOrderStatus(
                        Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt()))
                .thenThrow(new NotificationException("Service failure"));

        mockMvc.perform(post("/api/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // Since controller does not catch exception explicitly, expect 500
                .andExpect(status().isInternalServerError());
    }

    // --- GET /check-stock/{productId} success ---
    @Test
    void checkTheStockAvailability_success() throws Exception {
        long productId = 100L;
        String expectedResponse = "Stock is sufficient. No alert needed.";

        Mockito.when(notificationService.getStockAvailability(productId))
                .thenReturn(expectedResponse);

        mockMvc.perform(get("/api/check-stock/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }
}
