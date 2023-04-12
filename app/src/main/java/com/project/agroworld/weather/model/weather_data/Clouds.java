package com.project.agroworld.weather.model.weather_data;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Clouds implements Serializable {

    @SerializedName("all")
    private int all;

    public void setAll(int all) {
        this.all = all;
    }

    public int getAll() {
        return all;
    }

    @NonNull
    @Override
    public String toString() {
        return
                "Clouds{" +
                        "all = '" + all + '\'' +
                        "}";
    }
}