package com.stockify.useraccountservice.dto;

public class BusinessDto {

    private int businessCode;
    private String businessName;

    public BusinessDto(int businessCode, String businessName) {
        this.businessCode = businessCode;
        this.businessName = businessName;
    }

    public int getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(int businessCode) {
        this.businessCode = businessCode;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
}
