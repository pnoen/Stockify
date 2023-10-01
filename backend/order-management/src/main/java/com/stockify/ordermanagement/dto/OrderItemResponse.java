package com.stockify.ordermanagement.dto;

import com.stockify.ordermanagement.model.OrderItem;

import java.util.List;

public class OrderItemResponse {
    private OrderItem orderItem;
    private int statusCode;

    public OrderItemResponse(int statusCode, OrderItem orderItem) {
        this.orderItem = orderItem;
        this.statusCode = statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
}