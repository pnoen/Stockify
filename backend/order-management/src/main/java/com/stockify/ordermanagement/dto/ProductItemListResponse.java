package com.stockify.ordermanagement.dto;

import com.stockify.ordermanagement.model.ProductItem;

import java.util.List;

public class ProductItemListResponse {

    private int statusCode;
    private List<ProductItem> products;

    public ProductItemListResponse() {
    }

    public ProductItemListResponse(int statusCode, List<ProductItem> products) {
        this.statusCode = statusCode;
        this.products = products;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<ProductItem> getProducts() {
        return products;
    }

    public void setProducts(List<ProductItem> products) {
        this.products = products;
    }
}
