package com.project.agroworld.repository;

import static com.project.agroworld.utils.Constants.BASE_URL_SHEET_DB;
import static com.project.agroworld.utils.Constants.BASE_URL_WEATHER;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.agroworld.R;
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
import com.project.agroworld.weather.model.weather_data.WeatherResponse;
import com.project.agroworld.weather.model.weatherlist.WeatherDatesResponse;

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
    private final boolean selectedLanguage = preferenceHelper.getData(Constants.HINDI_KEY);
    private DatabaseReference databaseReference;

    public AgroWorldRepository(Context context) {
        this.context = context;
    }

    public LiveData<String> getRequestErrorLivedata() {
        return requestStatus;
    }

    public LiveData<Resource<WeatherResponse>> performWeatherRequest(double latitude, double longitude, String apiKey) {
        MutableLiveData<Resource<WeatherResponse>> weatherResponseMutableLiveData = new MutableLiveData<>();
        apiService = Network.getInstance(BASE_URL_WEATHER);
        apiService.getWeatherData(latitude, longitude, apiKey).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    weatherResponseMutableLiveData.setValue(Resource.success(response.body()));
                } else {
                    weatherResponseMutableLiveData.setValue(Resource.error(response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                weatherResponseMutableLiveData.setValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
        return weatherResponseMutableLiveData;
    }

    public LiveData<Resource<WeatherDatesResponse>> performWeatherForecastRequest(double latitude, double longitude, String apiKey) {
        MutableLiveData<Resource<WeatherDatesResponse>> weatherResponseMutableLiveData = new MutableLiveData<>();
        apiService = Network.getInstance(BASE_URL_WEATHER);
        apiService.getWeatherForecastData(latitude, longitude, apiKey).enqueue(new Callback<WeatherDatesResponse>() {
            @Override
            public void onResponse(Call<WeatherDatesResponse> call, Response<WeatherDatesResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    weatherResponseMutableLiveData.setValue(Resource.success(response.body()));
                } else {
                    weatherResponseMutableLiveData.setValue(Resource.error(response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<WeatherDatesResponse> call, Throwable t) {
                weatherResponseMutableLiveData.setValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
        return weatherResponseMutableLiveData;
    }

    public LiveData<Resource<List<DiseasesResponse>>> getDiseasesResponse() {
        final MutableLiveData<Resource<List<DiseasesResponse>>> diseasesMutableLiveData = new MutableLiveData<>();
        Call<List<DiseasesResponse>> diseasesApiService;
        if (selectedLanguage) {
            diseasesApiService = apiService.getLocalizedDiseasesList();
        } else {
            diseasesApiService = apiService.getDiseasesList();
        }
        diseasesApiService.enqueue(new Callback<List<DiseasesResponse>>() {
            @Override
            public void onResponse(Call<List<DiseasesResponse>> call, Response<List<DiseasesResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    diseasesMutableLiveData.setValue(Resource.success(response.body()));
                } else {
                    diseasesMutableLiveData.setValue(Resource.error(context.getString(R.string.token_expired), null));
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
                } else {
                    flowersMutableLiveData.setValue(Resource.error(context.getString(R.string.token_expired), null));

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
        Call<List<CropsResponse>> cropApiService;
        if (selectedLanguage) {
            cropApiService = apiService.getLocalizedCropsList();
        } else {
            cropApiService = apiService.getListOfCrops();
        }
        cropApiService.enqueue(new Callback<List<CropsResponse>>() {
            @Override
            public void onResponse(Call<List<CropsResponse>> call, Response<List<CropsResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cropsMutableLiveData.setValue(Resource.success(response.body()));
                } else {
                    cropsMutableLiveData.setValue(Resource.error(context.getString(R.string.token_expired), null));
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
        Call<List<FruitsResponse>> fruitsApiService;
        if (selectedLanguage) {
            fruitsApiService = apiService.getLocalizedFruitsList();
        } else {
            fruitsApiService = apiService.getFruitsFromDB();
        }
        fruitsApiService.enqueue(new Callback<List<FruitsResponse>>() {
            @Override
            public void onResponse(Call<List<FruitsResponse>> call, Response<List<FruitsResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fruitsMutableLiveData.setValue(Resource.success(response.body()));
                } else {
                    fruitsMutableLiveData.setValue(Resource.error(context.getString(R.string.token_expired), null));

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
        Call<List<HowToExpandResponse>> howToExpandApiService;
        if (selectedLanguage) {
            howToExpandApiService = apiService.getLocalizedHowToExpandData();
        } else {
            howToExpandApiService = apiService.getListOfHowToExpandData();
        }
        howToExpandApiService.enqueue(new Callback<List<HowToExpandResponse>>() {
            @Override
            public void onResponse(Call<List<HowToExpandResponse>> call, Response<List<HowToExpandResponse>> response) {
                Constants.printLog(response.body() + " getHowToExpandResponse");
                if (response.isSuccessful() && response.body() != null) {
                    howToExpandLivedata.setValue(Resource.success(response.body()));
                } else {
                    howToExpandLivedata.setValue(Resource.error(context.getString(R.string.token_expired), null));
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
                } else {
                    productLiveData.setValue(Resource.success(null));
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
                } else {
                    vehicleLiveData.setValue(Resource.success(null));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                vehicleLiveData.setValue(Resource.error(error.getMessage(), null));
            }
        });
        return vehicleLiveData;
    }

    public LiveData<Resource<String>> removeProductFromFirebase(String title) {
        MutableLiveData<Resource<String>> productRemovalLiveStatus = new MutableLiveData<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("product");
        databaseReference.child(title).removeValue().addOnSuccessListener(product -> {
            productRemovalLiveStatus.setValue(Resource.success("Success"));
        }).addOnFailureListener(command -> {
            Log.d("removeProduct", command.getLocalizedMessage());
            productRemovalLiveStatus.setValue(Resource.error(command.getLocalizedMessage(), null));
        });
        return productRemovalLiveStatus;
    }

    public LiveData<Resource<String>> performProductRemovalAction(String vehicleModel) {
        MutableLiveData<Resource<String>> productRemoveLivedata = new MutableLiveData<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("vehicle");
        databaseReference.child(vehicleModel).removeValue().addOnSuccessListener(unused -> {
            productRemoveLivedata.setValue(Resource.success("success"));
        }).addOnFailureListener(e -> {
            productRemoveLivedata.setValue(Resource.success(e.getLocalizedMessage()));
        });
        return productRemoveLivedata;
    }

    public void uploadTransactionDetail(PaymentModel paymentModel, String email) {
        databaseReference = FirebaseDatabase.getInstance().getReference(email + "-transaction");
        databaseReference.child(paymentModel.getPaymentID()).setValue(paymentModel).addOnSuccessListener(unused -> {

        }).addOnFailureListener(e -> requestStatus.postValue(e.getLocalizedMessage()));
    }

    public void deleteCartData(String email) {
        databaseReference = FirebaseDatabase.getInstance().getReference(email + "-CartItems");
        databaseReference.removeValue().addOnSuccessListener(unused -> Log.d("removeValue", "Cart node deleted successfully"));
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
