package com.project.agroworld.articles;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.project.agroworld.R;
import com.project.agroworld.articles.adapter.FlowersAdapter;
import com.project.agroworld.articles.adapter.FruitsAdapter;
import com.project.agroworld.articles.listener.FlowerClickListener;
import com.project.agroworld.articles.model.FlowersResponse;
import com.project.agroworld.databinding.ActivityFlowersBinding;
import com.project.agroworld.ui.viewmodel.AgroViewModel;
import com.project.agroworld.utils.CustomMultiColorProgressBar;

import java.util.ArrayList;

public class FlowersActivity extends AppCompatActivity implements FlowerClickListener {
    private ActivityFlowersBinding binding;
    private FlowersAdapter flowersAdapter;
    private final ArrayList<FlowersResponse> flowersResponsesList = new ArrayList<>();
    private CustomMultiColorProgressBar progressBar;
    private AgroViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_flowers);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.flowers));
        viewModel = ViewModelProviders.of(this).get(AgroViewModel.class);
        progressBar = new CustomMultiColorProgressBar(this, getString(R.string.loader_message));
        viewModel.init();
        getFlowersListFromApi();
    }

    private void getFlowersListFromApi() {
        progressBar.showProgressBar();
        viewModel.getFlowersResponseLivedata().observe(this, resource -> {
            switch (resource.status) {
                case ERROR:
                    progressBar.hideProgressBar();
                    binding.rvFlowers.setVisibility(View.GONE);
                    binding.tvNoFlowersDataFound.setVisibility(View.VISIBLE);
                    binding.tvNoFlowersDataFound.setText(resource.message);
                    break;
                case LOADING:
                    progressBar.showProgressBar();
                    break;
                case SUCCESS:
                    if (resource.data != null) {
                        flowersResponsesList.clear();
                        flowersResponsesList.addAll(resource.data);
                        progressBar.hideProgressBar();
                        binding.rvFlowers.setVisibility(View.VISIBLE);
                        setRecyclerView();
                    } else {
                        binding.tvNoFlowersDataFound.setVisibility(View.VISIBLE);
                        binding.tvNoFlowersDataFound.setText("Looks like Admin haven't added any item yet.");
                    }
                    break;
            }
        });
    }

    private void setRecyclerView() {
        flowersAdapter= new FlowersAdapter(flowersResponsesList, this);
        binding.rvFlowers.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvFlowers.setAdapter(flowersAdapter);
    }

    @Override
    public void onFlowersClick(FlowersResponse response) {
        Intent intent = new Intent(FlowersActivity.this, ArticleDetailsActivity.class);
        intent.putExtra("flowerItemResponse", response);
        intent.putExtra("isFlowersResponse", true);
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