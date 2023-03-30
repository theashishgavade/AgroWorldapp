package com.project.agroworld.articles.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.project.agroworld.R;
import com.project.agroworld.articles.adapter.CropsAdapter;
import com.project.agroworld.articles.listener.CropsClickListener;
import com.project.agroworld.articles.model.CropsResponse;
import com.project.agroworld.databinding.ActivityCropsBinding;
import com.project.agroworld.utils.CustomMultiColorProgressBar;
import com.project.agroworld.utils.Permissions;
import com.project.agroworld.viewmodel.AgroViewModel;

import java.util.ArrayList;


public class CropsActivity extends AppCompatActivity implements CropsClickListener {
    private final ArrayList<CropsResponse> cropsResponseArrayList = new ArrayList<>();
    private ActivityCropsBinding binding;
    private CropsAdapter cropsAdapter;
    private AgroViewModel viewModel;
    private CustomMultiColorProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_crops);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.crops));
        viewModel = ViewModelProviders.of(this).get(AgroViewModel.class);
        progressBar = new CustomMultiColorProgressBar(this, getString(R.string.loader_message));
        viewModel.init(this);
        if (Permissions.checkConnection(this)) {
            getCropsListFromApi();
        }
    }

    private void getCropsListFromApi() {
        progressBar.showProgressBar();
        viewModel.getCropsResponseLivedata().observe(this, resource -> {
            switch (resource.status) {
                case ERROR:
                    progressBar.hideProgressBar();
                    binding.rvCrops.setVisibility(View.GONE);
                    binding.tvNoCropsDataFound.setVisibility(View.VISIBLE);
                    binding.tvNoCropsDataFound.setText(resource.message);
                    break;
                case LOADING:
                    progressBar.showProgressBar();
                    break;
                case SUCCESS:
                    progressBar.hideProgressBar();
                    if (resource.data != null) {
                        cropsResponseArrayList.clear();
                        cropsResponseArrayList.addAll(resource.data);
                        binding.rvCrops.setVisibility(View.VISIBLE);
                        setRecyclerView();
                    } else {
                        binding.rvCrops.setVisibility(View.GONE);
                        binding.tvNoCropsDataFound.setVisibility(View.VISIBLE);
                        binding.tvNoCropsDataFound.setText(R.string.no_data_found);
                    }
                    break;
            }
        });
    }

    private void setRecyclerView() {
        cropsAdapter = new CropsAdapter(cropsResponseArrayList, this);
        binding.rvCrops.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvCrops.setAdapter(cropsAdapter);
    }

    @Override
    public void onCropsClick(CropsResponse response) {
        Intent intent = new Intent(CropsActivity.this, ArticleDetailsActivity.class);
        intent.putExtra("itemResponse", response);
        intent.putExtra("isCropResponse", true);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}