package com.project.agroworldapp.articles.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.project.agroworldapp.R;
import com.project.agroworldapp.articles.adapter.FruitsAdapter;
import com.project.agroworldapp.articles.listener.FruitsClickListener;
import com.project.agroworldapp.articles.model.FruitsResponse;
import com.project.agroworldapp.databinding.ActivityFruitsBinding;
import com.project.agroworldapp.ui.repository.AgroWorldRepositoryImpl;
import com.project.agroworldapp.utils.CustomMultiColorProgressBar;
import com.project.agroworldapp.viewmodel.AgroViewModel;
import com.project.agroworldapp.viewmodel.AgroWorldViewModelFactory;

import java.util.ArrayList;

public class FruitsActivity extends AppCompatActivity implements FruitsClickListener {

    private final ArrayList<FruitsResponse> fruitsList = new ArrayList<>();
    private AgroViewModel viewModel;
    private ActivityFruitsBinding binding;
    private CustomMultiColorProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fruits);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.fruits));
        initializeAgroWorldViewModel();
        progressBar = new CustomMultiColorProgressBar(this, getString(R.string.loader_message));
        getFruitsListFromApi();
    }

    private void getFruitsListFromApi() {
        progressBar.showProgressBar();
        viewModel.getFruitsResponseLivedata();
        viewModel.observeFruitsLiveData.observe(this, resource -> {
            switch (resource.status) {
                case ERROR:
                    progressBar.hideProgressBar();
                    binding.rvFruits.setVisibility(View.GONE);
                    binding.tvNoFruitsDataFound.setVisibility(View.VISIBLE);
                    binding.tvNoFruitsDataFound.setText(resource.message);
                    break;
                case LOADING:
                    progressBar.showProgressBar();
                    break;
                case SUCCESS:
                    if (resource.data != null) {
                        fruitsList.clear();
                        fruitsList.addAll(resource.data);
                        progressBar.hideProgressBar();
                        binding.rvFruits.setVisibility(View.VISIBLE);
                        setRecyclerView();
                    } else {
                        binding.tvNoFruitsDataFound.setVisibility(View.VISIBLE);
                        binding.tvNoFruitsDataFound.setText(getString(R.string.no_data_found));
                    }
                    break;
            }
        });
    }

    public void initializeAgroWorldViewModel() {
        AgroWorldRepositoryImpl agroWorldRepository = new AgroWorldRepositoryImpl();
        viewModel = ViewModelProviders.of(this, new AgroWorldViewModelFactory(agroWorldRepository, this)).get(AgroViewModel.class);
    }

    private void setRecyclerView() {
        FruitsAdapter fruitsAdapter = new FruitsAdapter(fruitsList, this);
        binding.rvFruits.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvFruits.setAdapter(fruitsAdapter);
    }

    @Override
    public void onFruitClick(FruitsResponse response) {
        Intent intent = new Intent(FruitsActivity.this, ArticleDetailsActivity.class);
        intent.putExtra("fruitItemResponse", response);
        intent.putExtra("isFruitResponse", true);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}