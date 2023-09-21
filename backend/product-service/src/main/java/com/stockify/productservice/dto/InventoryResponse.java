package com.stockify.productservice.dto;

import java.util.List;

public class InventoryResponse {

    private List<Inventory> inventory;
    private int statusCode;

    public InventoryResponse(int statusCode, List<Inventory> inventory) {
        this.inventory = inventory;
        this.statusCode = statusCode;
    }

    public List<Inventory> getInventory() {
        return inventory;
    }

    public void setInventory(List<Inventory> inventory) {
        this.inventory = inventory;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
