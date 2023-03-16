package com.project.agroworld.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.agroworld.articles.model.CropsResponse;
import com.project.agroworld.articles.model.FlowersResponse;
import com.project.agroworld.articles.model.FruitsResponse;
import com.project.agroworld.articles.model.HowToExpandResponse;
import com.project.agroworld.articles.model.TechniquesResponse;
import com.project.agroworld.payment.model.PaymentModel;
import com.project.agroworld.repository.AgroWorldRepository;
import com.project.agroworld.shopping.model.ProductModel;
import com.project.agroworld.transport.model.VehicleModel;
import com.project.agroworld.utils.Resource;

import java.util.List;

public class AgroViewModel extends ViewModel {
    private AgroWorldRepository repository;

    public void init() {
        repository = new AgroWorldRepository();
    }

    private MutableLiveData<Double> totalAmount = new MutableLiveData<>();

    public Double itemAmountState(Double amount) {
        totalAmount.setValue(amount);
        return amount;
    }

    public LiveData<Double> totalAmountLivedata() {
        if (totalAmount == null) {
            totalAmount = new MutableLiveData<>();
            totalAmount.setValue(0.0);
        }
        return totalAmount;
    }

    public LiveData<Resource<List<TechniquesResponse>>> getTechniqueResponseLivedata() {
        return repository.getTechniques();
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

    public LiveData<Resource<List<PaymentModel>>> getTransactionList(){
        return  repository.getTransactionList();
    }

    public void removeProductFromFirebase(String title) {
        repository.removeProductFromFirebase(title);
    }

    public void uploadTransaction(PaymentModel paymentModel){
        repository.uploadTransactionDetail(paymentModel);
    }

    public LiveData<String> checkLoadingStatus(){
        return repository.getRequestErrorLivedata();
    }

}
