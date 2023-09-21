package com.stockify.useraccountservice.dto;

import java.util.List;

public class BusinessesResponse {
    private List<BusinessDto> businesses;
    private int statusCode;

    public BusinessesResponse(int statusCode, List<BusinessDto> businesses) {
        this.businesses = businesses;
        this.statusCode = statusCode;
    }

    public List<BusinessDto> getBusinesses() {
        return businesses;
    }

    public void setBusinessNames(List<BusinessDto> businesses) {
        this.businesses = businesses;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
