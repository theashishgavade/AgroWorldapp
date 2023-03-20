package com.project.agroworld.articles.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CropsResponse implements Serializable {

    @SerializedName("image_link")
    private String imageLink;

    @SerializedName("seed")
    private String seed;

    @SerializedName("crop_info")
    private String cropInfo;

    @SerializedName("soil")
    private String soil;

    @SerializedName("title")
    private String title;

    @SerializedName("fertilizer")
    private String fertilizer;

    @SerializedName("land_preparation")
    private String landPreparation;

    @SerializedName("Time of sowing")
    private String timeOfSowing;

    @SerializedName("seed_treatment")
    private String seedTreatment;

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

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getSeed() {
        return seed;
    }

    public void setCropInfo(String cropInfo) {
        this.cropInfo = cropInfo;
    }

    public String getCropInfo() {
        return cropInfo;
    }

    public void setSoil(String soil) {
        this.soil = soil;
    }

    public String getSoil() {
        return soil;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setFertilizer(String fertilizer) {
        this.fertilizer = fertilizer;
    }

    public String getFertilizer() {
        return fertilizer;
    }

    public void setLandPreparation(String landPreparation) {
        this.landPreparation = landPreparation;
    }

    public String getLandPreparation() {
        return landPreparation;
    }

    public void setTimeOfSowing(String timeOfSowing) {
        this.timeOfSowing = timeOfSowing;
    }

    public String getTimeOfSowing() {
        return timeOfSowing;
    }

    public void setSeedTreatment(String seedTreatment) {
        this.seedTreatment = seedTreatment;
    }

    public String getSeedTreatment() {
        return seedTreatment;
    }

    public void setIrrigation(String irrigation) {
        this.irrigation = irrigation;
    }

    public String getIrrigation() {
        return irrigation;
    }

    public void setRequiredTemperature(String requiredTemperature) {
        this.requiredTemperature = requiredTemperature;
    }

    public String getRequiredTemperature() {
        return requiredTemperature;
    }

    public void setHarvesting(String harvesting) {
        this.harvesting = harvesting;
    }

    public String getHarvesting() {
        return harvesting;
    }

    public void setPostHarvest(String postHarvest) {
        this.postHarvest = postHarvest;
    }

    public String getPostHarvest() {
        return postHarvest;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getSeason() {
        return season;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return
                "CropsResponse{" +
                        "image_link = '" + imageLink + '\'' +
                        ",seed = '" + seed + '\'' +
                        ",crop_info = '" + cropInfo + '\'' +
                        ",soil = '" + soil + '\'' +
                        ",title = '" + title + '\'' +
                        ",fertilizer = '" + fertilizer + '\'' +
                        ",land_preparation = '" + landPreparation + '\'' +
                        ",time of sowing = '" + timeOfSowing + '\'' +
                        ",seed_treatment = '" + seedTreatment + '\'' +
                        ",irrigation = '" + irrigation + '\'' +
                        ",required_temperature  = '" + requiredTemperature + '\'' +
                        ",harvesting = '" + harvesting + '\'' +
                        ",post_harvest = '" + postHarvest + '\'' +
                        ",season = '" + season + '\'' +
                        ",state = '" + state + '\'' +
                        "}";
    }
}