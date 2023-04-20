package com.project.agroworldapp.viewmodel;

import android.content.Context;
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
import com.project.agroworldapp.R;
import com.project.agroworldapp.articles.model.CropsResponse;
import com.project.agroworldapp.articles.model.DiseasesResponse;
import com.project.agroworldapp.articles.model.FlowersResponse;
import com.project.agroworldapp.articles.model.FruitsResponse;
import com.project.agroworldapp.articles.model.HowToExpandResponse;
import com.project.agroworldapp.articles.model.InsectControlResponse;
import com.project.agroworldapp.payment.model.PaymentModel;
import com.project.agroworldapp.shopping.model.ProductModel;
import com.project.agroworldapp.transport.model.VehicleModel;
import com.project.agroworldapp.ui.repository.AgroWorldRepositoryImpl;
import com.project.agroworldapp.utils.Constants;
import com.project.agroworldapp.utils.Resource;
import com.project.agroworldapp.weather.model.weather_data.WeatherResponse;
import com.project.agroworldapp.weather.model.weatherlist.WeatherDatesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgroViewModel extends ViewModel {
    AgroWorldRepositoryImpl repository;
    private DatabaseReference databaseReference;
    private Context context;
    boolean selectedLanguage;

    public AgroViewModel(AgroWorldRepositoryImpl repository, Context context) {
        this.repository = repository;
        this.context = context;
        selectedLanguage = Constants.selectedLanguage(context);
    }

    /**
     *  Call the weather API & return the updated live data to views.
     *
     */
    private final MutableLiveData<Resource<WeatherResponse>> weatherResponseMutableLivedata = new MutableLiveData<>();
    public LiveData<Resource<WeatherResponse>> observeWeatherResponseLivedata = weatherResponseMutableLivedata;
    public void performWeatherRequest(double latitude, double longitude, String apiKey) {
        repository.getWeatherData(latitude, longitude, apiKey).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    weatherResponseMutableLivedata.setValue(Resource.success(response.body()));
                } else {
                    weatherResponseMutableLivedata.postValue(Resource.error(response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                weatherResponseMutableLivedata.postValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
    }

    /**
     *  For calling the weather forecast api & returning livedata to views.
     *
     */

    private final MutableLiveData<Resource<WeatherDatesResponse>> weatherDatesMutableLivedata = new MutableLiveData<>();
    public LiveData<Resource<WeatherDatesResponse>> observeWeatherDateResourceLiveData = weatherDatesMutableLivedata;

    public void performWeatherForecastRequest(double latitude, double longitude, String apiKey) {
        repository.getWeatherForecastData(latitude, longitude, apiKey).enqueue(new Callback<WeatherDatesResponse>() {
            @Override
            public void onResponse(Call<WeatherDatesResponse> call, Response<WeatherDatesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    weatherDatesMutableLivedata.setValue(Resource.success(response.body()));
                } else {
                    weatherDatesMutableLivedata.postValue(Resource.error(response.message(), null));
                }
            }
            @Override
            public void onFailure(Call<WeatherDatesResponse> call, Throwable t) {
                weatherDatesMutableLivedata.postValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
    }

    /**
     * For getting the data from sheetDB.
     * It will hit the diseases API & return livedata.
     */
    private final MutableLiveData<Resource<List<DiseasesResponse>>> diseasesMutableLiveData = new MutableLiveData<>();
    public LiveData<Resource<List<DiseasesResponse>>> observeDiseaseResponseLivedata = diseasesMutableLiveData;

    public void getDiseasesResponseLivedata() {
        Call<List<DiseasesResponse>> diseasesApiService;
        if (selectedLanguage) {
            diseasesApiService = repository.getLocalizedDiseasesList();
        } else {
            diseasesApiService = repository.getDiseasesList();
        }
        diseasesApiService.enqueue(new Callback<List<DiseasesResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<DiseasesResponse>> call, @NonNull Response<List<DiseasesResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    diseasesMutableLiveData.setValue(Resource.success(response.body()));
                } else {
                    diseasesMutableLiveData.setValue(Resource.error(context.getString(R.string.token_expired), null));
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<DiseasesResponse>> call, @NonNull Throwable t) {
                diseasesMutableLiveData.postValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
    }

    /**
     * For getting the data from sheetDB.
     * It will identify the language & as per key, It will hit the insect & control API & return livedata.
     */
    private final MutableLiveData<Resource<List<InsectControlResponse>>> insectControlMutableLiveData = new MutableLiveData<>();
    public LiveData<Resource<List<InsectControlResponse>>> observeInsectControlLiveData = insectControlMutableLiveData;

    public void getInsectAndControlLivedata() {
        Call<List<InsectControlResponse>> insectControlApiService;
        if (selectedLanguage) {
            insectControlApiService = repository.getLocalizedInsectAndControlList();
        } else {
            insectControlApiService = repository.getInsectAndControlList();
        }
        insectControlApiService.enqueue(new Callback<List<InsectControlResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<InsectControlResponse>> call, @NonNull Response<List<InsectControlResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    insectControlMutableLiveData.setValue(Resource.success(response.body()));
                } else {
                    insectControlMutableLiveData.setValue(Resource.error(context.getString(R.string.token_expired), null));
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<InsectControlResponse>> call, @NonNull Throwable t) {
                insectControlMutableLiveData.postValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
    }

    /**
     * For getting the data from sheetDB.
     * It will identify the language & as per key, It will hit the fruits API & return livedata.
     */
    private final MutableLiveData<Resource<List<FruitsResponse>>> fruitsMutableLiveData = new MutableLiveData<>();
    public LiveData<Resource<List<FruitsResponse>>> observeFruitsLiveData = fruitsMutableLiveData;

    public void getFruitsResponseLivedata() {
        Call<List<FruitsResponse>> fruitsApiService;
        if (selectedLanguage) {
            fruitsApiService = repository.getLocalizedFruitsList();
        } else {
            fruitsApiService = repository.getFruitsFromDB();
        }
        fruitsApiService.enqueue(new Callback<List<FruitsResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<FruitsResponse>> call, @NonNull Response<List<FruitsResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fruitsMutableLiveData.setValue(Resource.success(response.body()));
                } else {
                    fruitsMutableLiveData.setValue(Resource.error(context.getString(R.string.token_expired), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FruitsResponse>> call, @NonNull Throwable t) {
                fruitsMutableLiveData.postValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
    }

    /**
     * For getting the data from sheetDB.
     * It will identify the language & as per key, It will hit the How to expand API & return livedata.
     */
    private final MutableLiveData<Resource<List<HowToExpandResponse>>> howToExpandLivedata = new MutableLiveData<>();
    public LiveData<Resource<List<HowToExpandResponse>>> observeHowToExpandLivedata = howToExpandLivedata;

    public void getHowToExpandResponseLivedata() {
        Call<List<HowToExpandResponse>> howToExpandApiService;
        if (selectedLanguage) {
            howToExpandApiService = repository.getLocalizedHowToExpandData();
        } else {
            howToExpandApiService = repository.getListOfHowToExpandData();
        }
        howToExpandApiService.enqueue(new Callback<List<HowToExpandResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<HowToExpandResponse>> call, @NonNull Response<List<HowToExpandResponse>> response) {
                Constants.printLog(response.body() + " getHowToExpandResponse");
                if (response.isSuccessful() && response.body() != null) {
                    howToExpandLivedata.setValue(Resource.success(response.body()));
                } else {
                    howToExpandLivedata.setValue(Resource.error(context.getString(R.string.token_expired), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<HowToExpandResponse>> call, @NonNull Throwable t) {
                Constants.printLog(t.getMessage() + " getHowToExpandResponse");
                howToExpandLivedata.postValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
    }

    /**
     * For getting the data from sheetDB.
     * It will identify the language & as per key, It will hit the crops API & return livedata.
     */
    private final MutableLiveData<Resource<List<CropsResponse>>> cropsMutableLiveData = new MutableLiveData<>();
    public LiveData<Resource<List<CropsResponse>>> observeCropsLiveData = cropsMutableLiveData;

    public void getCropsResponseLivedata() {
        Call<List<CropsResponse>> cropApiService;
        if (selectedLanguage) {
            cropApiService = repository.getLocalizedCropsList();
        } else {
            cropApiService = repository.getListOfCrops();
        }
        cropApiService.enqueue(new Callback<List<CropsResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<CropsResponse>> call, @NonNull Response<List<CropsResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cropsMutableLiveData.setValue(Resource.success(response.body()));
                } else {
                    cropsMutableLiveData.setValue(Resource.error(context.getString(R.string.token_expired), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CropsResponse>> call, @NonNull Throwable t) {
                cropsMutableLiveData.postValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
    }

    /**
     * For getting the data from sheetDB.
     * It will identify the language & as per key, It will hit the flower API & update livedata.
     */
    private final MutableLiveData<Resource<List<FlowersResponse>>> flowersMutableLiveData = new MutableLiveData<>();
    public LiveData<Resource<List<FlowersResponse>>> observeFlowersLiveData = flowersMutableLiveData;

    public void getFlowersResponseLivedata() {
        Call<List<FlowersResponse>> flowerApi;
        if (selectedLanguage) {
            flowerApi = repository.getLocalizedFlowersList();
        } else {
            flowerApi = repository.getFlowersList();
        }
        flowerApi.enqueue(new Callback<List<FlowersResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<FlowersResponse>> call, @NonNull Response<List<FlowersResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    flowersMutableLiveData.setValue(Resource.success(response.body()));
                } else {
                    flowersMutableLiveData.postValue(Resource.error(context.getString(R.string.token_expired), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FlowersResponse>> call, @NonNull Throwable t) {
                flowersMutableLiveData.postValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
    }

    /**
     * For getting the non-localized product model data from firebase.
     * Actively it will observe the product model data in firebase
     */
    private final MutableLiveData<Resource<List<ProductModel>>> productMutableLivedata = new MutableLiveData<>();
    public LiveData<Resource<List<ProductModel>>> observeProductLivedata = productMutableLivedata;

    public void getProductModelLivedata() {
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
                    productMutableLivedata.setValue(Resource.success(productModelArrayList));
                } else {
                    productMutableLivedata.setValue(Resource.success(null));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                productMutableLivedata.postValue(Resource.error(error.getMessage(), null));
            }
        });
    }
    /**
     * For getting the localized product model data from firebase.
     * Actively it will observe the product model data in firebase
     */
    private final MutableLiveData<Resource<List<ProductModel>>> localizedProductMutableLivedata = new MutableLiveData<>();
    public LiveData<Resource<List<ProductModel>>> observeLocalizedProductLivedata = localizedProductMutableLivedata;

    public void getLocalizedProductDataList() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Localized_product");
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
                    localizedProductMutableLivedata.setValue(Resource.success(productModelArrayList));
                } else {
                    localizedProductMutableLivedata.setValue(Resource.success(null));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                localizedProductMutableLivedata.postValue(Resource.error(error.getMessage(), null));
            }
        });
    }

    /**
     * For getting the non-localized vehicle model data from firebase.
     * Actively it will observe the vehicle model data in firebase
     */
    private final MutableLiveData<Resource<List<VehicleModel>>> transportResourceMutableLiveData = new MutableLiveData<>();
    public LiveData<Resource<List<VehicleModel>>> observeTransportResourceLiveData = transportResourceMutableLiveData;

    public void getVehicleModelLivedata() {
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
                    transportResourceMutableLiveData.setValue(Resource.success(vehicleModelArrayList));
                } else {
                    transportResourceMutableLiveData.setValue(Resource.success(null));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                transportResourceMutableLiveData.postValue(Resource.error(error.getMessage(), null));
            }
        });
    }

    /**
     * For getting the payment transaction from firebase.
     * It will fetch the transaction using the key of user current email & -transaction.
     */
    private final MutableLiveData<Resource<List<PaymentModel>>> paymentHistoryMutableLiveData = new MutableLiveData<>();
    public LiveData<Resource<List<PaymentModel>>> observePaymentHistoryLiveData = paymentHistoryMutableLiveData;

    public void getTransactionList(String email) {
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
                    paymentHistoryMutableLiveData.setValue(Resource.success(paymentModelArrayList));
                } else {
                    paymentHistoryMutableLiveData.setValue(Resource.error(context.getString(R.string.no_payment_transaction_message), null));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                paymentHistoryMutableLiveData.setValue(Resource.error(error.getMessage(), null));
            }
        });
    }

    /**
     * Block of below code will perform action to remove selected value by checking the product title in firebase product field
     * & remove the respective product from list, only manufacturer user have this permission
     */
    private final MutableLiveData<Resource<String>> productRemovalMutableLivedata = new MutableLiveData<>();
    public LiveData<Resource<String>> observeProductRemovalLivedata = productRemovalMutableLivedata;

    public void removeProductFromFirebase(String title) {
        databaseReference = FirebaseDatabase.getInstance().getReference("product");
        databaseReference.child(title).removeValue().addOnSuccessListener(product -> {
            productRemovalMutableLivedata.setValue(Resource.success("Success"));
        }).addOnFailureListener(command -> {
            productRemovalMutableLivedata.setValue(Resource.error(command.getLocalizedMessage(), null));
        });
    }

    /**
     * Using the code below, the selected value will be removed by checking the product title in the Firebase product field, and removing the respective product from the list.
     * Only the manufacturer user has access to this functionality.
     */
    private final MutableLiveData<Resource<String>> localizedProductRemovalMutableLivedata = new MutableLiveData<>();
    public LiveData<Resource<String>> observeLocalizedProductRemovalLivedata = localizedProductRemovalMutableLivedata;
    public void removeLocalizedProduct(String title) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Localized_product");
        databaseReference.child(title).removeValue().addOnSuccessListener(product -> {
            localizedProductRemovalMutableLivedata.setValue(Resource.success("Success"));
        }).addOnFailureListener(command -> {
            localizedProductRemovalMutableLivedata.setValue(Resource.error(command.getLocalizedMessage(), null));
        });
    }


    /**
     * Using the code below, the selected value will be removed by checking the product title in the Firebase localized product field, and removing the respective product from the list.
     * Only the manufacturer user has access to this functionality.
     */
    private final MutableLiveData<Resource<String>> vehicleRemovalMutableLivedata = new MutableLiveData<>();
    public LiveData<Resource<String>> observeVehicleRemovalLivedata = vehicleRemovalMutableLivedata;

    public void performVehicleRemovalAction(String vehicleModel) {
        databaseReference = FirebaseDatabase.getInstance().getReference("vehicle");
        databaseReference.child(vehicleModel).removeValue().addOnSuccessListener(unused -> {
            vehicleRemovalMutableLivedata.setValue(Resource.success("success"));
        }).addOnFailureListener(e -> {
            vehicleRemovalMutableLivedata.setValue(Resource.error(e.getLocalizedMessage(), null));
        });
    }

    /**
     * After the payment transaction has been successfully completed by the user,
     * it will upload the related transaction details to Firebase using the key of the user's current email and transaction key.
     **/
    private final MutableLiveData<Resource<String>> uploadPaymentTransactionMutableLivedata = new MutableLiveData<>();
    public LiveData<Resource<String>> observeUploadPaymentTransactionLivedata = uploadPaymentTransactionMutableLivedata;

    public void uploadTransaction(PaymentModel paymentModel, String email) {
        databaseReference = FirebaseDatabase.getInstance().getReference(email + "-transaction");
        databaseReference.child(paymentModel.getPaymentID()).setValue(paymentModel).addOnSuccessListener(unused -> {
            uploadPaymentTransactionMutableLivedata.postValue(Resource.success("success"));
        }).addOnFailureListener(e -> {
            uploadPaymentTransactionMutableLivedata.postValue(Resource.error(e.getLocalizedMessage(), null));
        });
    }

    /**
     * Once user made successful transaction it will remove all present cart item from current user list.
     */
    public void deleteCartData(String email) {
        databaseReference = FirebaseDatabase.getInstance().getReference(email + "-CartItems");
        databaseReference.removeValue().addOnSuccessListener(unused -> Log.d("removeValue", "Cart node deleted successfully"));
    }
}
