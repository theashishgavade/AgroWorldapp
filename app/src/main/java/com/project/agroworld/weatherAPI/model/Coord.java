package com.project.agroworld.weatherAPI.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Coord implements Serializable {

    @SerializedName("lon")
    private Object lon;

    @SerializedName("lat")
    private Object lat;

    public void setLon(Object lon) {
        this.lon = lon;
    }

    public Object getLon() {
        return lon;
    }

    public void setLat(Object lat) {
        this.lat = lat;
    }

    public Object getLat() {
        return lat;
    }

    @Override
    public String toString() {
        return
                "Coord{" +
                        "lon = '" + lon + '\'' +
                        ",lat = '" + lat + '\'' +
                        "}";
    }
}