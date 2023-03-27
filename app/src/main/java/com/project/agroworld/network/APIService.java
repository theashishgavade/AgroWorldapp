package com.project.agroworld.network;

import com.project.agroworld.articles.model.CropsResponse;
import com.project.agroworld.articles.model.DiseasesResponse;
import com.project.agroworld.articles.model.FlowersResponse;
import com.project.agroworld.articles.model.FruitsResponse;
import com.project.agroworld.articles.model.HowToExpandResponse;
import com.project.agroworld.weather.model.weather_data.WeatherResponse;
import com.project.agroworld.weather.model.weatherlist.WeatherDatesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    //https://api.openweathermap.org/data/2.5/weather?lat=19.075975&lon=72.87738&appid=92f4e9a9c233be99f0b33d1c58c72386
    //https://api.openweathermap.org/data/2.5/forecast?lat=44.34&lon=10.99&appid=92f4e9a9c233be99f0b33d1c58c72386
    //https://sheetdb.io/api/v1/4hm2n4jziczjy

    @GET("weather")
    Call<WeatherResponse> getWeatherData(
            @Query("lat") Double lat,
            @Query("lon") Double lon,
            @Query("appid") String apiKey);

    @GET("forecast")
    Call<WeatherDatesResponse> getWeatherForecastData(
            @Query("lat") Double lat,
            @Query("lon") Double lon,
            @Query("appid") String apiKey);

    @GET("n79017ltxsonb")
    Call<List<DiseasesResponse>> getDiseasesList();

    @GET("e49zdqylo9c2q")
    Call<List<DiseasesResponse>> getLocalizedDiseasesList();

    @GET("r4wu8rpk45yu6")
    Call<List<FruitsResponse>> getFruitsFromDB();

    @GET("dywj6h1xvdo4e")
    Call<List<FruitsResponse>> getLocalizedFruitsList();

    @GET("93yx6646p49z4")
    Call<List<FlowersResponse>> getFlowersList();

    @GET("8uqwwa7qvueqf")
    Call<List<FlowersResponse>> getLocalizedFlowersList();

    @GET("krs0kbpmodi8d")
    Call<List<CropsResponse>> getListOfCrops();

    @GET("gnmuv9wnwcawh")
    Call<List<CropsResponse>> getLocalizedCropsList();

    @GET("tsljs9opsrj4c")
    Call<List<HowToExpandResponse>> getListOfHowToExpandData();

    @GET("habebx59tfol4")
    Call<List<HowToExpandResponse>> getLocalizedHowToExpandData();
}
