package com.inventryService.entity;

import jakarta.persistence.*;

@Entity
public class InventoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private int availableQuantity;
    private Double price;
    private String description;

    public InventoryEntity(Long productId, String productName, int availableQuantity, Double price,String description) {
        this.productId = productId;
        this.productName = productName;
        this.availableQuantity = availableQuantity;
        this.price = price;
        this.description=description;
    }

    public InventoryEntity() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
