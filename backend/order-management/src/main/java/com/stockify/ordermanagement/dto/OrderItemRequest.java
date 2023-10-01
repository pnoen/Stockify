package com.stockify.ordermanagement.dto;

import java.time.LocalDate;

public class OrderItemRequest {
    private int orderId;
    private int productId;
    private int productVarietyId;
    private int quantitySuffixId;
    private float quantity;
    private LocalDate lastUpdated;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductVarietyId() {
        return productVarietyId;
    }

    public void setProductVarietyId(int productVarietyId) {
        this.productVarietyId = productVarietyId;
    }

    public int getQuantitySuffixId() {
        return quantitySuffixId;
    }

    public void setQuantitySuffixId(int quantitySuffixId) {
        this.quantitySuffixId = quantitySuffixId;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
