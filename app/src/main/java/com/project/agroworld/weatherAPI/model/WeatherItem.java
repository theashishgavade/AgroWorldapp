package com.project.agroworld.weatherAPI.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WeatherItem implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("main")
    private String main;

    @SerializedName("description")
    private String description;

    @SerializedName("icon")
    private String icon;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getMain() {
        return main;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return
                "WeatherItem{" +
                        "id = '" + id + '\'' +
                        ",main = '" + main + '\'' +
                        ",description = '" + description + '\'' +
                        ",icon = '" + icon + '\'' +
                        "}";
    }
}