package com.stockify.ordermanagement.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private int orderId;
    private int productId;
    private int businessCode;
    private int quantity;
    private LocalDate lastUpdated;
    private double price;

    public int getId() {
        return id;
    }

    public int getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(int businessCode) {
        this.businessCode = businessCode;
    }

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price;}
}