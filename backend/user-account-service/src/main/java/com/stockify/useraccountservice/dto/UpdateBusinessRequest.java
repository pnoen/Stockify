package com.stockify.useraccountservice.dto;


public class UpdateBusinessRequest {
    private String email;
    private int businessCode;


    public void setEmail(String email) {
        this.email = email;
    }

    public void setBusinessCode(int businessCode) {
        this.businessCode = businessCode;
    }

    public String getEmail() {
        return email;
    }

    public int getBusinessCode() {
        return businessCode;
    }
}
