package com.project.agroworld.weather.activity;

import static com.project.agroworld.utils.Constants.API_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.project.agroworld.R;
import com.project.agroworld.databinding.ActivityWeatherForecastBinding;
import com.project.agroworld.utils.Constants;
import com.project.agroworld.utils.CustomMultiColorProgressBar;
import com.project.agroworld.utils.Permissions;
import com.project.agroworld.viewmodel.AgroViewModel;
import com.project.agroworld.weather.adapter.WeatherForecastAdapter;
import com.project.agroworld.weather.listener.WeatherForecastListener;
import com.project.agroworld.weather.model.weatherlist.ListItem;

import java.util.ArrayList;

public class WeatherForecastActivity extends AppCompatActivity implements WeatherForecastListener {
    private final ArrayList<ListItem> forecastItemArrayList = new ArrayList<>();
    private ActivityWeatherForecastBinding binding;
    private CustomMultiColorProgressBar progressBar;
    private WeatherForecastAdapter forecastAdapter;
    private AgroViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather_forecast);
        viewModel = ViewModelProviders.of(this).get(AgroViewModel.class);
        viewModel.init(this);
        progressBar = new CustomMultiColorProgressBar(this, getString(R.string.loader_message));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.forecast_weather);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat", 0.0);
        double lon = intent.getDoubleExtra("lon", 0.0);
        if (lat != 0.0 && lon != 0.0 && Permissions.checkConnection(this)) {
            callForecastApiService(lat, lon);
        }
    }

    private void callForecastApiService(double lat, double lon) {
        progressBar.showProgressBar();
        viewModel.performWeatherForecastRequest(lat, lon, API_KEY).observe(this, weatherDatesResponseResource -> {
            switch (weatherDatesResponseResource.status) {
                case ERROR:
                    progressBar.hideProgressBar();
                    binding.recyclerViewForecast.setVisibility(View.GONE);
                    binding.tvForecastNoDataFound.setVisibility(View.VISIBLE);
                    binding.tvForecastNoDataFound.setText(weatherDatesResponseResource.message);
                    break;
                case LOADING:
                    break;
                case SUCCESS:
                    progressBar.hideProgressBar();
                    if (weatherDatesResponseResource.data != null) {
                        forecastItemArrayList.clear();
                        forecastItemArrayList.addAll(weatherDatesResponseResource.data.getList());
                        setUpRecyclerView();
                    } else {
                        binding.recyclerViewForecast.setVisibility(View.GONE);
                        binding.tvForecastNoDataFound.setVisibility(View.VISIBLE);
                        binding.tvForecastNoDataFound.setText(weatherDatesResponseResource.message);
                    }
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
    public void onForecastWeatherCardClick(String description) {
        Constants.showToast(this, description);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}