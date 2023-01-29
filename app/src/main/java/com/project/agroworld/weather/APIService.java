package com.project.agroworld.weather;

import com.project.agroworld.weather.model.weather_data.WeatherResponse;
import com.project.agroworld.weather.model.weatherlist.WeatherDatesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    //https://api.openweathermap.org/data/2.5/weather?lat=19.075975&lon=72.87738&appid=92f4e9a9c233be99f0b33d1c58c72386
    //https://api.openweathermap.org/data/2.5/forecast?lat=44.34&lon=10.99&appid=92f4e9a9c233be99f0b33d1c58c72386
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

}
