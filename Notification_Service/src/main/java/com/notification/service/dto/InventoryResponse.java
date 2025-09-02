package com.notification.service.dto;

public class InventoryResponse {

    private long productId;
    private int availableStock ;

    public InventoryResponse(long productID, int i) {
    }

    public InventoryResponse() {

    }

    public long getProductId() {
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
