package com.project.agroworldapp.weather.model.weatherlist;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Wind implements Serializable {

    @SerializedName("speed")
    private Object speed;

    @SerializedName("deg")
    private int deg;

    @SerializedName("gust")
    private Object gust;

    public void setSpeed(Object speed) {
        this.speed = speed;
    }

    public Object getSpeed() {
        return speed;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public int getDeg() {
        return deg;
    }

    public void setGust(Object gust) {
        this.gust = gust;
    }

    public Object getGust() {
        return gust;
    }

    @Override
    public String toString() {
        return
                "Wind{" +
                        "speed = '" + speed + '\'' +
                        ",deg = '" + deg + '\'' +
                        ",gust = '" + gust + '\'' +
                        "}";
    }
}