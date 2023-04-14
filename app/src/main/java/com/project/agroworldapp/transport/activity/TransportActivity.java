package com.project.agroworldapp.transport.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.agroworldapp.R;
import com.project.agroworldapp.databinding.ActivityTransportBinding;
import com.project.agroworldapp.transport.model.VehicleModel;
import com.project.agroworldapp.ui.repository.AgroWorldRepositoryImpl;
import com.project.agroworldapp.utils.Constants;
import com.project.agroworldapp.utils.CustomMultiColorProgressBar;
import com.project.agroworldapp.utils.Permissions;
import com.project.agroworldapp.viewmodel.AgroViewModel;
import com.project.agroworldapp.viewmodel.AgroWorldViewModelFactory;

import java.util.Locale;

public class TransportActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 99;
    VehicleModel vehicleModel;
    private ActivityTransportBinding binding;
    private Uri imageUri;
    private CustomMultiColorProgressBar progressBar;
    private boolean isImageSelected = false;
    private boolean isDataFromIntent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transport);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Transport Panel");
        actionBar.setDisplayHomeAsUpEnabled(true);
        progressBar = new CustomMultiColorProgressBar(this, getString(R.string.loader_message));

        Intent intent = getIntent();
        boolean isActionWithData = intent.getBooleanExtra("isActionWithData", false);
        if (isActionWithData) {
            vehicleModel = (VehicleModel) intent.getSerializableExtra("vehicleModel");
            updateUI(vehicleModel);
        }
        binding.crdUploadImageVehicle.setOnClickListener(v -> {
            selectImage();
        });

        binding.etVehicleAddress.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, binding.etLayoutAddressVehicle);
            popupMenu.getMenuInflater().inflate(R.menu.location_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getTitle() != null) {
                    binding.etVehicleAddress.setText(menuItem.getTitle() + ", Maharashtra");
                }
                return true;
            });
            popupMenu.show();
        });

        binding.btnUpdateDataVehicle.setOnClickListener(v -> {
            String model = binding.etVehicleModel.getText().toString();
            String rate = binding.etVehicleRate.getText().toString();
            String address = binding.etVehicleAddress.getText().toString();
            String contact = binding.etVehicleContact.getText().toString();
            String unit = binding.etVehicleRateUnit.getText().toString();
            if (Permissions.checkConnection(this) && isDataFromIntent) {
                uploadDataToFirebase(model, rate, unit, address, contact, imageUri.toString());
            } else if (Permissions.checkConnection(this) &&
                    !model.isEmpty() &&
                    !rate.isEmpty() &&
                    !unit.isEmpty() &&
                    !address.isEmpty() &&
                    Constants.contactValidation(contact) &&
                    isImageSelected) {
                uploadImageToFirebase(model, rate, unit.toUpperCase(Locale.ROOT), address, contact);
            } else {
                Constants.showToast(this, getString(R.string.requiredDataChecks));
            }
        });
    }

    private void updateUI(VehicleModel vehicleModel) {
        isImageSelected = true;
        isDataFromIntent = true;
        String model = vehicleModel.getModel();
        String address = vehicleModel.getAddress();
        String contact = vehicleModel.getContact();
        String rate = vehicleModel.getRates();
        String unit = vehicleModel.getUnit();
        String image = vehicleModel.getImageUrl();
        imageUri = Uri.parse(image);
        binding.etVehicleModel.setText(model);
        binding.etVehicleAddress.setText(address);
        binding.etVehicleRate.setText(rate);
        binding.etVehicleRateUnit.setText(unit);
        binding.etVehicleContact.setText(contact);
        binding.ivVehicleUploadIcon.setVisibility(View.GONE);
        binding.etVehicleModel.setClickable(true);
        binding.etVehicleModel.setFocusable(false);
        binding.etVehicleModel.setOnClickListener(v -> {
            Constants.showToast(this, getString(R.string.constant_title_error));
        });
        Glide.with(binding.ivVehicleSelected).load(image).into(binding.ivVehicleSelected);
    }

    private void uploadImageToFirebase(String model, String rates, String unit, String address, String contact) {
        progressBar.showProgressBar();
        StorageReference storage = FirebaseStorage.getInstance().getReference("vehicle");
        storage.child(model).putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Constants.showToast(TransportActivity.this, getString(R.string.image_uploaded));
            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
                String imageUrl = task.getResult().toString();
                Log.d("fileLink", imageUrl);
                uploadDataToFirebase(model, rates, unit, address, contact, imageUrl);
            }).addOnFailureListener(e -> Constants.showToast(TransportActivity.this, getString(R.string.failed_to_generate_url)));

        }).addOnFailureListener(e -> {
            progressBar.hideProgressBar();
            Log.d("onFailureImageUpload", e.getLocalizedMessage());
            Constants.showToast(TransportActivity.this, "Failed to upload image");
        });
    }

    private void uploadDataToFirebase(String model, String rates, String unit, String address, String contact, String imageUrl) {
        DatabaseReference firebaseStorage = FirebaseDatabase.getInstance().getReference("vehicle");
        VehicleModel vehicleModel = new VehicleModel(model, address, rates, unit, contact, imageUrl);
        firebaseStorage.child(model).setValue(vehicleModel).addOnSuccessListener(unused -> {

            binding.ivVehicleSelected.setImageResource(R.color.colorPrimary);
            binding.ivVehicleUploadIcon.setVisibility(View.VISIBLE);
            binding.etVehicleModel.setText(null);
            binding.etVehicleAddress.setText(null);
            binding.etVehicleRate.setText(null);
            binding.etVehicleContact.setText(null);
            progressBar.hideProgressBar();
            Constants.showToast(TransportActivity.this, getString(R.string.vehicle_updated));
            startActivity(new Intent(TransportActivity.this, TransportDataActivity.class));
            finish();
        }).addOnFailureListener(e -> {
            progressBar.hideProgressBar();
            Constants.showToast(TransportActivity.this, getString(R.string.failed_to_update_vehicle));
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
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
            binding.ivVehicleUploadIcon.setVisibility(View.GONE);
            binding.ivVehicleSelected.setImageURI(imageUri);
        }
    }
}
