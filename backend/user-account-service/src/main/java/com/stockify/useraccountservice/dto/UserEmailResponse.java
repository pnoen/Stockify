package com.stockify.useraccountservice.dto;

public class UserEmailResponse {
    private String email;
    private int statusCode;

    public UserEmailResponse() {
    }

    public UserEmailResponse(int statusCode, String email) {
        this.email = email;
        this.statusCode = statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}