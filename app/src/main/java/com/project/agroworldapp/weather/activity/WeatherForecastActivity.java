package com.project.agroworldapp.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.project.agroworldapp.BuildConfig;
import com.project.agroworldapp.R;
import com.project.agroworldapp.databinding.ActivityWeatherForecastBinding;
import com.project.agroworldapp.ui.repository.AgroWorldRepositoryImpl;
import com.project.agroworldapp.utils.Constants;
import com.project.agroworldapp.utils.CustomMultiColorProgressBar;
import com.project.agroworldapp.utils.Permissions;
import com.project.agroworldapp.viewmodel.AgroViewModel;
import com.project.agroworldapp.viewmodel.AgroWorldViewModelFactory;
import com.project.agroworldapp.weather.adapter.WeatherForecastAdapter;
import com.project.agroworldapp.weather.listener.WeatherForecastListener;
import com.project.agroworldapp.weather.model.weatherlist.ListItem;

import java.util.ArrayList;

public class WeatherForecastActivity extends AppCompatActivity implements WeatherForecastListener {
    private final ArrayList<ListItem> forecastItemArrayList = new ArrayList<>();
    private ActivityWeatherForecastBinding binding;
    private CustomMultiColorProgressBar progressBar;
    private AgroViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather_forecast);
        initializeAgroWorldViewModel();
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
        viewModel.performWeatherForecastRequest(lat, lon, BuildConfig.API_KEY);
        viewModel.observeWeatherDateResourceLiveData.observe(this, weatherDatesResponseResource -> {
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
        WeatherForecastAdapter forecastAdapter = new WeatherForecastAdapter(forecastItemArrayList, this);
        binding.recyclerViewForecast.setAdapter(forecastAdapter);
        binding.recyclerViewForecast.setLayoutManager(new GridLayoutManager(WeatherForecastActivity.this, 2));
    }

    public void initializeAgroWorldViewModel() {
        AgroWorldRepositoryImpl agroWorldRepository = new AgroWorldRepositoryImpl();
        viewModel = ViewModelProviders.of(this, new AgroWorldViewModelFactory(agroWorldRepository, this)).get(AgroViewModel.class);
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