package com.stockify.productservice.dto;

import com.stockify.productservice.model.Product;

import java.util.List;

public class ProductListSpecificResponse {

    private int statusCode;
    private List<Product> products;

    public ProductListSpecificResponse(int statusCode, List<Product> products) {
        this.statusCode = statusCode;
        this.products = products;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
