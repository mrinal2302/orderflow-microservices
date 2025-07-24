package com.notification.service.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateNotification() throws Exception {
        String jsonPayload = "{\n" +
                "  \"messageId\": \"MSG003\",\n" +
                "  \"recipient\": \"user@test.com\",\n" +
                "  \"message\": \"Order received\",\n" +
                "  \"status\": \"SENT\"\n" +
                "}";

        mockMvc.perform(post("/api/notify/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SENT"))
                .andExpect(jsonPath("$.messageId").value("MSG003"));


    }
}