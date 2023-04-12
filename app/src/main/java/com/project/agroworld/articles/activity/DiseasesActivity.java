package com.project.agroworld.articles.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.project.agroworld.R;
import com.project.agroworld.articles.adapter.DiseaseAdapter;
import com.project.agroworld.articles.listener.DiseasesListener;
import com.project.agroworld.articles.model.DiseasesResponse;
import com.project.agroworld.databinding.ActivityDiseasesBinding;
import com.project.agroworld.utils.Constants;
import com.project.agroworld.utils.Permissions;
import com.project.agroworld.viewmodel.AgroViewModel;

import java.util.ArrayList;

public class DiseasesActivity extends AppCompatActivity implements DiseasesListener {
    private final ArrayList<DiseasesResponse> diseasesResponseArrayList = new ArrayList<>();
    ActivityDiseasesBinding binding;
    private AgroViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_diseases);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.diseases));
        viewModel = new ViewModelProvider(this).get(AgroViewModel.class);
        viewModel.init(this);
        if (Permissions.checkConnection(this)) {
            getDiseasesListFromAPI();
        }
    }

    private void getDiseasesListFromAPI() {
        binding.shimmer.startShimmer();
        viewModel.getDiseasesResponseLivedata().observe(this, resource -> {
            switch (resource.status) {
                case ERROR:
                    binding.shimmer.stopShimmer();
                    binding.shimmer.setVisibility(View.GONE);
                    binding.rvDiseases.setVisibility(View.GONE);
                    binding.tvNoDataFoundErr.setVisibility(View.VISIBLE);
                    binding.tvNoDataFoundErr.setText(resource.message);
                    break;
                case LOADING:
                    break;
                case SUCCESS:
                    if (resource.data != null) {
                        diseasesResponseArrayList.clear();
                        diseasesResponseArrayList.addAll(resource.data);
                        binding.shimmer.stopShimmer();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.rvDiseases.setVisibility(View.VISIBLE);
                        setRecyclerView();
                    } else {
                        binding.tvNoDataFoundErr.setVisibility(View.VISIBLE);
                        binding.tvNoDataFoundErr.setText(R.string.no_data_found);
                    }
                    break;
            }
        });
    }

    private void setRecyclerView() {
        DiseaseAdapter diseaseAdapter = new DiseaseAdapter(diseasesResponseArrayList, this);
        binding.rvDiseases.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvDiseases.setAdapter(diseaseAdapter);
    }

    @Override
    public void onDiseaseItemClick(DiseasesResponse response) {
        Intent intent = new Intent(this, DiseasesDetailsActivity.class);
        intent.putExtra("diseasesResponse", response);
        intent.putExtra("isDiseasesResponse", true);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Permissions.checkConnection(this)) {
            getDiseasesListFromAPI();
        }
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