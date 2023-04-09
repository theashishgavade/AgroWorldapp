package com.project.agroworld.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.project.agroworld.articles.model.CropsResponse;
import com.project.agroworld.articles.model.DiseasesResponse;
import com.project.agroworld.articles.model.FlowersResponse;
import com.project.agroworld.articles.model.FruitsResponse;
import com.project.agroworld.articles.model.HowToExpandResponse;
import com.project.agroworld.payment.model.PaymentModel;
import com.project.agroworld.repository.AgroWorldRepository;
import com.project.agroworld.shopping.model.ProductModel;
import com.project.agroworld.transport.model.VehicleModel;
import com.project.agroworld.utils.Resource;
import com.project.agroworld.weather.model.weather_data.WeatherResponse;
import com.project.agroworld.weather.model.weatherlist.WeatherDatesResponse;

import java.util.List;

import retrofit2.Call;

public class AgroViewModel extends ViewModel {
    private AgroWorldRepository repository;

    public void init(Context context) {
        repository = new AgroWorldRepository(context);
    }

    public LiveData<Resource<WeatherResponse>> performWeatherRequest(double latitude, double longitude, String apiKey) {
        return repository.performWeatherRequest(latitude, longitude, apiKey);
    }

    public LiveData<Resource<WeatherDatesResponse>> performWeatherForecastRequest(double latitude, double longitude, String apiKey) {
        return repository.performWeatherForecastRequest(latitude, longitude, apiKey);
    }

    public LiveData<Resource<List<DiseasesResponse>>> getDiseasesResponseLivedata() {
        return repository.getDiseasesResponse();
    }

    public LiveData<Resource<List<FruitsResponse>>> getFruitsResponseLivedata() {
        return repository.getFruitsResponse();
    }

    public LiveData<Resource<List<HowToExpandResponse>>> getHowToExpandResponseLivedata() {
        return repository.getHowToExpandResponse();
    }

    public LiveData<Resource<List<CropsResponse>>> getCropsResponseLivedata() {
        return repository.getCropsResponse();
    }

    public LiveData<Resource<List<FlowersResponse>>> getFlowersResponseLivedata() {
        return repository.getFlowersResponse();
    }

    public LiveData<Resource<List<ProductModel>>> getProductModelLivedata() {
        return repository.getProductListFromFirebase();
    }

    public LiveData<Resource<List<ProductModel>>> getLocalizedProductDataList() {
        return repository.getLocalizedProductDataList();
    }

    public LiveData<Resource<List<VehicleModel>>> getVehicleModelLivedata() {
        return repository.getVehicleListFromFirebase();
    }

    public LiveData<Resource<List<PaymentModel>>> getTransactionList(String email) {
        return repository.getTransactionList(email);
    }

    public LiveData<Resource<String>> removeProductFromFirebase(String title) {
        return repository.removeProductFromFirebase(title);
    }

    public LiveData<Resource<String>> removeLocalizedProduct(String title) {
        return repository.removeLocalizedProduct(title);
    }

    public LiveData<Resource<String>> performVehicleRemovalAction(String vehicleModel) {
        return repository.performProductRemovalAction(vehicleModel);
    }

    public void uploadTransaction(PaymentModel paymentModel, String email) {
        repository.uploadTransactionDetail(paymentModel, email);
    }

    public void deleteCartData(String email) {
        repository.deleteCartData(email);
    }

    public void clearAllHistory(String email) {
        repository.removeAllTransactionHistory(email);
    }

    public LiveData<String> checkLoadingStatus() {
        return repository.getRequestErrorLivedata();
    }

}
