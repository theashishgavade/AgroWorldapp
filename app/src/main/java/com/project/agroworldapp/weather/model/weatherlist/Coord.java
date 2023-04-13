package com.project.agroworldapp.weather.model.weatherlist;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Coord implements Serializable {

    @SerializedName("lat")
    private Object lat;

    @SerializedName("lon")
    private Object lon;

    public Object getLat() {
        return lat;
    }

    public void setLat(Object lat) {
        this.lat = lat;
    }

    public Object getLon() {
        return lon;
    }

    public void setLon(Object lon) {
        this.lon = lon;
    }

    @NonNull
    @Override
    public String toString() {
        return "Coord{" + "lat = '" + lat + '\'' + ",lon = '" + lon + '\'' + "}";
    }
}