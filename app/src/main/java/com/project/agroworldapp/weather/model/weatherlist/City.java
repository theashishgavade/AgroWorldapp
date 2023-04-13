package com.project.agroworldapp.weather.model.weatherlist;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class City implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("coord")
    private Coord coord;

    @SerializedName("country")
    private String country;

    @SerializedName("population")
    private int population;

    @SerializedName("timezone")
    private int timezone;

    @SerializedName("sunrise")
    private int sunrise;

    @SerializedName("sunset")
    private int sunset;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getPopulation() {
        return population;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }

    public int getSunrise() {
        return sunrise;
    }

    public void setSunset(int sunset) {
        this.sunset = sunset;
    }

    public int getSunset() {
        return sunset;
    }

    @Override
    public String toString() {
        return
                "City{" +
                        "id = '" + id + '\'' +
                        ",name = '" + name + '\'' +
                        ",coord = '" + coord + '\'' +
                        ",country = '" + country + '\'' +
                        ",population = '" + population + '\'' +
                        ",timezone = '" + timezone + '\'' +
                        ",sunrise = '" + sunrise + '\'' +
                        ",sunset = '" + sunset + '\'' +
                        "}";
    }
}