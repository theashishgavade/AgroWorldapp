package com.project.agroworld.ui.repository;

import static com.project.agroworld.utils.Constants.BASE_URL_SHEET_DB;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.agroworld.articles.model.TechniquesResponse;
import com.project.agroworld.ui.shopping.model.ProductModel;
import com.project.agroworld.ui.transport.model.VehicleModel;
import com.project.agroworld.utils.Resource;
import com.project.agroworld.weather.APIService;
import com.project.agroworld.weather.Network;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgroWorldRepository {
    private DatabaseReference databaseReference;

    public LiveData<Resource<List<TechniquesResponse>>> getTechniques() {
        final MutableLiveData<Resource<List<TechniquesResponse>>> techniquesMutableLiveData = new MutableLiveData<>();
        APIService apiService = Network.getInstance(BASE_URL_SHEET_DB).create(APIService.class);
        apiService.getTechniquesList().enqueue(new Callback<List<TechniquesResponse>>() {
            @Override
            public void onResponse(Call<List<TechniquesResponse>> call, Response<List<TechniquesResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    techniquesMutableLiveData.setValue(Resource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<TechniquesResponse>> call, Throwable t) {
                techniquesMutableLiveData.setValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
        return techniquesMutableLiveData;
    }


    public LiveData<Resource<List<ProductModel>>> getProductListFromFirebase() {
        final MutableLiveData<Resource<List<ProductModel>>> productLiveData = new MutableLiveData<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("product");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot product : snapshot.getChildren()) {
                        ProductModel productItem = product.getValue(ProductModel.class);
                        if (productItem != null) {
                            productModelArrayList.add(productItem);
                        }
                    }
                    productLiveData.setValue(Resource.success(productModelArrayList));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                productLiveData.setValue(Resource.error(error.getMessage(), null));
            }
        });
        return productLiveData;
    }

    public LiveData<Resource<List<VehicleModel>>> getVehicleListFromFirebase() {
        final MutableLiveData<Resource<List<VehicleModel>>> vehicleLiveData = new MutableLiveData<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("vehicle");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<VehicleModel> vehicleModelArrayList = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot product : snapshot.getChildren()) {
                        VehicleModel vehicleModel = product.getValue(VehicleModel.class);
                        if (vehicleModel != null) {
                            vehicleModelArrayList.add(vehicleModel);
                        }
                    }
                    vehicleLiveData.setValue(Resource.success(vehicleModelArrayList));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                vehicleLiveData.setValue(Resource.error(error.getMessage(), null));
            }
        });
        return vehicleLiveData;
    }
}
