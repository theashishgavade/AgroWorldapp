package com.project.agroworldapp.articles.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.project.agroworldapp.R;
import com.project.agroworldapp.articles.model.DiseasesResponse;
import com.project.agroworldapp.articles.model.InsectControlResponse;
import com.project.agroworldapp.databinding.ActivityDiseasesDetailsBinding;
import com.project.agroworldapp.utils.Constants;

public class DiseasesDetailsActivity extends AppCompatActivity {
    ActivityDiseasesDetailsBinding binding;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_diseases_details);
        Intent intent = getIntent();
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        DiseasesResponse diseasesResponse = ((DiseasesResponse) intent.getSerializableExtra("diseasesResponse"));
        boolean isDiseasesResponse = intent.getBooleanExtra("isDiseasesResponse", false);

        InsectControlResponse insectControlResponse = ((InsectControlResponse) intent.getSerializableExtra("insectControlResponse"));
        boolean isInsectControlResponse = intent.getBooleanExtra("isInsectControlResponse", false);
        if (isDiseasesResponse && diseasesResponse != null) {
            updateUI(diseasesResponse);
        } else if (isInsectControlResponse && insectControlResponse != null) {
            updateInsectControlUI(insectControlResponse);
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

    private void updateInsectControlUI(InsectControlResponse response) {
        Constants.bindImage(binding.ivItemPicture, response.getImageLink(), binding.ivItemPicture);
        actionBar.setTitle(response.getPlantName());
        binding.tvPlantName.setText("Plant- " + response.getPlantName());
        binding.tvDiseaseTitle.setText("Insect- " + response.getInsectName());
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