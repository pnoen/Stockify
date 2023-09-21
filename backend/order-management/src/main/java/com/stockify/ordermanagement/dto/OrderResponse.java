package com.stockify.ordermanagement.dto;

import com.stockify.ordermanagement.model.Order;

import java.util.List;

public class OrderResponse {
    private Order order;
    private int statusCode;

    public OrderResponse(int statusCode, Order order) {
        this.order = order;
        this.statusCode = statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}