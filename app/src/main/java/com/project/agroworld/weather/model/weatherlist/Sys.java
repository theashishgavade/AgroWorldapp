package com.project.agroworld.weather.model.weatherlist;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Sys implements Serializable {

    @SerializedName("pod")
    private String pod;

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getPod() {
        return pod;
    }

    @Override
    public String toString() {
        return
                "Sys{" +
                        "pod = '" + pod + '\'' +
                        "}";
    }
}