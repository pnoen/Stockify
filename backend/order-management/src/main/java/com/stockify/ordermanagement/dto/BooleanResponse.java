package com.stockify.ordermanagement.dto;

public class BooleanResponse {
    private int statusCode;
    private boolean success;
    private String userToken;

    public BooleanResponse() {

    }

    public BooleanResponse(int statusCode, boolean success) {
        this.statusCode = statusCode;
        this.success = success;
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

    public boolean getSuccess() {
        return success;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}