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
    private int productVarietyId;
    private int quantitySuffixId;
    private float quantity;
    private LocalDate lastUpdated;
    private double price;

    public int getId() {
        return id;
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

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price;}
}