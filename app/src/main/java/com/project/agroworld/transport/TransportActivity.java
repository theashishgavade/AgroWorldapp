package com.project.agroworld.transport;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.agroworld.R;
import com.project.agroworld.databinding.ActivityTransportBinding;
import com.project.agroworld.ui.transport.model.VehicleModel;
import com.project.agroworld.utils.Constants;
import com.project.agroworld.utils.CustomMultiColorProgressBar;

public class TransportActivity extends AppCompatActivity {

    private ActivityTransportBinding binding;
    private final int REQUEST_CODE = 99;
    private Uri imageUri;
    private DatabaseReference firebaseStorage;
    private StorageReference storage;
    private CustomMultiColorProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transport);
        progressBar = new CustomMultiColorProgressBar(this, "Please wait\nWe're running your request...");
        binding.crdUploadImageVehicle.setOnClickListener(v -> {
            selectImage();
        });

        binding.btnUpdateDataVehicle.setOnClickListener(v -> {
            String model = binding.etVehicleModel.getText().toString();
            String rate = binding.etVehicleRate.getText().toString();
            String address = binding.etVehicleAddress.getText().toString();
            String contact = binding.etVehicleContact.getText().toString();

            if (!model.isEmpty() && !rate.isEmpty() && !address.isEmpty() && !contact.isEmpty()) {
                uploadImageToFirebase(model, rate, address, contact);
            } else {
                Constants.showToast(this, "All fields are required to fill.");
            }
        });
    }

    private void uploadImageToFirebase(String model, String rates, String address, String contact) {
        progressBar.showProgressBar();
        storage = FirebaseStorage.getInstance().getReference("vehicle");
        storage.child(model).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Constants.showToast(TransportActivity.this, "Image uploaded successfully");
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String imageUrl = task.getResult().toString();
                        Log.d("fileLink", imageUrl);
                        uploadDataToFirebase(model, rates, address, contact, imageUrl);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Constants.showToast(TransportActivity.this, "Failed to generate Image url");
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.hideProgressBar();
                Log.d("onFailureImageUpload", e.getLocalizedMessage());
                Constants.showToast(TransportActivity.this, "Failed to upload image");
            }
        });
    }


    private void uploadDataToFirebase(String model, String rates, String address, String contact, String imageUrl) {
        firebaseStorage = FirebaseDatabase.getInstance().getReference("vehicle");
        VehicleModel vehicleModel = new VehicleModel(model, address, rates, contact, imageUrl);
        firebaseStorage.child(model).setValue(vehicleModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                binding.ivVehicleSelected.setImageResource(R.color.colorPrimary);
                binding.ivVehicleUploadIcon.setVisibility(View.VISIBLE);
                binding.etVehicleModel.setText(null);
                binding.etVehicleAddress.setText(null);
                binding.etVehicleRate.setText(null);
                binding.etVehicleContact.setText(null);
                progressBar.hideProgressBar();
                Constants.showToast(TransportActivity.this, "Vehicle updated successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.hideProgressBar();
                Constants.showToast(TransportActivity.this, "Failed to update vehicle details");
            }
        });
    }


    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null && data.getData() != null) {
            imageUri = data.getData();
            Log.d("imageURi", imageUri.toString());
            binding.ivVehicleUploadIcon.setVisibility(View.GONE);
            binding.ivVehicleSelected.setImageURI(imageUri);
        }
    }


}
