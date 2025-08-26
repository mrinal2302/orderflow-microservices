package com.notification.service.dto;

public class InventoryResponse {


    private int productId;
    private int availableStock ;

    public InventoryResponse(String productID, int i) {
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }
}
