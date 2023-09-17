package com.stockify.useraccountservice.dto;

import com.stockify.useraccountservice.Model.User;

import java.util.List;

public class UserIdsResponse {
    private List<User> userIds;
    private int statusCode;

    public UserIdsResponse(int statusCode, List<User> userIds) {
        this.userIds = userIds;
        this.statusCode = statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public List<User> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<User> userIds) {
        this.userIds = userIds;
    }
}