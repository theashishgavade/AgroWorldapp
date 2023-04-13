package com.project.agroworldapp.transport.model;

import java.io.Serializable;

public class VehicleModel implements Serializable {
    private String model;
    private String address;
    private String rates;
    private String unit;
    private String contact;
    private String imageUrl;

    public VehicleModel() {

    }

    public VehicleModel(String model, String address, String rates, String unit, String contact, String imageUrl) {
        this.model = model;
        this.address = address;
        this.rates = rates;
        this.unit = unit;
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

    public String getUnit() {
        return unit;
    }

    public String getAddress() {
        return address;
    }

    public String getModel() {
        return model;
    }
}
