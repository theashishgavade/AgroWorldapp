package com.project.agroworld.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.project.agroworld.R;
import com.project.agroworld.weatherAPI.WeatherActivity;
import com.project.agroworld.utils.Constants;
import com.project.agroworld.utils.Permissions;
import com.project.agroworld.weatherAPI.APIService;
import com.project.agroworld.weatherAPI.Network;
import com.project.agroworld.weatherAPI.model.WeatherResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FusedLocationProviderClient fusedLocationProviderClient;
    CardView weatherCard;
    ImageView ivWeatherIconHome;
    TextView tvWeatherStatus, tvWeatherTemp, tvWeatherCity, tvWeatherWind, tvWeatherHumidity, tvWeatherDate;
    ProgressBar progressBar;
    String locality, adminArea;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (!(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            askPermission();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Permissions.checkConnection(getContext()) && Permissions.isGpsEnable(getContext())) {
            getLastLocation();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        weatherCard = view.findViewById(R.id.weatherCard);
        ivWeatherIconHome = view.findViewById(R.id.ivWeatherIconHome);
        tvWeatherStatus = view.findViewById(R.id.tvWeatherStatus);
        tvWeatherTemp = view.findViewById(R.id.tvWeatherTemp);
        tvWeatherCity = view.findViewById(R.id.tvWeatherCity);
        tvWeatherWind = view.findViewById(R.id.tvWeatherWind);
        tvWeatherHumidity = view.findViewById(R.id.tvWeatherHumidity);
        tvWeatherDate = view.findViewById(R.id.tvWeatherDate);
        progressBar = view.findViewById(R.id.weatherProgressbar);

        weatherCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WeatherActivity.class);
                intent.putExtra("adminArea", adminArea);
                startActivity(intent);
            }
        });

    }

    private void callApiService(Double lat, Double lon) {
        progressBar.setVisibility(View.VISIBLE);
        APIService apiService = Network.getInstance().create(APIService.class);
        apiService.getWeatherData(lat, lon, Constants.API_KEY).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body() != null) {
                    updateUI(response.body());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Constants.printLog(t.getLocalizedMessage());
            }
        });
    }

    private void updateUI(WeatherResponse response) {

        String temp = String.format("%.0f", (response.getMain().getTemp() + 0.01) - 273.15).toString();
        String date = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(response.getDt() * 1000L));
        String status = response.getWeather().get(0).getDescription();
        String wind = response.getWind().getSpeed().toString();
        String humidity = String.valueOf(response.getMain().getHumidity());
        String iconUrl = "http://openweathermap.org/img/wn/" + response.getWeather().get(0).getIcon() + "@4x.png";
        Log.d("IconURL", iconUrl);
        Glide.with(ivWeatherIconHome).load(iconUrl).into(ivWeatherIconHome);
        tvWeatherDate.setText(date);
        tvWeatherHumidity.setText("Humidity:" + humidity);
        tvWeatherStatus.setText(status);
        tvWeatherTemp.setText(temp + "°C");
        tvWeatherWind.setText("Wind:" + wind);
        tvWeatherCity.setText(locality);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {

            }
        }
    }


    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    Double lat = addresses.get(0).getLatitude();
                                    Double lon = addresses.get(0).getLongitude();
                                    String currentLocality = addresses.get(0).getLocality();
                                    String currentAdminArea = addresses.get(0).getAdminArea();
                                    adminArea = currentAdminArea;
                                    locality = currentLocality;
                                    callApiService(lat, lon);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {

                            }
                        }
                    });
        } else {
            askPermission();
        }
    }

    private void askPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_CODE);

    }

}