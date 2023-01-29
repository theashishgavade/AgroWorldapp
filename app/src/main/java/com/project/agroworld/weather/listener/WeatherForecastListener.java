package com.project.agroworld.weather.listener;

import com.project.agroworld.weather.model.weatherlist.ListItem;

public interface WeatherForecastListener {

    void onForecastWeatherCardClick(ListItem listItem, int position);
}
