package com.project.agroworld.ui.shopping.model;

import java.io.Serializable;

public class ProductModel implements Serializable {
    private String title;
    private String description;
    private double price;
    private String imageUrl;

    public ProductModel(){

    }

    public ProductModel(String title, String description, double price, String imageUrl) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {return price;}

    public String getImageUrl() {
        return imageUrl;
    }
}
