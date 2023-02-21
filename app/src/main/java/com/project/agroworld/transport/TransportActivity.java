package com.project.agroworld.transport;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Transport Panel");
        actionBar.setDisplayHomeAsUpEnabled(true);
        progressBar = new CustomMultiColorProgressBar(this, "Please wait\nWe're running your request...");

        binding.crdUploadImageVehicle.setOnClickListener(v -> {
            selectImage();
        });

        binding.btnUpdateDataVehicle.setOnClickListener(v -> {
            String model = binding.etVehicleModel.getText().toString();
            String rate = binding.etVehicleRate.getText().toString();
            String address = binding.etVehicleAddress.getText().toString();
            String contact = binding.etVehicleContact.getText().toString();

            if (!model.isEmpty() && !rate.isEmpty() && !address.isEmpty() && Constants.contactValidation(contact)) {
                uploadImageToFirebase(model, rate, address, contact);
            } else {
                Constants.showToast(this, "Please check given data once, Make sure all fields are filled out accurately");
            }
        });
    }

    private void uploadImageToFirebase(String model, String rates, String address, String contact) {
        progressBar.showProgressBar();
        storage = FirebaseStorage.getInstance().getReference("vehicle");
        storage.child(model).putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Constants.showToast(TransportActivity.this, "Image uploaded successfully");
            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener( task -> {
                String imageUrl = task.getResult().toString();
                Log.d("fileLink", imageUrl);
                uploadDataToFirebase(model, rates, address, contact, imageUrl);
            }).addOnFailureListener(e -> Constants.showToast(TransportActivity.this, "Failed to generate Image url"));

        }).addOnFailureListener( e -> {
            progressBar.hideProgressBar();
            Log.d("onFailureImageUpload", e.getLocalizedMessage());
            Constants.showToast(TransportActivity.this, "Failed to upload image");
        });
    }

    private void uploadDataToFirebase(String model, String rates, String address, String contact, String imageUrl) {
        firebaseStorage = FirebaseDatabase.getInstance().getReference("vehicle");
        VehicleModel vehicleModel = new VehicleModel(model, address, rates, contact, imageUrl);
        firebaseStorage.child(model).setValue(vehicleModel).addOnSuccessListener(unused -> {

            binding.ivVehicleSelected.setImageResource(R.color.colorPrimary);
            binding.ivVehicleUploadIcon.setVisibility(View.VISIBLE);
            binding.etVehicleModel.setText(null);
            binding.etVehicleAddress.setText(null);
            binding.etVehicleRate.setText(null);
            binding.etVehicleContact.setText(null);
            progressBar.hideProgressBar();
            Constants.showToast(TransportActivity.this, "Vehicle updated successfully");

        }).addOnFailureListener(e -> {
            progressBar.hideProgressBar();
            Constants.showToast(TransportActivity.this, "Failed to update vehicle details");
        });
    }


    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.transport_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.transport_list:
                moveToTransportDataActivity();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void moveToTransportDataActivity() {
        Intent intent = new Intent(TransportActivity.this, TransportDataActivity.class);
        startActivity(intent);
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
