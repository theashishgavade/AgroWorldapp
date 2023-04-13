package com.project.agroworldapp.articles.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FruitsResponse implements Serializable {

    @SerializedName("image_link")
    private String imageLink;

    @SerializedName("seed")
    private String seed;

    @SerializedName("soil")
    private String soil;

    @SerializedName("best_time_to_grow")
    private String bestTimeToGrow;

    @SerializedName("fruit_info")
    private String fruitInfo;

    @SerializedName("title")
    private String title;

    @SerializedName("land_preparation")
    private String landPreparation;

    @SerializedName("irrigation")
    private String irrigation;

    @SerializedName("required_temperature ")
    private String requiredTemperature;

    @SerializedName("harvesting")
    private String harvesting;

    @SerializedName("post_harvest")
    private String postHarvest;

    @SerializedName("season")
    private String season;

    @SerializedName("state")
    private String state;

    public String getImageLink() {
        return imageLink;
    }

    public String getSeed() {
        return seed;
    }

    public String getSoil() {
        return soil;
    }

    public String getBestTimeToGrow() {
        return bestTimeToGrow;
    }

    public String getFruitInfo() {
        return fruitInfo;
    }

    public String getTitle() {
        return title;
    }

    public String getLandPreparation() {
        return landPreparation;
    }

    public String getIrrigation() {
        return irrigation;
    }

    public String getRequiredTemperature() {
        return requiredTemperature;
    }

    public String getHarvesting() {
        return harvesting;
    }

    public String getPostHarvest() {
        return postHarvest;
    }

    public String getSeason() {
        return season;
    }

    public String getState() {
        return state;
    }

    @NonNull
    @Override
    public String toString() {
        return
                "FruitsResponse{" +
                        "image_link = '" + imageLink + '\'' +
                        ",seed = '" + seed + '\'' +
                        ",soil = '" + soil + '\'' +
                        ",best_time_to_grow = '" + bestTimeToGrow + '\'' +
                        ",fruit_info = '" + fruitInfo + '\'' +
                        ",title = '" + title + '\'' +
                        ",land_preparation = '" + landPreparation + '\'' +
                        ",irrigation = '" + irrigation + '\'' +
                        ",required_temperature  = '" + requiredTemperature + '\'' +
                        ",harvesting = '" + harvesting + '\'' +
                        ",post_harvest = '" + postHarvest + '\'' +
                        ",season = '" + season + '\'' +
                        ",state = '" + state + '\'' +
                        "}";
    }
}