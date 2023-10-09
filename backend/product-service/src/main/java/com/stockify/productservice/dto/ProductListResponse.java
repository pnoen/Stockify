package com.stockify.productservice.dto;

import java.util.List;

public class ProductListResponse {
    private List<ProductsSimple> products;
    private int statusCode;

    public ProductListResponse(int statusCode, List<ProductsSimple> products) {
        this.products = products;
        this.statusCode = statusCode;
    }

    public List<ProductsSimple> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsSimple> products) {
        this.products = products;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
