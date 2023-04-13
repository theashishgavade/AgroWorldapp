package com.project.agroworldapp.manufacture.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.agroworldapp.R;
import com.project.agroworldapp.databinding.ActivityManufactureDataPostBinding;
import com.project.agroworldapp.shopping.model.ProductModel;
import com.project.agroworldapp.utils.Constants;
import com.project.agroworldapp.utils.CustomMultiColorProgressBar;
import com.project.agroworldapp.utils.Permissions;

public class ManufactureActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 99;
    protected String selectedDataLanguage;
    private ActivityManufactureDataPostBinding binding;
    private Uri imageUri;
    private DatabaseReference firebaseStorage;
    private CustomMultiColorProgressBar progressBar;
    private String editImageUrl;
    private boolean isImageSelected = false;
    private boolean isEditAction = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manufacture_data_post);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.manufacture_panel));
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        ProductModel productModel = ((ProductModel) intent.getSerializableExtra("productModel"));
        selectedDataLanguage = intent.getStringExtra("selectedDataLanguage");
        boolean isManufactureEditAction = intent.getBooleanExtra("manufactureEditAction", false);
        boolean isLocalizedData = intent.getBooleanExtra("isLocalizedData", false);
        if (isManufactureEditAction && productModel != null) {
            updateIntentData(productModel);
        }
        progressBar = new CustomMultiColorProgressBar(this, getString(R.string.loader_message));
        binding.crdUploadImage.setOnClickListener(v -> {
            selectImage();
        });

        binding.btnUpdateData.setOnClickListener(v -> {
            String title = binding.etProductTitle.getText().toString();
            String description = binding.etProductDescription.getText().toString();
            String amountStr = binding.etProductPrice.getText().toString();

            if (title.isEmpty()) {
                binding.etProductTitle.setError(getString(R.string.this_field_required));
                return;
            }
            if (amountStr.isEmpty()) {
                binding.etProductPrice.setError(getString(R.string.this_field_required));
                return;
            }
            if (description.isEmpty()) {
                binding.etProductDescription.setError(getString(R.string.this_field_required));
                return;
            }
            if (!isImageSelected) {
                Constants.showToast(this, "Please select image");
                return;
            }
            double price = Double.parseDouble(amountStr);
            if (isEditAction && isLocalizedData) {
                uploadLocalizedDataToFirebase(title, description, price, editImageUrl);
            } else if (isEditAction) {
                uploadDataToFirebase(title, description, price, editImageUrl);
            } else if (Permissions.checkConnection(this) && price != 0 && isImageSelected) {
                uploadImageToFirebase(title, price, description);
            }
        });

    }

    private void updateIntentData(ProductModel productModel) {
        isEditAction = true;
        isImageSelected = true;
        editImageUrl = productModel.getImageUrl();
        binding.etProductTitle.setClickable(true);
        binding.etProductTitle.setFocusable(false);
        binding.ivProductUploadIcon.setVisibility(View.GONE);
        binding.etProductTitle.setText(productModel.getTitle());
        binding.etProductDescription.setText(productModel.getDescription());
        binding.etProductPrice.setText(String.valueOf(productModel.getPrice()));
        binding.etProductTitle.setOnClickListener(v -> {
            Constants.showToast(this, getString(R.string.constant_title_error));
        });
        Constants.bindImage(binding.ivProductSelected, productModel.getImageUrl(), binding.ivProductSelected);
    }

    private void uploadImageToFirebase(String title, double price, String description) {
        progressBar.showProgressBar();
        StorageReference storage = FirebaseStorage.getInstance().getReference("product");
        storage.child(title).putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            binding.ivProductSelected.setImageResource(R.color.colorPrimary);
            Constants.showToast(ManufactureActivity.this, getString(R.string.image_uploaded));
            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
                String imageUrl = task.getResult().toString();
                Log.d("fileLink", imageUrl);
                if (selectedDataLanguage != null && selectedDataLanguage.equals("English")) {
                    uploadDataToFirebase(title, description, price, imageUrl);
                } else if (selectedDataLanguage != null && selectedDataLanguage.equals("Hindi")) {
                    uploadLocalizedDataToFirebase(title, description, price, imageUrl);
                }
            }).addOnFailureListener(e -> Constants.showToast(ManufactureActivity.this, getString(R.string.failed_to_generate_url)));

        }).addOnFailureListener(e -> {
            Log.d("onFailureImageUpload", e.getLocalizedMessage());
            Constants.showToast(ManufactureActivity.this, "Failed to upload image");
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void uploadDataToFirebase(String title, String description, Double price, String imageUrl) {
        firebaseStorage = FirebaseDatabase.getInstance().getReference("product");
        ProductModel productModel = new ProductModel(title, description, price, imageUrl, 0);
        firebaseStorage.child(title).setValue(productModel).addOnSuccessListener(unused -> {
            binding.ivProductUploadIcon.setVisibility(View.VISIBLE);
            binding.etProductDescription.setText(null);
            binding.etProductPrice.setText(null);
            binding.etProductTitle.setText(null);
            progressBar.hideProgressBar();
            Constants.showToast(ManufactureActivity.this, getString(R.string.product_updated));
            Intent intent = new Intent(ManufactureActivity.this, ManufactureDataActivity.class);
            intent.putExtra("localizedData", false);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e -> {
            progressBar.hideProgressBar();
            Constants.showToast(ManufactureActivity.this, "Failed to update product");
        });
    }

    private void uploadLocalizedDataToFirebase(String title, String description, Double price, String imageUrl) {
        firebaseStorage = FirebaseDatabase.getInstance().getReference("Localized_product");
        ProductModel productModel = new ProductModel(title, description, price, imageUrl, 0);
        firebaseStorage.child(title).setValue(productModel).addOnSuccessListener(unused -> {
            binding.ivProductUploadIcon.setVisibility(View.VISIBLE);
            binding.etProductDescription.setText(null);
            binding.etProductPrice.setText(null);
            binding.etProductTitle.setText(null);
            progressBar.hideProgressBar();
            Constants.showToast(ManufactureActivity.this, getString(R.string.product_updated));
            Intent localizedIntent = new Intent(ManufactureActivity.this, ManufactureDataActivity.class);
            localizedIntent.putExtra("localizedData", true);
            startActivity(localizedIntent);
            finish();
        }).addOnFailureListener(e -> {
            progressBar.hideProgressBar();
            Constants.showToast(ManufactureActivity.this, "Failed to update product");
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null && data.getData() != null) {
            isImageSelected = true;
            imageUri = data.getData();
            Log.d("imageURi", imageUri.toString());
            binding.ivProductUploadIcon.setVisibility(View.GONE);
            binding.ivProductSelected.setImageURI(imageUri);
        }
    }
}