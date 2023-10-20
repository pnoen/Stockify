package com.stockify.productservice.dto;

public class EditProductRequest {

    private int id;
    private String name;
    private String description;
    private Integer quantity;
    private float price;
    private String imageUrl;

    public EditProductRequest(int id, String name, String description, Integer quantity, float price, String imageUrl) {
        this.id=id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
