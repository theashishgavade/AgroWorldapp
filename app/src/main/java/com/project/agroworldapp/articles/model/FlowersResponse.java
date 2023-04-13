package com.project.agroworldapp.articles.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FlowersResponse implements Serializable {

    @SerializedName("irrigation")
    private String irrigation;

    @SerializedName("propagation")
    private String propagation;

    @SerializedName("image_link")
    private String imageLink;

    @SerializedName("seed")
    private String seed;

    @SerializedName("harvesting")
    private String harvesting;

    @SerializedName("post_harvest")
    private String postHarvest;

    @SerializedName("season")
    private String season;

    @SerializedName("soil")
    private String soil;

    @SerializedName("best_time_to_grow")
    private String bestTimeToGrow;

    @SerializedName("title")
    private String title;

    @SerializedName("flower_info")
    private String flowerInfo;

    @SerializedName("land_preparation")
    private String landPreparation;

    public String getIrrigation() {
        return irrigation;
    }

    public void setIrrigation(String irrigation) {
        this.irrigation = irrigation;
    }

    public String getPropagation() {
        return propagation;
    }

    public void setPropagation(String propagation) {
        this.propagation = propagation;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getHarvesting() {
        return harvesting;
    }

    public void setHarvesting(String harvesting) {
        this.harvesting = harvesting;
    }

    public String getPostHarvest() {
        return postHarvest;
    }

    public void setPostHarvest(String postHarvest) {
        this.postHarvest = postHarvest;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getSoil() {
        return soil;
    }

    public void setSoil(String soil) {
        this.soil = soil;
    }

    public String getBestTimeToGrow() {
        return bestTimeToGrow;
    }

    public void setBestTimeToGrow(String bestTimeToGrow) {
        this.bestTimeToGrow = bestTimeToGrow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFlowerInfo() {
        return flowerInfo;
    }

    public void setFlowerInfo(String flowerInfo) {
        this.flowerInfo = flowerInfo;
    }

    public String getLandPreparation() {
        return landPreparation;
    }

    public void setLandPreparation(String landPreparation) {
        this.landPreparation = landPreparation;
    }

    @NonNull
    @Override
    public String toString() {
        return
                "FlowersResponse{" +
                        "irrigation = '" + irrigation + '\'' +
                        ",propagation = '" + propagation + '\'' +
                        ",image_link = '" + imageLink + '\'' +
                        ",seed = '" + seed + '\'' +
                        ",harvesting = '" + harvesting + '\'' +
                        ",post_harvest = '" + postHarvest + '\'' +
                        ",season = '" + season + '\'' +
                        ",soil = '" + soil + '\'' +
                        ",best_time_to_grow = '" + bestTimeToGrow + '\'' +
                        ",title = '" + title + '\'' +
                        ",flower_info = '" + flowerInfo + '\'' +
                        ",land_preparation = '" + landPreparation + '\'' +
                        "}";
    }
}