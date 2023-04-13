package com.project.agroworldapp.weather.model.weatherlist;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Wind implements Serializable {

    @SerializedName("speed")
    private Object speed;

    @SerializedName("deg")
    private int deg;

    @SerializedName("gust")
    private Object gust;

    public Object getSpeed() {
        return speed;
    }

    public void setSpeed(Object speed) {
        this.speed = speed;
    }

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public Object getGust() {
        return gust;
    }

    public void setGust(Object gust) {
        this.gust = gust;
    }

    @NonNull
    @Override
    public String toString() {
        return "Wind{" + "speed = '" + speed + '\'' + ",deg = '" + deg + '\'' + ",gust = '" + gust + '\'' + "}";
    }
}