package com.stockify.productservice.dto;

public class GetProductResponse {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private int statusCode;
    private String name;
    private String description;
    private int quantity;
    private float price;
    private String company;

    public GetProductResponse(int statusCode, String message){
        this.statusCode = statusCode;
        this.message = message;
    }

    public GetProductResponse(int statusCode, String name, String description, int quantity, float price, String company) {
        this.statusCode = statusCode;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.company = company;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
