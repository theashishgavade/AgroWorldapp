package com.project.agroworld.articles.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.project.agroworld.R;
import com.project.agroworld.articles.model.DiseasesResponse;
import com.project.agroworld.databinding.ActivityDiseasesDetailsBinding;
import com.project.agroworld.utils.Constants;

public class DiseasesDetailsActivity extends AppCompatActivity {
    ActivityDiseasesDetailsBinding binding;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_diseases_details);
        Intent intent = getIntent();
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        DiseasesResponse response = ((DiseasesResponse) intent.getSerializableExtra("diseasesResponse"));
        if (response != null) {
            updateUI(response);
        }
    }

    private void updateUI(DiseasesResponse response) {
        Constants.bindImage(binding.ivItemPicture, response.getImageLink(), binding.ivItemPicture);
        actionBar.setTitle(response.getPlantName());
        binding.tvPlantName.setText("Plant- " + response.getPlantName());
        binding.tvDiseaseTitle.setText("Disease- " + response.getDiseaseName());
        binding.tvSymptomsTxt.setText(response.getSymptoms());
        binding.tvProtectInfoTxt.setText(response.getProtect());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}