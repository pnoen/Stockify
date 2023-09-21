package com.stockify.usermanagementservice.dto;


import java.util.List;

public class UserIdResponse {
    private int statusCode;
    private List<UserDetailsDto> users;

    public UserIdResponse() {
    }

    public UserIdResponse(int statusCode, List<UserDetailsDto> users) {
        this.statusCode = statusCode;
        this.users = users;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setUsers(List<UserDetailsDto> users) {
        this.users = users;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public List<UserDetailsDto> getUsers() {
        return users;
    }
}
