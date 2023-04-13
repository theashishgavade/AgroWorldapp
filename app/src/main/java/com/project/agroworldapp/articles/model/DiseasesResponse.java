package com.project.agroworldapp.articles.model;

import androidx.annotation.NonNull;

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

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getProtect() {
        return protect;
    }

    public void setProtect(String protect) {
        this.protect = protect;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    @NonNull
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