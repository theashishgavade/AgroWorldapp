package com.project.agroworldapp.network;

import com.project.agroworldapp.articles.model.CropsResponse;
import com.project.agroworldapp.articles.model.DiseasesResponse;
import com.project.agroworldapp.articles.model.FlowersResponse;
import com.project.agroworldapp.articles.model.FruitsResponse;
import com.project.agroworldapp.articles.model.HowToExpandResponse;
import com.project.agroworldapp.articles.model.InsectControlResponse;
import com.project.agroworldapp.weather.model.weather_data.WeatherResponse;
import com.project.agroworldapp.weather.model.weatherlist.WeatherDatesResponse;

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

    @GET("pak7qin2yt7i8")
    Call<List<DiseasesResponse>> getDiseasesList();
    @GET("cstwpz7gpz1da")
    Call<List<DiseasesResponse>> getLocalizedDiseasesList();
    @GET("0kofxd6x0muzp")
    Call<List<FruitsResponse>> getFruitsFromDB();
    @GET("9ax3hlz0utbby")
    Call<List<FruitsResponse>> getLocalizedFruitsList();
    @GET("jzqcnwnhd7pms")
    Call<List<FlowersResponse>> getFlowersList();
    @GET("bekl2o7zqgxb8")
    Call<List<FlowersResponse>> getLocalizedFlowersList();
    @GET("54hyqipsecvsw")
    Call<List<CropsResponse>> getListOfCrops();
    @GET("te4foxozbz2rk")
    Call<List<CropsResponse>> getLocalizedCropsList();
    @GET("hw7m9n2kxvupq")
    Call<List<HowToExpandResponse>> getListOfHowToExpandData();
    @GET("qy85vf93vhlbc")
    Call<List<HowToExpandResponse>> getLocalizedHowToExpandData();
    @GET("jvv0uk3btu7ln")
    Call<List<InsectControlResponse>> getInsectAndControlList();
    @GET("fg3ysaf8mkyxo")
    Call<List<InsectControlResponse>> getLocalizedInsectAndControlList();
}
