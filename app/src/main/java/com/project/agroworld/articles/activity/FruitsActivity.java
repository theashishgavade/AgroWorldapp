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
import com.project.agroworld.articles.adapter.FruitsAdapter;
import com.project.agroworld.articles.listener.FruitsClickListener;
import com.project.agroworld.articles.model.FruitsResponse;
import com.project.agroworld.databinding.ActivityFruitsBinding;
import com.project.agroworld.viewmodel.AgroViewModel;
import com.project.agroworld.utils.CustomMultiColorProgressBar;

import java.util.ArrayList;

public class FruitsActivity extends AppCompatActivity implements FruitsClickListener {

    private final ArrayList<FruitsResponse> fruitsList = new ArrayList<>();
    private FruitsAdapter fruitsAdapter;
    private AgroViewModel viewModel;
    private ActivityFruitsBinding binding;
    private CustomMultiColorProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fruits);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.fruits));
        viewModel = ViewModelProviders.of(this).get(AgroViewModel.class);
        progressBar = new CustomMultiColorProgressBar(this, getString(R.string.loader_message));
        viewModel.init(this);
        getFruitsListFromApi();
    }

    private void getFruitsListFromApi() {
        progressBar.showProgressBar();
        viewModel.getFruitsResponseLivedata().observe(this, resource -> {
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

    private void setRecyclerView() {
        fruitsAdapter = new FruitsAdapter(fruitsList, this);
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
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}