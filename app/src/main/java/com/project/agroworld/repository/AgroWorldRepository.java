package com.project.agroworld.repository;

import static com.project.agroworld.utils.Constants.BASE_URL_SHEET_DB;

import android.content.Context;
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
import com.project.agroworld.articles.model.DiseasesResponse;
import com.project.agroworld.articles.model.FlowersResponse;
import com.project.agroworld.articles.model.FruitsResponse;
import com.project.agroworld.articles.model.HowToExpandResponse;
import com.project.agroworld.db.PreferenceHelper;
import com.project.agroworld.network.APIService;
import com.project.agroworld.network.Network;
import com.project.agroworld.payment.model.PaymentModel;
import com.project.agroworld.shopping.model.ProductModel;
import com.project.agroworld.transport.model.VehicleModel;
import com.project.agroworld.utils.Constants;
import com.project.agroworld.utils.Resource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgroWorldRepository {
    public MutableLiveData<String> requestStatus = new MutableLiveData<>();
    Context context;
    APIService apiService = Network.getInstance(BASE_URL_SHEET_DB);
    PreferenceHelper preferenceHelper = PreferenceHelper.getInstance(context);
    private boolean selectedLanguage = preferenceHelper.getData(Constants.HINDI_KEY);
    private DatabaseReference databaseReference;

    public AgroWorldRepository(Context context) {
        this.context = context;
    }

    public LiveData<String> getRequestErrorLivedata() {
        return requestStatus;
    }

    public LiveData<Resource<List<DiseasesResponse>>> getDiseasesResponse() {
        final MutableLiveData<Resource<List<DiseasesResponse>>> diseasesMutableLiveData = new MutableLiveData<>();
        apiService.getDiseasesList().enqueue(new Callback<List<DiseasesResponse>>() {
            @Override
            public void onResponse(Call<List<DiseasesResponse>> call, Response<List<DiseasesResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    diseasesMutableLiveData.setValue(Resource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<DiseasesResponse>> call, Throwable t) {
                diseasesMutableLiveData.setValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
        return diseasesMutableLiveData;
    }

    public LiveData<Resource<List<FlowersResponse>>> getFlowersResponse() {
        final MutableLiveData<Resource<List<FlowersResponse>>> flowersMutableLiveData = new MutableLiveData<>();
        Call<List<FlowersResponse>> flowerApi;
        if (selectedLanguage) {
            flowerApi = apiService.getLocalizedFlowersList();
        } else {
            flowerApi = apiService.getFlowersList();
        }
        flowerApi.enqueue(new Callback<List<FlowersResponse>>() {
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
                Constants.printLog(response.body() + " getHowToExpandResponse");
                if (response.isSuccessful() && response.body() != null) {
                    howToExpandLivedata.setValue(Resource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<HowToExpandResponse>> call, Throwable t) {
                Constants.printLog(t.getMessage() + " getHowToExpandResponse");
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

    public void uploadTransactionDetail(PaymentModel paymentModel, String email) {
        databaseReference = FirebaseDatabase.getInstance().getReference(email + "-transaction");
        databaseReference.child(paymentModel.getPaymentID()).setValue(paymentModel).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void deleteCartData(String email) {
        databaseReference = FirebaseDatabase.getInstance().getReference(email + "-CartItems");
        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("removeValue", "Cart node deleted successfully");
            }
        });
    }

    public LiveData<Resource<List<PaymentModel>>> getTransactionList(String email) {
        final MutableLiveData<Resource<List<PaymentModel>>> historyLivedata = new MutableLiveData<>();
        databaseReference = FirebaseDatabase.getInstance().getReference(email + "-transaction");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<PaymentModel> paymentModelArrayList = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot product : snapshot.getChildren()) {
                        PaymentModel paymentModel = product.getValue(PaymentModel.class);
                        if (paymentModel != null) {
                            paymentModelArrayList.add(paymentModel);
                        }
                    }
                    historyLivedata.setValue(Resource.success(paymentModelArrayList));
                } else {
                    historyLivedata.setValue(Resource.error("Look's like you haven't made any transaction yet.", null));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                historyLivedata.setValue(Resource.error(error.getMessage(), null));
            }
        });
        return historyLivedata;
    }

}
