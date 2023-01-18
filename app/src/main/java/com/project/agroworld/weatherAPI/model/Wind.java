package com.project.agroworld.weatherAPI.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Wind implements Serializable {

    @SerializedName("speed")
    private Object speed;

    @SerializedName("deg")
    private int deg;

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

    @Override
    public String toString() {
        return
                "Wind{" +
                        "speed = '" + speed + '\'' +
                        ",deg = '" + deg + '\'' +
                        "}";
    }
}