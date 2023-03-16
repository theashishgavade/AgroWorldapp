package com.project.agroworld.transport.model;

public class VehicleModel {
    private String model;
    private String address;
    private String rates;
    private String contact;
    private String imageUrl;

    public VehicleModel(){

    }

    public VehicleModel(String model, String address, String rates, String contact, String imageUrl) {
        this.model = model;
        this.address = address;
        this.rates = rates;
        this.contact = contact;
        this.imageUrl = imageUrl;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public String getContact() {
        return contact;
    }

    public String getRates() {
        return rates;
    }

    public String getAddress() {
        return address;
    }

    public String getModel() {
        return model;
    }
}
