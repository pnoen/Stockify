package com.stockify.ordermanagement.dto;

public class OrderCostUpdateRequest {
    private int orderId;
    private double price;

    public OrderCostUpdateRequest(int orderId, double price) {
        this.orderId = orderId;
        this.price = price;
    }

    public int getOrderId() { return orderId; }

    public void setOrderId(int orderId) { this.orderId = orderId; }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
