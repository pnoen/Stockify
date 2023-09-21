package com.stockify.usermanagementservice.dto;

public class ApiCallResponse {
    private int statusCode;
    private String message;
    private String userToken;

    public ApiCallResponse() {

    }

    public ApiCallResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}