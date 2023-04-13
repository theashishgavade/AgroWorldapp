package com.project.agroworldapp.articles.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HowToExpandResponse implements Serializable {

    @SerializedName("irrigation")
    private String irrigation;

    @SerializedName("image_link")
    private String imageLink;

    @SerializedName("seed")
    private String seed;

    @SerializedName("harvesting")
    private String harvesting;

    @SerializedName("required_temperature")
    private String requiredTemperature;

    @SerializedName("crop_info")
    private String cropInfo;

    @SerializedName("crop_name")
    private String cropName;

    @SerializedName("season")
    private String season;

    @SerializedName("fertilizer")
    private String fertilizer;

    public String getIrrigation() {
        return irrigation;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getSeed() {
        return seed;
    }

    public String getHarvesting() {
        return harvesting;
    }

    public String getRequiredTemperature() {
        return requiredTemperature;
    }

    public String getCropInfo() {
        return cropInfo;
    }

    public String getCropName() {
        return cropName;
    }

    public String getSeason() {
        return season;
    }

    public String getFertilizer() {
        return fertilizer;
    }

    @NonNull
    @Override
    public String toString() {
        return
                "HowToExpandResponse{" +
                        "irrigation = '" + irrigation + '\'' +
                        ",image_link = '" + imageLink + '\'' +
                        ",seed = '" + seed + '\'' +
                        ",harvesting = '" + harvesting + '\'' +
                        ",required_temperature = '" + requiredTemperature + '\'' +
                        ",crop_info = '" + cropInfo + '\'' +
                        ",crop_name = '" + cropName + '\'' +
                        ",season = '" + season + '\'' +
                        ",fertilizer = '" + fertilizer + '\'' +
                        "}";
    }
}