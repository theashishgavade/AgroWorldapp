package com.project.agroworld.weather.model.weatherlist;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WeatherDatesResponse implements Serializable {

    @SerializedName("cod")
    private String cod;

    @SerializedName("message")
    private int message;

    @SerializedName("cnt")
    private int cnt;

    @SerializedName("list")
    private List<ListItem> list;

    @SerializedName("city")
    private City city;

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getCod() {
        return cod;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getMessage() {
        return message;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getCnt() {
        return cnt;
    }

    public void setList(List<ListItem> list) {
        this.list = list;
    }

    public List<ListItem> getList() {
        return list;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    @Override
    public String toString() {
        return
                "WeatherDatesResponse{" +
                        "cod = '" + cod + '\'' +
                        ",message = '" + message + '\'' +
                        ",cnt = '" + cnt + '\'' +
                        ",list = '" + list + '\'' +
                        ",city = '" + city + '\'' +
                        "}";
    }
}