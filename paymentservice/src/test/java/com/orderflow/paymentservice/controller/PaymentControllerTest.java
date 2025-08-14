package com.orderflow.paymentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderflow.paymentservice.entity.PaymentEntity;
import com.orderflow.paymentservice.model.PaymentMethod;
import com.orderflow.paymentservice.model.PaymentStatus;
import com.orderflow.paymentservice.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetPaymentByOrderId() throws Exception {
        PaymentEntity entity = new PaymentEntity();
        entity.setOrderId(1L);
        entity.setAmount(1000.0);
        entity.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        entity.setEmailAddress("test@ok.com");
        entity.setPaymentStatus(PaymentStatus.SUCCESS);

        Mockito.when(paymentService.getPaymentByOrderId(anyLong())).thenReturn(entity);

        mockMvc.perform(get("/payment/getPaymentByOrderId/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.amount").value(1000.0))
                .andExpect(jsonPath("$.paymentMethod").value("CREDIT_CARD"))
                .andExpect(jsonPath("$.emailAddress").value("test@ok.com"))
                .andExpect(jsonPath("$.paymentStatus").value("SUCCESS"));
    }

    @Test
    void testGetAllPayments() throws Exception {
        PaymentEntity entity1 = new PaymentEntity();
        entity1.setOrderId(1L);
        entity1.setAmount(500.0);
        entity1.setPaymentMethod(PaymentMethod.DEBIT_CARD);
        entity1.setEmailAddress("a@ok.com");
        entity1.setPaymentStatus(PaymentStatus.SUCCESS);

        PaymentEntity entity2 = new PaymentEntity();
        entity2.setOrderId(2L);
        entity2.setAmount(1500.0);
        entity2.setPaymentMethod(PaymentMethod.UPI);
        entity2.setEmailAddress("b@ok.com");
        entity2.setPaymentStatus(PaymentStatus.PENDING);

        List<PaymentEntity> list = Arrays.asList(entity1, entity2);

        Mockito.when(paymentService.getAllPaymentDetails()).thenReturn(list);

        mockMvc.perform(get("/payment/getAllData")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].orderId").value(1))
                .andExpect(jsonPath("$[1].orderId").value(2));
    }

    @Test
    void testSavePaymentData() throws Exception {
        PaymentEntity entity = new PaymentEntity();
        entity.setOrderId(10L);
        entity.setAmount(250.0);
        entity.setPaymentMethod(PaymentMethod.UPI);
        entity.setEmailAddress("save@ok.com");
        entity.setPaymentStatus(PaymentStatus.SUCCESS);

        Mockito.doNothing().when(paymentService).savePaymentData(any(PaymentEntity.class));

        mockMvc.perform(post("/payment/savePaymentData")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entity)))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment data saved successfully"));
    }
}