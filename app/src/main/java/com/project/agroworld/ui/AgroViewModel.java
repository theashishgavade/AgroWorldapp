package com.project.agroworld.ui;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.agroworld.ui.shopping.model.ProductModel;
import com.project.agroworld.ui.transport.model.VehicleModel;
import com.project.agroworld.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class AgroViewModel extends ViewModel {

    private DatabaseReference databaseReference;
    private MutableLiveData<List<ProductModel>> productModelArrayList;
    private MutableLiveData<List<VehicleModel>> vehicleModelList;

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
