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
import com.project.agroworldapp.articles.adapter.HowToExpandAdapter;
import com.project.agroworldapp.articles.listener.ExpandClickListener;
import com.project.agroworldapp.articles.model.HowToExpandResponse;
import com.project.agroworldapp.databinding.ActivityHowToExpandBinding;
import com.project.agroworldapp.ui.repository.AgroWorldRepositoryImpl;
import com.project.agroworldapp.utils.Permissions;
import com.project.agroworldapp.viewmodel.AgroViewModel;
import com.project.agroworldapp.viewmodel.AgroWorldViewModelFactory;

import java.util.ArrayList;

public class HowToExpandActivity extends AppCompatActivity implements ExpandClickListener {

    private final ArrayList<HowToExpandResponse> howToExpandDataList = new ArrayList<>();
    private AgroViewModel viewModel;
    private ActivityHowToExpandBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_how_to_expand);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.how_to_expand));
        initializeAgroWorldViewModel();
        if (Permissions.checkConnection(this)) {
            getExpandListFromApi();
        }
    }

    private void getExpandListFromApi() {
        binding.expandProgressBar.setVisibility(View.VISIBLE);
        viewModel.getHowToExpandResponseLivedata();
        viewModel.observeHowToExpandLivedata.observe(this, resource -> {
            switch (resource.status) {
                case ERROR:
                    binding.expandProgressBar.setVisibility(View.GONE);
                    binding.rvHowToExpand.setVisibility(View.GONE);
                    binding.tvNoExpandDataFound.setVisibility(View.VISIBLE);
                    binding.tvNoExpandDataFound.setText(resource.message);
                    break;
                case LOADING:
                    break;
                case SUCCESS:
                    binding.expandProgressBar.setVisibility(View.GONE);
                    if (resource.data != null) {
                        howToExpandDataList.clear();
                        howToExpandDataList.addAll(resource.data);
                        binding.rvHowToExpand.setVisibility(View.VISIBLE);
                        setRecyclerView();
                    } else {
                        binding.tvNoExpandDataFound.setVisibility(View.VISIBLE);
                        binding.tvNoExpandDataFound.setText(getString(R.string.no_data_found));
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
        HowToExpandAdapter expandAdapter = new HowToExpandAdapter(howToExpandDataList, this);
        binding.rvHowToExpand.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvHowToExpand.setAdapter(expandAdapter);
    }

    @Override
    public void onExpandItemClick(HowToExpandResponse response) {
        Intent intent = new Intent(HowToExpandActivity.this, ArticleDetailsActivity.class);
        intent.putExtra("expandItemResponse", response);
        intent.putExtra("isExpandResponse", true);
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