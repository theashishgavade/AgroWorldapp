package com.project.agroworldapp.weather.model.weatherlist;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ListItem implements Serializable {

    @SerializedName("dt")
    private int dt;

    @SerializedName("main")
    private Main main;

    @SerializedName("weather")
    private List<WeatherItem> weather;

    @SerializedName("clouds")
    private Clouds clouds;

    @SerializedName("wind")
    private Wind wind;

    @SerializedName("visibility")
    private int visibility;

    @SerializedName("pop")
    private double pop;

    @SerializedName("sys")
    private Sys sys;

    @SerializedName("dt_txt")
    private String dtTxt;

    public void setDt(int dt) {
        this.dt = dt;
    }

    public int getDt() {
        return dt;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Main getMain() {
        return main;
    }

    public void setWeather(List<WeatherItem> weather) {
        this.weather = weather;
    }

    public List<WeatherItem> getWeather() {
        return weather;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Wind getWind() {
        return wind;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setPop(double pop) {
        this.pop = pop;
    }

    public double getPop() {
        return pop;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public Sys getSys() {
        return sys;
    }

    public void setDtTxt(String dtTxt) {
        this.dtTxt = dtTxt;
    }

    public String getDtTxt() {
        return dtTxt;
    }

    @Override
    public String toString() {
        return
                "ListItem{" +
                        "dt = '" + dt + '\'' +
                        ",main = '" + main + '\'' +
                        ",weather = '" + weather + '\'' +
                        ",clouds = '" + clouds + '\'' +
                        ",wind = '" + wind + '\'' +
                        ",visibility = '" + visibility + '\'' +
                        ",pop = '" + pop + '\'' +
                        ",sys = '" + sys + '\'' +
                        ",dt_txt = '" + dtTxt + '\'' +
                        "}";
    }
}