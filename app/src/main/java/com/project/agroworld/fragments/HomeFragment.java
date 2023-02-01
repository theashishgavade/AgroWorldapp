package com.project.agroworld.fragments;

import static com.project.agroworld.utils.Constants.BASE_URL_WEATHER;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.project.agroworld.R;
import com.project.agroworld.articles.FruitsActivity;
import com.project.agroworld.databinding.FragmentHomeBinding;
import com.project.agroworld.ui.AgroViewModel;
import com.project.agroworld.ui.shopping.activity.ProductDetailActivity;
import com.project.agroworld.ui.shopping.adapter.ProductAdapter;
import com.project.agroworld.ui.shopping.listener.OnProductListener;
import com.project.agroworld.ui.shopping.model.ProductModel;
import com.project.agroworld.ui.transport.adapter.OnVehicleCallClick;
import com.project.agroworld.ui.transport.adapter.VehicleAdapter;
import com.project.agroworld.ui.transport.model.VehicleModel;
import com.project.agroworld.utils.Constants;
import com.project.agroworld.utils.Permissions;
import com.project.agroworld.weather.APIService;
import com.project.agroworld.weather.Network;
import com.project.agroworld.weather.WeatherActivity;
import com.project.agroworld.weather.model.weather_data.WeatherResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OnProductListener, OnVehicleCallClick {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private FragmentHomeBinding binding;
    private final List<ProductModel> productModelArrayList = new ArrayList<>(5);
    private ProductAdapter productAdapter;
    private final ArrayList<VehicleModel> vehicleItemList = new ArrayList<>(5);
    private AgroViewModel viewModel;
    private VehicleAdapter vehicleAdapter;
    private String locality;
    double latitude, longitude;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Agro world");
        initViews(view);
        viewModel = new ViewModelProvider(this).get(AgroViewModel.class);
        getProductListFromFirebase();
        getVehicleListFromFirebase();

        binding.crdFruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), FruitsActivity.class));
            }
        });

    }

    private void initViews(View view) {
        binding.weatherCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WeatherActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
            }
        });
    }

    private void callApiService(Double lat, Double lon) {
        binding.weatherProgressbar.setVisibility(View.VISIBLE);
        APIService apiService = Network.getInstance(BASE_URL_WEATHER).create(APIService.class);
        apiService.getWeatherData(lat, lon, Constants.API_KEY).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                binding.weatherProgressbar.setVisibility(View.GONE);
                if (response.body() != null) {
                    updateUI(response.body());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                binding.weatherProgressbar.setVisibility(View.GONE);
                Constants.printLog(t.getLocalizedMessage());
            }
        });
    }

    private void updateUI(WeatherResponse response) {

        String temp = String.format("%.0f", (response.getMain().getTemp() + 0.01) - 273.15);
        String date = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(response.getDt() * 1000L));
        String status = response.getWeather().get(0).getDescription();
        String wind = response.getWind().getSpeed().toString();
        String humidity = String.valueOf(response.getMain().getHumidity());
        String iconUrl = "http://openweathermap.org/img/wn/" + response.getWeather().get(0).getIcon() + "@4x.png";
        Log.d("IconURL", iconUrl);
        Glide.with(requireContext()).load(iconUrl).placeholder(R.drawable.weather).dontAnimate().into(binding.ivWeatherIconHome);
        binding.tvWeatherDate.setText(date);
        binding.tvWeatherHumidity.setText("Humidity:" + humidity);
        binding.tvWeatherStatus.setText(status);
        binding.tvWeatherTemp.setText(temp + "°C");
        binding.tvWeatherWind.setText("Wind:" + wind);
        binding.tvWeatherCity.setText(locality);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Constants.showToast(requireContext(), "Please provide required permission");
            }
        }
    }

    private void getProductListFromFirebase() {
        viewModel.getProductList().observe(getViewLifecycleOwner(), productModelList -> {
            if (!productModelList.isEmpty()) {
                productModelArrayList.addAll(productModelList);
            }
            setRecyclerView();
        });
    }

    private void setRecyclerView() {
        productAdapter = new ProductAdapter(productModelArrayList, HomeFragment.this);
        binding.shoppingRecyclerView.setAdapter(productAdapter);
        binding.shoppingRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.shoppingRecyclerView.setHasFixedSize(true);
    }

    private void getVehicleListFromFirebase() {
        viewModel.getVehicleList().observe(getViewLifecycleOwner(), vehicleModels -> {
            if (!vehicleModels.isEmpty()) {
                vehicleItemList.clear();
                vehicleItemList.addAll(vehicleModels);
            }
            binding.transportRecyclerView.setVisibility(View.VISIBLE);
            setVehicleRecyclerView();
        });
    }

    private void setVehicleRecyclerView() {
        vehicleAdapter = new VehicleAdapter(vehicleItemList, HomeFragment.this);
        binding.transportRecyclerView.setAdapter(vehicleAdapter);
        binding.transportRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.transportRecyclerView.setHasFixedSize(true);
    }


    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
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
                            locality = addresses.get(0).getLocality();
                            latitude = lat;
                            longitude = lon;
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

    @Override
    public void onProductClick(ProductModel productModel) {
        Intent intent = new Intent(requireContext(), ProductDetailActivity.class);
        intent.putExtra("productModel", productModel);
        startActivity(intent);
    }

    @Override
    public void callVehicleOwner(VehicleModel vehicleModel) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + vehicleModel.getContact()));
        startActivity(intent);
    }
}