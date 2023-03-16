package com.project.agroworld.weather;

import static com.project.agroworld.utils.Constants.API_KEY;
import static com.project.agroworld.utils.Constants.BASE_URL_WEATHER;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.project.agroworld.R;
import com.project.agroworld.databinding.ActivityWeatherForecastBinding;
import com.project.agroworld.networkManager.APIService;
import com.project.agroworld.networkManager.Network;
import com.project.agroworld.utils.Constants;
import com.project.agroworld.utils.CustomMultiColorProgressBar;
import com.project.agroworld.weather.adapter.WeatherForecastAdapter;
import com.project.agroworld.weather.listener.WeatherForecastListener;
import com.project.agroworld.weather.model.weatherlist.ListItem;
import com.project.agroworld.weather.model.weatherlist.WeatherDatesResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherForecastActivity extends AppCompatActivity implements WeatherForecastListener {
    private ActivityWeatherForecastBinding binding;
    private final ArrayList<ListItem> forecastItemArrayList = new ArrayList<>();
    private CustomMultiColorProgressBar progressBar;
    private WeatherForecastAdapter forecastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather_forecast);
        progressBar = new CustomMultiColorProgressBar(this, getString(R.string.loader_message));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.forecast_weather);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat", 0.0);
        double lon = intent.getDoubleExtra("lon", 0.0);
        callForecastApiService(lat, lon);
    }

    private void callForecastApiService(double lat, double lon) {
        progressBar.showProgressBar();
        binding.tvForecastNoDataFound.setVisibility(View.GONE);
        APIService apiService = Network.getInstance(BASE_URL_WEATHER).create(APIService.class);
        apiService.getWeatherForecastData(lat, lon, API_KEY).enqueue(new Callback<WeatherDatesResponse>() {
            @Override
            public void onResponse(Call<WeatherDatesResponse> call, Response<WeatherDatesResponse> response) {
                progressBar.hideProgressBar();
                if (response.body() != null) {
                    forecastItemArrayList.clear();
                    forecastItemArrayList.addAll(response.body().getList());
                    setUpRecyclerView();
                }
                Log.d("forecastItemArrayList ", String.valueOf(forecastItemArrayList.size()));
            }

            @Override
            public void onFailure(Call<WeatherDatesResponse> call, Throwable t) {
                progressBar.hideProgressBar();
                binding.tvForecastNoDataFound.setVisibility(View.VISIBLE);
                binding.tvForecastNoDataFound.setText(t.getLocalizedMessage());
                Constants.showToast(WeatherForecastActivity.this, t.getLocalizedMessage());
            }
        });
    }

    private void setUpRecyclerView() {
        forecastAdapter = new WeatherForecastAdapter(forecastItemArrayList, this);
        binding.recyclerViewForecast.setAdapter(forecastAdapter);
        binding.recyclerViewForecast.setLayoutManager(new GridLayoutManager(WeatherForecastActivity.this, 2));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onForecastWeatherCardClick(ListItem listItem, int position) {
        Constants.showToast(WeatherForecastActivity.this, "Status - " + listItem.getWeather().get(position).getDescription());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}