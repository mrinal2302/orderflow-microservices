package com.orderflow.paymentservice.service;

import com.orderflow.paymentservice.entity.PaymentEntity;
import com.orderflow.paymentservice.exceptionHandler.OrderIdNotFoundException;
import com.orderflow.paymentservice.exceptionHandler.PaymentNotFound;
import com.orderflow.paymentservice.model.PaymentMethod;
import com.orderflow.paymentservice.model.PaymentStatus;
import com.orderflow.paymentservice.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void savePaymentData_success() {
        PaymentEntity entity = new PaymentEntity();
        entity.setOrderId(1L);
        entity.setAmount(1000.0);
        entity.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        entity.setPaymentStatus(PaymentStatus.SUCCESS);
        entity.setEmailAddress("success@ok.com");

        when(paymentRepository.save(any(PaymentEntity.class))).thenReturn(entity);

        assertDoesNotThrow(() -> paymentService.savePaymentData(entity));
        verify(paymentRepository, times(1)).save(any(PaymentEntity.class));
    }

    @Test
    void savePaymentData_failure_throwsOrderIdNotFoundException() {
        PaymentEntity entity = new PaymentEntity();
        entity.setOrderId(1L);
        entity.setAmount(1000.0);
        entity.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        entity.setPaymentStatus(PaymentStatus.SUCCESS);
        entity.setEmailAddress("fail@ok.com");

        when(paymentRepository.save(any(PaymentEntity.class))).thenThrow(new RuntimeException("db"));

        OrderIdNotFoundException ex = assertThrows(OrderIdNotFoundException.class, () -> paymentService.savePaymentData(entity));
        assertTrue(ex.getMessage().contains("failed to save data"));
    }

    @Test
    void updatePaymentDetails_success() {
        Long orderId = 10L;
        PaymentEntity existing = new PaymentEntity();
        existing.setOrderId(orderId);
        existing.setAmount(100.0);
        existing.setPaymentMethod(PaymentMethod.DEBIT_CARD);
        existing.setPaymentStatus(PaymentStatus.PENDING);
        existing.setEmailAddress("old@ok.com");

        PaymentEntity updates = new PaymentEntity();
        updates.setAmount(200.0);
        updates.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        updates.setPaymentStatus(PaymentStatus.SUCCESS);

        when(paymentRepository.findByOrderId(orderId)).thenReturn(existing);
        when(paymentRepository.save(any(PaymentEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        PaymentEntity saved = paymentService.updatePaymentDetails(updates, orderId);
        assertEquals(200.0, saved.getAmount());
        assertEquals(PaymentMethod.CREDIT_CARD, saved.getPaymentMethod());
        assertEquals(PaymentStatus.SUCCESS, saved.getPaymentStatus());
        verify(paymentRepository).save(existing);
    }

    @Test
    void updatePaymentDetails_notFound_throwsOrderIdNotFoundException() {
        Long orderId = 99L;
        when(paymentRepository.findByOrderId(orderId)).thenReturn(null);
        PaymentEntity updates = new PaymentEntity();
        OrderIdNotFoundException ex = assertThrows(OrderIdNotFoundException.class, () -> paymentService.updatePaymentDetails(updates, orderId));
        assertTrue(ex.getMessage().contains("Payment not found for Order ID"));
    }

    @Test
    void getAllPaymentDetails_success() {
        PaymentEntity e1 = new PaymentEntity();
        PaymentEntity e2 = new PaymentEntity();
        when(paymentRepository.findAll()).thenReturn(Arrays.asList(e1, e2));
        List<PaymentEntity> out = paymentService.getAllPaymentDetails();
        assertEquals(2, out.size());
        verify(paymentRepository).findAll();
    }

    @Test
    void getAllPaymentDetails_failure_throwsPaymentNotFound() {
        when(paymentRepository.findAll()).thenThrow(new RuntimeException("db"));
        PaymentNotFound ex = assertThrows(PaymentNotFound.class, () -> paymentService.getAllPaymentDetails());
        assertTrue(ex.getMessage().contains("failed to get payment details"));
    }

    @Test
    void deleteByOrderId_success() {
        assertDoesNotThrow(() -> paymentService.deleteByOrderId(1L));
        verify(paymentRepository).deleteById(1L);
    }

    @Test
    void deleteByOrderId_failure_throwsOrderIdNotFoundException() {
        doThrow(new RuntimeException("db")).when(paymentRepository).deleteById(anyLong());
        OrderIdNotFoundException ex = assertThrows(OrderIdNotFoundException.class, () -> paymentService.deleteByOrderId(1L));
        assertTrue(ex.getMessage().contains("failed to delete"));
    }

    @Test
    void getPaymentByOrderId_success() {
        PaymentEntity entity = new PaymentEntity();
        entity.setOrderId(42L);
        when(paymentRepository.findByOrderId(42L)).thenReturn(entity);
        PaymentEntity out = paymentService.getPaymentByOrderId(42L);
        assertNotNull(out);
        assertEquals(42L, out.getOrderId());
    }

    @Test
    void getPaymentByOrderId_notFound_throwsPaymentNotFound() {
        when(paymentRepository.findByOrderId(55L)).thenReturn(null);
        PaymentNotFound ex = assertThrows(PaymentNotFound.class, () -> paymentService.getPaymentByOrderId(55L));
        assertTrue(ex.getMessage().contains("Payment not found for Order ID"));
    }

    @Test
    void sendNotify_success_buildsNotifyStatus() {
        PaymentEntity entity = new PaymentEntity();
        entity.setOrderId(5L);
        entity.setEmailAddress("a@b.com");
        entity.setPaymentStatus(PaymentStatus.SUCCESS);
        when(paymentRepository.findByOrderId(5L)).thenReturn(entity);

        var notify = paymentService.sendNotify(5L);
        assertEquals(5L, notify.getOrderId());
        assertEquals("a@b.com", notify.getEmailAddress());
        assertEquals("SUCCESS", notify.getPaymentStatus());
    }

    @Test
    void sendNotify_missingFields_returnsNotifyWithNulls() {
        PaymentEntity entity = new PaymentEntity();
        entity.setOrderId(6L);
        // emailAddress and paymentStatus are null by default
        when(paymentRepository.findByOrderId(6L)).thenReturn(entity);

        var notify = assertDoesNotThrow(() -> paymentService.sendNotify(6L));
        assertNotNull(notify);
        assertEquals(6L, notify.getOrderId());
        assertNull(notify.getEmailAddress());
        // String.valueOf(null) -> "null"
        assertEquals("null", notify.getPaymentStatus());
    }
}
