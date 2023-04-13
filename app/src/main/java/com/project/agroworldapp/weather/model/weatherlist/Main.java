package com.project.agroworldapp.weather.model.weatherlist;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Main implements Serializable {

    @SerializedName("temp")
    private double temp;

    @SerializedName("feels_like")
    private Object feelsLike;

    @SerializedName("temp_min")
    private Object tempMin;

    @SerializedName("temp_max")
    private Object tempMax;

    @SerializedName("pressure")
    private int pressure;

    @SerializedName("sea_level")
    private int seaLevel;

    @SerializedName("grnd_level")
    private int grndLevel;

    @SerializedName("humidity")
    private int humidity;

    @SerializedName("temp_kf")
    private Object tempKf;

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public Object getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Object feelsLike) {
        this.feelsLike = feelsLike;
    }

    public Object getTempMin() {
        return tempMin;
    }

    public void setTempMin(Object tempMin) {
        this.tempMin = tempMin;
    }

    public Object getTempMax() {
        return tempMax;
    }

    public void setTempMax(Object tempMax) {
        this.tempMax = tempMax;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(int seaLevel) {
        this.seaLevel = seaLevel;
    }

    public int getGrndLevel() {
        return grndLevel;
    }

    public void setGrndLevel(int grndLevel) {
        this.grndLevel = grndLevel;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public Object getTempKf() {
        return tempKf;
    }

    public void setTempKf(Object tempKf) {
        this.tempKf = tempKf;
    }

    @NonNull
    @Override
    public String toString() {
        return "Main{" + "temp = '" + temp + '\'' + ",feels_like = '" + feelsLike + '\'' + ",temp_min = '" + tempMin + '\'' + ",temp_max = '" + tempMax + '\'' + ",pressure = '" + pressure + '\'' + ",sea_level = '" + seaLevel + '\'' + ",grnd_level = '" + grndLevel + '\'' + ",humidity = '" + humidity + '\'' + ",temp_kf = '" + tempKf + '\'' + "}";
    }
}