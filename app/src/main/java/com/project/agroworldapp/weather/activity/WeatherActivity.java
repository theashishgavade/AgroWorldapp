package com.project.agroworldapp.weather.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.project.agroworldapp.BuildConfig;
import com.project.agroworldapp.R;
import com.project.agroworldapp.databinding.ActivityWeatherBinding;
import com.project.agroworldapp.ui.repository.AgroWorldRepositoryImpl;
import com.project.agroworldapp.utils.Constants;
import com.project.agroworldapp.utils.CustomMultiColorProgressBar;
import com.project.agroworldapp.utils.Permissions;
import com.project.agroworldapp.viewmodel.AgroViewModel;
import com.project.agroworldapp.viewmodel.AgroWorldViewModelFactory;
import com.project.agroworldapp.weather.adapter.WeatherForecastAdapter;
import com.project.agroworldapp.weather.listener.WeatherForecastListener;
import com.project.agroworldapp.weather.model.weather_data.WeatherResponse;
import com.project.agroworldapp.weather.model.weatherlist.ListItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity implements WeatherForecastListener {

    private final ArrayList<ListItem> forecastItemArrayList = new ArrayList<>();
    // initialize all buttons, textView, etc:
    double latitude, longitude;
    private ActivityWeatherBinding binding;
    private CustomMultiColorProgressBar progressBar;
    private AgroViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather);
        initializeAgroWorldViewModel();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.current_weather);
        actionBar.setDisplayHomeAsUpEnabled(true);
        progressBar = new CustomMultiColorProgressBar(this, getString(R.string.loader_message));
        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 0.0);
        longitude = intent.getDoubleExtra("longitude", 0.0);
        Log.d("getIntent", "latitude " + latitude + " , " + "longitude" + longitude);

        if (Permissions.checkConnection(this)) {
            callForecastApiService(latitude, longitude);
            callApiService(latitude, longitude);
        } else {
            binding.mainContainer.setVisibility(View.GONE);
            binding.errorText.setVisibility(View.VISIBLE);
            binding.errorText.setText(getString(R.string.something_wrong_err));
        }
    }

    private void callApiService(Double lat, Double lon) {
        progressBar.showProgressBar();
        viewModel.performWeatherRequest(lat, lon, BuildConfig.API_KEY);
        viewModel.observeWeatherResponseLivedata.observe(this, weatherResponseResource -> {
            switch (weatherResponseResource.status) {
                case ERROR:
                    progressBar.hideProgressBar();
                    binding.mainContainer.setVisibility(View.GONE);
                    binding.errorText.setVisibility(View.VISIBLE);
                    binding.errorText.setText(weatherResponseResource.message);
                    break;
                case LOADING:
                    break;
                case SUCCESS:
                    progressBar.hideProgressBar();
                    if (weatherResponseResource.data != null) {
                        updateUI(weatherResponseResource.data);
                    } else {
                        binding.mainContainer.setVisibility(View.GONE);
                        binding.errorText.setVisibility(View.VISIBLE);
                        binding.errorText.setText(weatherResponseResource.message);
                    }
            }
        });
    }

    private void callForecastApiService(double lat, double lon) {
        binding.forecastProgressBar.setVisibility(View.VISIBLE);
        binding.tvForecastNoDataFound.setVisibility(View.GONE);
        viewModel.performWeatherForecastRequest(lat, lon, BuildConfig.API_KEY);
        viewModel.observeWeatherDateResourceLiveData.observe(this, weatherDatesResponseResource -> {
            switch (weatherDatesResponseResource.status) {
                case ERROR:
                    binding.forecastProgressBar.setVisibility(View.GONE);
                    binding.recyclerViewForecast.setVisibility(View.GONE);
                    binding.tvForecastNoDataFound.setVisibility(View.VISIBLE);
                    binding.tvForecastNoDataFound.setText(weatherDatesResponseResource.message);
                    break;
                case LOADING:
                    break;
                case SUCCESS:
                    binding.forecastProgressBar.setVisibility(View.GONE);
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
        binding.recyclerViewForecast.setLayoutManager(new LinearLayoutManager(WeatherActivity.this, LinearLayoutManager.HORIZONTAL, false));
    }

    public void initializeAgroWorldViewModel() {
        AgroWorldRepositoryImpl agroWorldRepository = new AgroWorldRepositoryImpl();
        viewModel = ViewModelProviders.of(this, new AgroWorldViewModelFactory(agroWorldRepository, this)).get(AgroViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast_menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.forecast_menu:
                moveToForecastWeather();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void moveToForecastWeather() {
        Intent intent = new Intent(WeatherActivity.this, WeatherForecastActivity.class);
        intent.putExtra("lat", latitude);
        intent.putExtra("lon", longitude);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(WeatherResponse it) {

        binding.address.setText(it.getSys().getCountry() + " , " + it.getName());
        String date =
                new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(it.getDt() * 1000L));
        binding.updatedAt.setText(date);
        binding.status.setText(it.getWeather().get(0).getDescription());
        String iconUrl = "http://openweathermap.org/img/wn/" + it.getWeather().get(0).getIcon() + "@4x.png";
        Glide.with(this).load(iconUrl).placeholder(R.drawable.weather).dontAnimate().into(binding.weatherPNG);

        String mainTemp = String.format("%.0f", (it.getMain().getTemp() + 0.01) - 273.15);
        double minTemp = Double.parseDouble(String.format("%.2f", it.getMain().getTempMin() - 273.15));
        double maxTemp = Double.parseDouble(String.format("%.2f", it.getMain().getTempMax() - 273.15));
        String sunrise = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(it.getSys().getSunrise() * 1000L));
        String sunset = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(it.getSys().getSunset() * 1000L));
        String pressure = String.valueOf(it.getMain().getPressure());
        String wind = it.getWind().getSpeed().toString();
        binding.temp.setText(mainTemp + "°C");
        binding.tempMin.setText(minTemp + "°C");
        binding.tempMax.setText(maxTemp + "°C");
        binding.sunrise.setText(sunrise);
        binding.sunset.setText(sunset);
        binding.wind.setText(wind);
        binding.pressure.setText(pressure);
        binding.latitude.setText(String.valueOf(it.getCoord().getLat()));
        binding.longitude.setText(String.valueOf(it.getCoord().getLon()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onForecastWeatherCardClick(String description) {
        Constants.showToast(WeatherActivity.this, description);
    }
}