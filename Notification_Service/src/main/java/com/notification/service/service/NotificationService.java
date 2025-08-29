package com.notification.service.service;

import com.notification.service.Exception.DataInsufficientException;
import com.notification.service.Exception.NotificationException;
import com.notification.service.configuration.InventoryClient;
import com.notification.service.dto.InventoryResponse;
import com.notification.service.dto.PaymentResponse;
import com.notification.service.entity.OrderNotification;
import com.notification.service.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
@Slf4j
public class NotificationService {

    @Value("${inventory.low.threshold}")
    public int lowInventoryThreshold;

    @Value("${admin.email}")
    public String adminEmail;

    @Value("${spring.mail.username}")
    public String fromEmail;
    private NotificationRepository notificationRepository;

    private JavaMailSender javaMailSender;

    private InventoryClient inventoryClient;

    public NotificationService(NotificationRepository notificationRepository, JavaMailSender javaMailSender, InventoryClient inventoryClient) {
        this.notificationRepository = notificationRepository;
        this.javaMailSender = javaMailSender;
        this.inventoryClient = inventoryClient;
    }
    //To give response to the users on placing order with there status
    public PaymentResponse notifyUserBasedOnOrderStatus(String recipient, String orderId, String status, long productId, int quantity) throws NotificationException {
        String subject = "Order Status Notification: " + orderId;
        String message;
        if ("SUCCESS".equalsIgnoreCase(status)) {
            message = "Dear customer,\n\nYour order " + orderId + " has been processed successfully!\n\nThank you for your purchase.";
        } else {
            message = "Dear customer,\n\nUnfortunately, your order " + orderId + " payment failed. Please try again or contact support.";
        }
        try {
            sendEmail(recipient, subject, message);
        } catch (Exception ex) {
            throw new NotificationException("Failed to send Email");
        }
        OrderNotification notification = new OrderNotification();
        notification.setOrderId(orderId);
        notification.setRecipientEmail(recipient);
        notification.setStatus(status);
        notification.setSentAt(LocalDateTime.now());
        notification.setMessage(message);

        notificationRepository.save(notification);

        String sentAtStr = notification.getSentAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return new PaymentResponse(
                notification.getStatus(),
                sentAtStr,
                notification.getMessageId(),
                notification.getOrderId(),
                notification.getMessage()
        );
    }


    public String getStockAvailability(Long productId) {
        InventoryResponse response = inventoryClient.getInventoryByProductId(productId);
       if( response!= null && response.getAvailableStock() <= lowInventoryThreshold){
           String subject = "Low Stock Alert for Product " + productId;;
           String message= "Product ID " + productId + " has low stock. Current stock: " + response.getAvailableStock();
           sendEmail(adminEmail, subject, message);
       }
        return "Stock is sufficient. No alert needed.";
    }

    public void sendEmail(String recipient, String subject, String body) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(fromEmail);
        mail.setTo(recipient);
        mail.setSubject(subject);
        mail.setText(body);
        javaMailSender.send(mail);
    }

}
