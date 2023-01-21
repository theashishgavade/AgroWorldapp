package com.project.agroworld.manufacture;

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
import com.project.agroworld.databinding.ActivityManufactureDataPostBinding;
import com.project.agroworld.manufacture.model.ProductModel;
import com.project.agroworld.utils.Constants;
import com.project.agroworld.utils.CustomMultiColorProgressBar;

public class ManufactureDataPost extends AppCompatActivity {
    private ActivityManufactureDataPostBinding binding;
    private int REQUEST_CODE = 99;
    private Uri imageUri;
    private DatabaseReference firebaseStorage;
    private StorageReference storage;
    private CustomMultiColorProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manufacture_data_post);
        progressBar = new CustomMultiColorProgressBar(this, "Uploading in progress...");
        binding.crdUploadImage.setOnClickListener(v -> {
            selectImage();
        });

        binding.btnUpdateData.setOnClickListener(v -> {
            String title = binding.etProductTitle.getText().toString();
            double price = Double.parseDouble(binding.etProductPrice.getText().toString());
            String description = binding.etProductDescription.getText().toString();

            if (!title.isEmpty() && price != 0 && !description.isEmpty()) {
                uploadImageToFirebase(title, price, description);
            } else {
                Constants.showToast(this, "All fields are required to fill.");
            }
        });

  /*      binding.btnProductImageUpload.setOnClickListener(v -> {
            String title = binding.etProductTitle.getText().toString();
            Double price = Double.valueOf(binding.etProductPrice.getText().toString());
            String description = binding.etProductDescription.getText().toString();

            if (!title.isEmpty() && !price.toString().isEmpty() && !description.isEmpty()) {
            } else {
                Constants.showToast(this, "All fields are required to fill.");
            }
        });*/
    }

    private void uploadImageToFirebase(String title, double price, String description) {
        progressBar.showProgressBar();
        storage = FirebaseStorage.getInstance().getReference("product");
        storage.child(title).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String imageUrl = task.getResult().toString();
                        Log.d("fileLink", imageUrl);
                        uploadDataToFirebase(title, description, price, imageUrl);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Constants.showToast(ManufactureDataPost.this, "Failed to generate Image url");
                    }
                });
                binding.ivProductSelected.setImageURI(null);
                Constants.showToast(ManufactureDataPost.this, "Image uploaded successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("onFailureImageUpload", e.getLocalizedMessage());
                Constants.showToast(ManufactureDataPost.this, "Failed to upload image");
            }
        });
    }

    private void retrieveImageFromFirebase() {


    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void uploadDataToFirebase(String title, String description, Double price, String imageUrl) {
        firebaseStorage = FirebaseDatabase.getInstance().getReference("product");
        ProductModel productModel = new ProductModel(title, description, price, imageUrl);
        firebaseStorage.child(title).setValue(productModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                binding.ivProductUploadIcon.setVisibility(View.VISIBLE);
                binding.etProductDescription.setText(null);
                binding.etProductPrice.setText(null);
                binding.etProductTitle.setText(null);
                progressBar.hideProgressBar();
                Constants.showToast(ManufactureDataPost.this, "Product updated successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.hideProgressBar();
                Constants.showToast(ManufactureDataPost.this, "Failed to update product");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null && data.getData() != null) {
            imageUri = data.getData();
            Log.d("imageURi", imageUri.toString());
            binding.ivProductUploadIcon.setVisibility(View.GONE);
            binding.ivProductSelected.setImageURI(imageUri);
        }
    }
}