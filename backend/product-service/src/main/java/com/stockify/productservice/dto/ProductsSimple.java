package com.stockify.productservice.dto;

public class ProductsSimple {

    private String name;
    private float price;
    private String companyName;

    public ProductsSimple(String name, float price, String companyName) {
        this.name = name;
        this.price = price;
        this.companyName = companyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
