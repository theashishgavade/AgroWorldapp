package com.project.agroworld.ui;

import static com.project.agroworld.utils.Constants.BASE_URL_SHEET_DB;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.agroworld.articles.model.TechniquesResponse;
import com.project.agroworld.ui.shopping.model.ProductModel;
import com.project.agroworld.ui.transport.model.VehicleModel;
import com.project.agroworld.weather.APIService;
import com.project.agroworld.weather.Network;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgroViewModel extends ViewModel {

    private DatabaseReference databaseReference;
    private MutableLiveData<List<ProductModel>> productModelArrayList;
    private MutableLiveData<List<VehicleModel>> vehicleModelList;

    private MutableLiveData<List<TechniquesResponse>> techniquesResponseList;

    public LiveData<List<ProductModel>> getProductList() {
        if (productModelArrayList == null) {
            productModelArrayList = new MutableLiveData<>();
            getProductListFromFirebase();
        }
        return productModelArrayList;
    }

    public LiveData<List<VehicleModel>> getVehicleList() {
        if (vehicleModelList == null) {
            vehicleModelList = new MutableLiveData<>();
            getVehicleListFromFirebase();
        }
        return vehicleModelList;
    }

    public LiveData<List<TechniquesResponse>> getFruitsList() {
        if (techniquesResponseList == null) {
            techniquesResponseList = new MutableLiveData<>();
            getTechniques();
        }
        return techniquesResponseList;
    }

    private void getTechniques() {
        APIService apiService = Network.getInstance(BASE_URL_SHEET_DB).create(APIService.class);
        apiService.getTechniquesList().enqueue(new Callback<List<TechniquesResponse>>() {
            @Override
            public void onResponse(Call<List<TechniquesResponse>> call, Response<List<TechniquesResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    techniquesResponseList.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<TechniquesResponse>> call, Throwable t) {
                Log.d("fruitsResponseArrayList", t.getLocalizedMessage());

            }
        });
    }


    private void getProductListFromFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("product");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<ProductModel> productModelslist = new ArrayList<>();
                    for (DataSnapshot product : snapshot.getChildren()) {
                        ProductModel productItem = product.getValue(ProductModel.class);
                        if (productItem != null) {
                            productModelslist.add(productItem);
                        }
                    }
                    productModelArrayList.setValue(productModelslist);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getVehicleListFromFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("vehicle");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<VehicleModel> vehicleModelArrayList = new ArrayList<>();
                    for (DataSnapshot product : snapshot.getChildren()) {
                        VehicleModel vehicleModel = product.getValue(VehicleModel.class);
                        if (vehicleModel != null) {
                            vehicleModelArrayList.add(vehicleModel);
                        }
                    }
                    vehicleModelList.setValue(vehicleModelArrayList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
