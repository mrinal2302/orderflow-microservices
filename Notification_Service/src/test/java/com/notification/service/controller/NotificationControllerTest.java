package com.notification.service.controller;
import com.notification.service.dto.NotificationResponse;
import com.notification.service.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDateTime;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {


    @InjectMocks
    private NotificationController notificationController;

    @Mock
    private NotificationService notificationService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
    }

    @Test
    void notifySaveOrderStatus_success_returnsCreated() throws Exception {
        String recipient = "customer@example.com";
        String orderId = "ORD-001";
        String status = "SUCCESS";
        String productID ="abc123";
        int quantity = 10;
        NotificationResponse response = new NotificationResponse(
                status,
                LocalDateTime.now().toString(),
                1,
                orderId,
                "Your order has been processed successfully!"
        );

        when(notificationService.notifyUserBasedOnOrderStatus(recipient, orderId, status, productID, quantity))
                .thenReturn(response);

        String payload = """
            {
                "recipient": "%s",
                "orderId": "%s",
                "status": "%s"
            }
            """.formatted(recipient, orderId, status);

        mockMvc.perform(post("/api/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.messageId").value(1));
    }

    @Test
    @DisplayName("Not-success notification -> HTTP 201 Created")
    void notifySaveOrderStatus_notSuccess_returnsCreated() throws Exception {
        String recipient = "customer@example.com";
        String orderId = "ORD-002";
        String status = "FAILED";
        String productID ="abc123";
        int quantity = 10;
        NotificationResponse response = new NotificationResponse(
                status,
                LocalDateTime.now().toString(),
                2,
                orderId,
                "Dear customer,... payment failed."
        );

        when(notificationService.notifyUserBasedOnOrderStatus(recipient, orderId, status, productID, quantity))
                .thenReturn(response);

        String payload = String.format("""
        {
            "recipient": "%s",
            "orderId": "%s",
            "status": "%s"
        }
        """, recipient, orderId, status);

        mockMvc.perform(post("/api/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.message").value("Dear customer,... payment failed."));
    }
    @Test
    @DisplayName("Validation failure -> HTTP 400")
    void notifySaveOrderStatus_validationFailure_returnsBadRequest() throws Exception {
        String payload = """
        {
            "orderId": "",
            "recipient": "invalid-email",
            "status": ""
        }
        """;

        mockMvc.perform(post("/api/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }



}
