package com.project.agroworld.weatherAPI;

import com.project.agroworld.weatherAPI.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
//https://api.openweathermap.org/data/2.5/weather?lat=19.075975&lon=72.87738&appid=cff3614749a8d630f28ea5c7f079d389
    @GET("weather")
    Call<WeatherResponse> getWeatherData(
            @Query("lat") Double lat,
            @Query("lon") Double lon,
            @Query("appid") String apiKey);
}
