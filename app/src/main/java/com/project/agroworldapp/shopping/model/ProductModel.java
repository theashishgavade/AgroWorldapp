package com.project.agroworldapp.shopping.model;

import java.io.Serializable;

public class ProductModel implements Serializable {
    private String title;
    private String description;
    private double price;
    private String imageUrl;

    private int quantity = 1;

    public ProductModel() {

    }

    public ProductModel(String title, String description, double price, String imageUrl, int quantity) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
