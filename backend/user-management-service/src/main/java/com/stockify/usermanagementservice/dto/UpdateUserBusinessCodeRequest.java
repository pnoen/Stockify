package com.stockify.usermanagementservice.dto;

public class UpdateUserBusinessCodeRequest {
    private String email;
    private int businessCode;

    public UpdateUserBusinessCodeRequest(String email, int businessCode) {
        this.email = email;
        this.businessCode = businessCode;
    }

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
