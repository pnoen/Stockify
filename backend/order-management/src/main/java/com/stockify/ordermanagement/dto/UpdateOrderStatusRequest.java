package com.stockify.ordermanagement.dto;

import com.stockify.ordermanagement.constants.OrderStatus;

public class UpdateOrderStatusRequest {
    private int orderId;
    private OrderStatus orderStatus;


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
