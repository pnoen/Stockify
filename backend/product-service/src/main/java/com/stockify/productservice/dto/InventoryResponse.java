package com.stockify.productservice.dto;
import com.stockify.productservice.model.Product;

import java.util.List;

public class InventoryResponse {

    private List<Product> product;
    private int statusCode;

    public InventoryResponse(int statusCode, List<Product> product) {
        this.product = product;
        this.statusCode = statusCode;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
