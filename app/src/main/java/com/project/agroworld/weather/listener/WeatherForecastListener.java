package com.project.agroworld.weather.listener;

import com.project.agroworld.weather.model.weatherlist.ListItem;

public interface WeatherForecastListener {

    void onForecastWeatherCardClick(String description);
}
