package com.stockify.ordermanagement.dto;

import com.stockify.ordermanagement.model.Order;

import java.util.List;

public class OrderListResponse {
    private List<Order> orderList;
    private int statusCode;

    public OrderListResponse(int statusCode, List<Order> orderList) {
        this.orderList = orderList;
        this.statusCode = statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrder(List<Order> orderList) {
        this.orderList = orderList;
    }
}