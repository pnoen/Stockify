package com.stockify.useraccountservice.dto;

import com.stockify.useraccountservice.Model.User;

import java.util.List;

public class UserIdsResponse {
    private List<User> users;
    private int statusCode;

    public UserIdsResponse(int statusCode, List<User> users) {
        this.users = users;
        this.statusCode = statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}