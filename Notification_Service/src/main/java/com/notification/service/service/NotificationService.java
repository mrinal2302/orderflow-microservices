package com.notification.service.service;

import com.notification.service.entity.OrderNotification;
import com.notification.service.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private NotificationRepository notificationRepository;

    public void sendOrderNotification(String recipientEmail, String orderId, boolean isSuccess) {
        String subject = "Order Notification for Order ID: " + orderId;
        LocalDateTime now = LocalDateTime.now();
        String status = isSuccess ? "SUCCESS" : "FAIL";
        String message = isSuccess
                ? "Your order has been placed successfully!"
                : "Unfortunately, your order placement has failed.";

        // Create and save notification record
        OrderNotification notification = new OrderNotification();
        notification.setRecipientEmail(recipientEmail);
        notification.setOrderId(orderId);
        notification.setSentAt(now);
        notification.setStatus(status);
        notification.setMessage(message);
        notificationRepository.save(notification);
        //  System.out.println("hello");

        // Send email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(
                "Order ID: " + orderId + "\n"
                        + "Date/Time: " + now + "\n"
                        + "Status: " + message
        );
        mailSender.send(mailMessage);
    }
}

