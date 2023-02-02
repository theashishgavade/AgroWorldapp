package com.project.agroworld.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

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

    public LiveData<Resource<List<ProductModel>>> getProductModelLivedata(){
        return repository.getProductListFromFirebase();
    }

    public LiveData<Resource<List<VehicleModel>>> getVehicleModelLivedata(){
        return repository.getVehicleListFromFirebase();
    }

}
