package com.stockify.useraccountservice.dto;

public class BusinessDetailsResponse {
    private int statusCode;
    private String businessName;
    private int businessCode;

    public BusinessDetailsResponse(int statusCode, String businessName, int businessCode) {
        this.statusCode = statusCode;
        this.businessName = businessName;
        this.businessCode = businessCode;
    }

    public BusinessDetailsResponse() {
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public int getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(int businessCode) {
        this.businessCode = businessCode;
    }
}
