package com.stockify.ordermanagement.dto;

import com.stockify.ordermanagement.model.OrderItem;

import java.util.List;

public class OrderItemListResponse {
    private List<OrderItem> orderItemList;
    private int statusCode;

    public OrderItemListResponse(int statusCode, List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
        this.statusCode = statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public List<OrderItem> getOrderItem() {
        return orderItemList;
    }

    public void setOrderItem(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}