package com.project.agroworld.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.project.agroworld.articles.model.CropsResponse;
import com.project.agroworld.articles.model.FlowersResponse;
import com.project.agroworld.articles.model.FruitsResponse;
import com.project.agroworld.articles.model.HowToExpandResponse;
import com.project.agroworld.articles.model.TechniquesResponse;
import com.project.agroworld.ui.repository.AgroWorldRepository;
import com.project.agroworld.ui.shopping.model.ProductModel;
import com.project.agroworld.ui.transport.model.VehicleModel;
import com.project.agroworld.utils.Resource;

import java.util.List;

public class AgroViewModel extends ViewModel {
    private AgroWorldRepository repository;
    public void init() {
        repository = new AgroWorldRepository();
    }
    public LiveData<Resource<List<TechniquesResponse>>> getTechniqueResponseLivedata() {
        return repository.getTechniques();
    }

    public LiveData<Resource<List<FruitsResponse>>> getFruitsResponseLivedata(){
        return repository.getFruitsResponse();
    }

    public LiveData<Resource<List<HowToExpandResponse>>> getHowToExpandResponseLivedata(){
        return repository.getHowToExpandResponse();
    }

    public LiveData<Resource<List<CropsResponse>>> getCropsResponseLivedata(){
        return repository.getCropsResponse();
    }
    public LiveData<Resource<List<FlowersResponse>>> getFlowersResponseLivedata(){
        return repository.getFlowersResponse();
    }

    public LiveData<Resource<List<ProductModel>>> getProductModelLivedata(){
        return repository.getProductListFromFirebase();
    }

    public LiveData<Resource<List<VehicleModel>>> getVehicleModelLivedata(){
        return repository.getVehicleListFromFirebase();
    }

    public void removeProductFromFirebase(String title){
        repository.removeProductFromFirebase(title);
    }

}
