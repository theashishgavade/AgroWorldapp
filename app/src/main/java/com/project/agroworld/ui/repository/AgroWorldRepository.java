package com.project.agroworld.ui.repository;

import static com.project.agroworld.utils.Constants.BASE_URL_SHEET_DB;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.agroworld.articles.model.CropsResponse;
import com.project.agroworld.articles.model.FlowersResponse;
import com.project.agroworld.articles.model.FruitsResponse;
import com.project.agroworld.articles.model.HowToExpandResponse;
import com.project.agroworld.articles.model.TechniquesResponse;
import com.project.agroworld.ui.payment.model.PaymentModel;
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
    APIService apiService = Network.getInstance(BASE_URL_SHEET_DB).create(APIService.class);
    public MutableLiveData<String> requestStatus = new MutableLiveData<>();

    public LiveData<String> getRequestErrorLivedata() {
        return requestStatus;
    }

    public LiveData<Resource<List<TechniquesResponse>>> getTechniques() {
        final MutableLiveData<Resource<List<TechniquesResponse>>> techniquesMutableLiveData = new MutableLiveData<>();
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

    public LiveData<Resource<List<FlowersResponse>>> getFlowersResponse() {
        final MutableLiveData<Resource<List<FlowersResponse>>> flowersMutableLiveData = new MutableLiveData<>();
        apiService.getFlowersList().enqueue(new Callback<List<FlowersResponse>>() {
            @Override
            public void onResponse(Call<List<FlowersResponse>> call, Response<List<FlowersResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    flowersMutableLiveData.setValue(Resource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<FlowersResponse>> call, Throwable t) {
                flowersMutableLiveData.setValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
        return flowersMutableLiveData;
    }


    public LiveData<Resource<List<CropsResponse>>> getCropsResponse() {
        final MutableLiveData<Resource<List<CropsResponse>>> cropsMutableLiveData = new MutableLiveData<>();
        apiService.getListOfCrops().enqueue(new Callback<List<CropsResponse>>() {
            @Override
            public void onResponse(Call<List<CropsResponse>> call, Response<List<CropsResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cropsMutableLiveData.setValue(Resource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<CropsResponse>> call, Throwable t) {
                cropsMutableLiveData.setValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
        return cropsMutableLiveData;
    }

    public LiveData<Resource<List<FruitsResponse>>> getFruitsResponse() {
        final MutableLiveData<Resource<List<FruitsResponse>>> fruitsMutableLiveData = new MutableLiveData<>();
        apiService.getFruitsFromDB().enqueue(new Callback<List<FruitsResponse>>() {
            @Override
            public void onResponse(Call<List<FruitsResponse>> call, Response<List<FruitsResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fruitsMutableLiveData.setValue(Resource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<FruitsResponse>> call, Throwable t) {
                fruitsMutableLiveData.setValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
        return fruitsMutableLiveData;
    }

    public LiveData<Resource<List<HowToExpandResponse>>> getHowToExpandResponse() {
        final MutableLiveData<Resource<List<HowToExpandResponse>>> howToExpandLivedata = new MutableLiveData<>();
        apiService.getListOfHowToExpandData().enqueue(new Callback<List<HowToExpandResponse>>() {
            @Override
            public void onResponse(Call<List<HowToExpandResponse>> call, Response<List<HowToExpandResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    howToExpandLivedata.setValue(Resource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<HowToExpandResponse>> call, Throwable t) {
                howToExpandLivedata.setValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
        return howToExpandLivedata;
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

    public void removeProductFromFirebase(String title) {
        databaseReference = FirebaseDatabase.getInstance().getReference("product");
        databaseReference.child(title).removeValue().addOnSuccessListener(product -> {
            Log.d("removeProduct", "onSuccess");
        }).addOnFailureListener(command -> {
            Log.d("removeProduct", command.getLocalizedMessage());

        });
    }

    public void uploadTransactionDetail(PaymentModel paymentModel, String productTitle) {
        databaseReference = FirebaseDatabase.getInstance().getReference("transaction");
        databaseReference.child(productTitle).setValue(paymentModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                requestStatus.postValue(e.getLocalizedMessage());
            }
        });
    }
}
