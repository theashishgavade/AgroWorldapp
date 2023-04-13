package com.project.agroworldapp.weather.model.weatherlist;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Coord implements Serializable {

    @SerializedName("lat")
    private Object lat;

    @SerializedName("lon")
    private Object lon;

    public void setLat(Object lat) {
        this.lat = lat;
    }

    public Object getLat() {
        return lat;
    }

    public void setLon(Object lon) {
        this.lon = lon;
    }

    public Object getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return
                "Coord{" +
                        "lat = '" + lat + '\'' +
                        ",lon = '" + lon + '\'' +
                        "}";
    }
}