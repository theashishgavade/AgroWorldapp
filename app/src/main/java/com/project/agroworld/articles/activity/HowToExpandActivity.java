package com.project.agroworld.articles.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.project.agroworld.R;
import com.project.agroworld.articles.adapter.HowToExpandAdapter;
import com.project.agroworld.articles.listener.ExpandClickListener;
import com.project.agroworld.articles.model.HowToExpandResponse;
import com.project.agroworld.databinding.ActivityHowToExpandBinding;
import com.project.agroworld.utils.Permissions;
import com.project.agroworld.viewmodel.AgroViewModel;

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
        viewModel = new ViewModelProvider(this).get(AgroViewModel.class);
        viewModel.init(this);
        if (Permissions.checkConnection(this)) {
            getExpandListFromApi();
        }
    }

    private void getExpandListFromApi() {
        binding.expandProgressBar.setVisibility(View.VISIBLE);
        viewModel.getHowToExpandResponseLivedata().observe(this, resource -> {
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