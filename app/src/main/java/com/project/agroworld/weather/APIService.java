package com.project.agroworld.weather;

import com.project.agroworld.articles.model.CropsResponse;
import com.project.agroworld.articles.model.FlowersResponse;
import com.project.agroworld.articles.model.FruitsResponse;
import com.project.agroworld.articles.model.HowToExpandResponse;
import com.project.agroworld.articles.model.TechniquesResponse;
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

    @GET("4hm2n4jziczjy")
    Call<List<TechniquesResponse>> getTechniquesList();

    @GET("r4wu8rpk45yu6")
    Call<List<FruitsResponse>> getFruitsFromDB();

    @GET("93yx6646p49z4")
    Call<List<FlowersResponse>> getFlowersList();

    @GET("uh8m39eqk27y8")
    Call<List<CropsResponse>> getListOfCrops();
    @GET("xhuy8scok80dz")
    Call<List<HowToExpandResponse>> getListOfHowToExpandData();

}
