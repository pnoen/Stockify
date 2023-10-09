package com.stockify.invoiceservice.dto;

import com.stockify.invoiceservice.model.User;

public class UserResponse {
    private User user;
    private int statusCode;

    public UserResponse() {
    }

    public UserResponse(int statusCode, User user) {
        this.user = user;
        this.statusCode = statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}