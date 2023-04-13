package com.project.agroworldapp.weather.model.weather_data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Main implements Serializable {

    @SerializedName("temp")
    private Double temp;

    @SerializedName("feels_like")
    private Object feelsLike;

    @SerializedName("temp_min")
    private double tempMin;

    @SerializedName("temp_max")
    private double tempMax;

    @SerializedName("pressure")
    private int pressure;

    @SerializedName("humidity")
    private int humidity;

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getTemp() {
        return temp;
    }

    public void setFeelsLike(Object feelsLike) {
        this.feelsLike = feelsLike;
    }

    public Object getFeelsLike() {
        return feelsLike;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getPressure() {
        return pressure;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getHumidity() {
        return humidity;
    }

    @Override
    public String toString() {
        return
                "Main{" +
                        "temp = '" + temp + '\'' +
                        ",feels_like = '" + feelsLike + '\'' +
                        ",temp_min = '" + tempMin + '\'' +
                        ",temp_max = '" + tempMax + '\'' +
                        ",pressure = '" + pressure + '\'' +
                        ",humidity = '" + humidity + '\'' +
                        "}";
    }
}