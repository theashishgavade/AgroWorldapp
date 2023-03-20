package com.project.agroworld.articles.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DiseasesResponse implements Serializable {

    @SerializedName("symptoms")
    private String symptoms;

    @SerializedName("image_link")
    private String imageLink;

    @SerializedName("disease_name")
    private String diseaseName;

    @SerializedName("protect")
    private String protect;

    @SerializedName("plant_name")
    private String plantName;

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setProtect(String protect) {
        this.protect = protect;
    }

    public String getProtect() {
        return protect;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getPlantName() {
        return plantName;
    }

    @Override
    public String toString() {
        return
                "DiseasesResponse{" +
                        "symptoms = '" + symptoms + '\'' +
                        ",image_link = '" + imageLink + '\'' +
                        ",disease_name = '" + diseaseName + '\'' +
                        ",protect = '" + protect + '\'' +
                        ",plant_name = '" + plantName + '\'' +
                        "}";
    }
}