package com.project.agroworld.weatherAPI.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Sys implements Serializable {

    @SerializedName("type")
    private int type;

    @SerializedName("id")
    private int id;

    @SerializedName("country")
    private String country;

    @SerializedName("sunrise")
    private int sunrise;

    @SerializedName("sunset")
    private int sunset;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }

    public int getSunrise() {
        return sunrise;
    }

    public void setSunset(int sunset) {
        this.sunset = sunset;
    }

    public int getSunset() {
        return sunset;
    }

    @Override
    public String toString() {
        return
                "Sys{" +
                        "type = '" + type + '\'' +
                        ",id = '" + id + '\'' +
                        ",country = '" + country + '\'' +
                        ",sunrise = '" + sunrise + '\'' +
                        ",sunset = '" + sunset + '\'' +
                        "}";
    }
}