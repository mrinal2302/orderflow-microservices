package com.notification.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class NotificationRequest {


    @NotBlank(message = "Order ID is required")
    private String orderId;
    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid email format")
    private String recipient;
    @NotBlank(message = "Order status is required")
    private String status;

    private long productId;

    private int quantity ;

    public NotificationRequest(String orderId, String recipient, String status, long productId, int quantity) {
        this.orderId = orderId;
        this.recipient = recipient;
        this.status = status;
        this.productId = productId;
        this.quantity = quantity;
    }

    public NotificationRequest() {
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}


