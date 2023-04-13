package com.project.agroworldapp.shopping.activity.repository;

import com.project.agroworldapp.articles.model.CropsResponse;
import com.project.agroworldapp.articles.model.DiseasesResponse;
import com.project.agroworldapp.articles.model.FlowersResponse;
import com.project.agroworldapp.articles.model.FruitsResponse;
import com.project.agroworldapp.articles.model.HowToExpandResponse;
import com.project.agroworldapp.articles.model.InsectControlResponse;
import com.project.agroworldapp.network.APIService;
import com.project.agroworldapp.weather.model.weather_data.WeatherResponse;
import com.project.agroworldapp.weather.model.weatherlist.WeatherDatesResponse;

import java.util.List;

import retrofit2.Call;

public class AgroWorldRepositoryImpl implements APIService {
    @Override
    public Call<WeatherResponse> getWeatherData(Double lat, Double lon, String apiKey) {
        return null;
    }

    @Override
    public Call<WeatherDatesResponse> getWeatherForecastData(Double lat, Double lon, String apiKey) {
        return null;
    }

    @Override
    public Call<List<DiseasesResponse>> getDiseasesList() {
        return null;
    }

    @Override
    public Call<List<DiseasesResponse>> getLocalizedDiseasesList() {
        return null;
    }

    @Override
    public Call<List<FruitsResponse>> getFruitsFromDB() {
        return null;
    }

    @Override
    public Call<List<FruitsResponse>> getLocalizedFruitsList() {
        return null;
    }

    @Override
    public Call<List<FlowersResponse>> getFlowersList() {
        return null;
    }

    @Override
    public Call<List<FlowersResponse>> getLocalizedFlowersList() {
        return null;
    }

    @Override
    public Call<List<CropsResponse>> getListOfCrops() {
        return null;
    }

    @Override
    public Call<List<CropsResponse>> getLocalizedCropsList() {
        return null;
    }

    @Override
    public Call<List<HowToExpandResponse>> getListOfHowToExpandData() {
        return null;
    }

    @Override
    public Call<List<HowToExpandResponse>> getLocalizedHowToExpandData() {
        return null;
    }

    @Override
    public Call<List<InsectControlResponse>> getInsectAndControlList() {
        return null;
    }

    @Override
    public Call<List<InsectControlResponse>> getLocalizedInsectAndControlList() {
        return null;
    }
}
