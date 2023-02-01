package com.project.agroworld.weather;

import static com.project.agroworld.utils.Constants.BASE_URL_WEATHER;

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

import com.bumptech.glide.Glide;
import com.project.agroworld.R;
import com.project.agroworld.databinding.ActivityWeatherBinding;
import com.project.agroworld.utils.Constants;
import com.project.agroworld.utils.CustomMultiColorProgressBar;
import com.project.agroworld.weather.model.weather_data.WeatherResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    // initialize all buttons, textView, etc:
    double latitude, longitude;
    private ActivityWeatherBinding binding;
    private CustomMultiColorProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Current Weather");
        actionBar.setDisplayHomeAsUpEnabled(true);
        progressBar = new CustomMultiColorProgressBar(this, "Please wait...\nWe're running your request.");
        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 0.0);
        longitude = intent.getDoubleExtra("longitude", 0.0);
        Log.d("getIntent", "latitude " + latitude + " , " + "longitude" + longitude);
        callApiService(latitude, longitude);
    }

    private void callApiService(Double lat, Double lon) {
        progressBar.showProgressBar();
        APIService apiService = Network.getInstance(BASE_URL_WEATHER).create(APIService.class);
        apiService.getWeatherData(lat, lon, Constants.API_KEY).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                progressBar.hideProgressBar();
                if (response.body() != null) {
                    updateUI(response.body());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                progressBar.hideProgressBar();
                binding.mainContainer.setVisibility(View.GONE);
                binding.errorText.setVisibility(View.VISIBLE);
                binding.errorText.setText(t.getLocalizedMessage());
                Constants.printLog(t.getLocalizedMessage());
            }
        });
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

        String mainTemp = String.format("%.0f", (it.getMain().getTemp() + 0.01) - 273.15).toString();
        double minTemp = Double.parseDouble(String.format("%.2f", it.getMain().getTempMin() - 273.15));
        double maxTemp = Double.parseDouble(String.format("%.2f", it.getMain().getTempMax() - 273.15));
        String sunrise = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(it.getSys().getSunrise() * 1000L));
        String sunset = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(it.getSys().getSunset() * 1000L));
        String pressure = String.valueOf(it.getMain().getPressure());
        String wind = String.valueOf(it.getWind().getSpeed().toString());
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
}