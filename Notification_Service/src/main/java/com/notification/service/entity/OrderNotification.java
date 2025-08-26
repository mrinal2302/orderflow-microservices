package com.notification.service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "OrderNotificationTable")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long messageId;
    private String orderId;
    private String recipientEmail;
    private String message;
    private LocalDateTime sentAt;
    private String status;
}