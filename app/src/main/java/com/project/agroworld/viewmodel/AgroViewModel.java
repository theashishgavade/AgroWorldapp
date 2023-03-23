package com.project.agroworld.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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

import java.util.List;

public class AgroViewModel extends ViewModel {
    private AgroWorldRepository repository;
    public void init(Context context) {
        repository = new AgroWorldRepository(context);
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

    public LiveData<Resource<List<VehicleModel>>> getVehicleModelLivedata() {
        return repository.getVehicleListFromFirebase();
    }

    public LiveData<Resource<List<PaymentModel>>> getTransactionList(String email) {
        return repository.getTransactionList(email);
    }

    public void removeProductFromFirebase(String title) {
        repository.removeProductFromFirebase(title);
    }

    public void uploadTransaction(PaymentModel paymentModel, String email) {
        repository.uploadTransactionDetail(paymentModel, email);
    }

    public void deleteCartData(String email){
        repository.deleteCartData(email);
    }

    public LiveData<String> checkLoadingStatus() {
        return repository.getRequestErrorLivedata();
    }

}
