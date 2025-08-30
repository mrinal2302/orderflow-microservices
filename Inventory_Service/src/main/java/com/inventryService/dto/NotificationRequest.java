package com.inventryService.dto;

public class NotificationRequest {
    public  Long productId;
    public String productName;
    public int availableQuantity;

    public NotificationRequest(Long productId, String productName, int availableQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.availableQuantity = availableQuantity;
    }

    public NotificationRequest() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    @Override
    public String toString() {
        return "NotificationRequest{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", availableQuantity='" + availableQuantity + '\'' +
                '}';
    }
}
