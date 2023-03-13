package com.project.agroworld.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.agroworld.R;
import com.project.agroworld.articles.ArticleDetailsActivity;
import com.project.agroworld.articles.adapter.CropsAdapter;
import com.project.agroworld.articles.adapter.FlowersAdapter;
import com.project.agroworld.articles.adapter.FruitsAdapter;
import com.project.agroworld.articles.adapter.HowToExpandAdapter;
import com.project.agroworld.articles.listener.CropsClickListener;
import com.project.agroworld.articles.listener.ExpandClickListener;
import com.project.agroworld.articles.listener.FlowerClickListener;
import com.project.agroworld.articles.listener.FruitsClickListener;
import com.project.agroworld.articles.model.CropsResponse;
import com.project.agroworld.articles.model.FlowersResponse;
import com.project.agroworld.articles.model.FruitsResponse;
import com.project.agroworld.articles.model.HowToExpandResponse;
import com.project.agroworld.databinding.FragmentEducationBinding;
import com.project.agroworld.viewmodel.AgroViewModel;
import com.project.agroworld.utils.CustomMultiColorProgressBar;

import java.util.ArrayList;


public class EducationFragment extends Fragment implements CropsClickListener, FruitsClickListener, FlowerClickListener, ExpandClickListener {

    private FragmentEducationBinding binding;
    private CropsAdapter cropsAdapter;
    private FlowersAdapter flowersAdapter;
    private FruitsAdapter fruitsAdapter;
    private HowToExpandAdapter expandAdapter;
    private AgroViewModel viewModel;
    private CustomMultiColorProgressBar progressBar;
    private final ArrayList<CropsResponse> cropsResponseArrayList = new ArrayList<>();
    private final ArrayList<FruitsResponse> fruitsResponseArrayList = new ArrayList<>();
    private final ArrayList<FlowersResponse> flowersResponseArrayList = new ArrayList<>();
    private final ArrayList<HowToExpandResponse> expandResponseArrayList = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_education, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        progressBar = new CustomMultiColorProgressBar(getContext(), getString(R.string.loader_message));
        viewModel = ViewModelProviders.of(this).get(AgroViewModel.class);
        viewModel.init();
        actionBar.setTitle(getString(R.string.education));
        getCropsListFromApi();
        getFruitsListFromApi();
        getFlowersListFromApi();
        getExpandListFromApi();
    }

    private void getCropsListFromApi() {
        progressBar.showProgressBar();
        viewModel.getCropsResponseLivedata().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case ERROR:
                    binding.rvCropsEd.setVisibility(View.GONE);
                    binding.tvCropsEd.setVisibility(View.VISIBLE);
                    binding.tvCropsEd.setText(getString(R.string.something_wrong_err));
                    break;
                case LOADING:
                    break;
                case SUCCESS:
                    if (resource.data != null) {
                        cropsResponseArrayList.clear();
                        cropsResponseArrayList.addAll(resource.data);
                        binding.rvCropsEd.setVisibility(View.VISIBLE);
                        setRecyclerView();
                    } else {
                        binding.tvCropsEd.setVisibility(View.VISIBLE);
                        binding.tvCropsEd.setText(getString(R.string.loader_message));
                    }
                    break;
            }
        });
    }


    private void getFlowersListFromApi() {
        viewModel.getFlowersResponseLivedata().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case ERROR:
                    progressBar.hideProgressBar();
                    binding.rvFlowersEd.setVisibility(View.GONE);
                    binding.tvFlowersEd.setVisibility(View.VISIBLE);
                    binding.tvFlowersEd.setText(R.string.something_wrong_err);
                    break;
                case LOADING:
                    break;
                case SUCCESS:
                    if (resource.data != null) {
                        flowersResponseArrayList.clear();
                        flowersResponseArrayList.addAll(resource.data);
                        progressBar.hideProgressBar();
                        binding.rvFlowersEd.setVisibility(View.VISIBLE);
                        setFlowersRecyclerView();
                    } else {
                        binding.tvFlowersEd.setVisibility(View.VISIBLE);
                        binding.tvFlowersEd.setText(getString(R.string.loader_message));
                    }
                    break;
            }
        });
    }

    private void getFruitsListFromApi() {
        viewModel.getFruitsResponseLivedata().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case ERROR:
                    binding.rvFruitsEd.setVisibility(View.GONE);
                    binding.tvFruitsEd.setVisibility(View.VISIBLE);
                    binding.tvFruitsEd.setText(getString(R.string.something_wrong_err));
                    break;
                case LOADING:
                    break;
                case SUCCESS:
                    if (resource.data != null) {
                        fruitsResponseArrayList.clear();
                        fruitsResponseArrayList.addAll(resource.data);
                        binding.rvFruitsEd.setVisibility(View.VISIBLE);
                        setFruitsRecyclerView();
                    } else {
                        binding.tvFruitsEd.setVisibility(View.VISIBLE);
                        binding.tvFruitsEd.setText(getString(R.string.loader_message));
                    }
                    break;
            }
        });
    }

    private void getExpandListFromApi() {
        viewModel.getHowToExpandResponseLivedata().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case ERROR:
                    progressBar.hideProgressBar();
                    binding.rvHowToExpandEd.setVisibility(View.GONE);
                    binding.rvHowToExpandEd.setVisibility(View.VISIBLE);
                    binding.tvHowToExpandEd.setText(R.string.something_wrong_err);
                    break;
                case LOADING:
                    break;
                case SUCCESS:
                    if (resource.data != null) {
                        expandResponseArrayList.clear();
                        expandResponseArrayList.addAll(resource.data);
                        progressBar.hideProgressBar();
                        binding.rvHowToExpandEd.setVisibility(View.VISIBLE);
                        setExpandRecyclerView();
                    } else {
                        binding.rvHowToExpandEd.setVisibility(View.VISIBLE);
                        binding.tvHowToExpandEd.setText(getString(R.string.loader_message));
                    }
                    break;
            }
        });
    }


    private void setRecyclerView() {
        cropsAdapter = new CropsAdapter(cropsResponseArrayList, this);
        binding.rvCropsEd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCropsEd.setAdapter(cropsAdapter);
    }

    private void setFruitsRecyclerView() {
        fruitsAdapter = new FruitsAdapter(fruitsResponseArrayList, this);
        binding.rvFruitsEd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvFruitsEd.setAdapter(fruitsAdapter);
    }

    private void setFlowersRecyclerView() {
        flowersAdapter = new FlowersAdapter(flowersResponseArrayList, this);
        binding.rvFlowersEd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvFlowersEd.setAdapter(flowersAdapter);
    }

    private void setExpandRecyclerView() {
        expandAdapter = new HowToExpandAdapter(expandResponseArrayList, this);
        binding.rvHowToExpandEd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvHowToExpandEd.setAdapter(expandAdapter);
    }
    @Override
    public void onCropsClick(CropsResponse response) {
        Intent intent = new Intent(getContext(), ArticleDetailsActivity.class);
        intent.putExtra("itemResponse", response);
        intent.putExtra("isCropResponse", true);
        startActivity(intent);
    }

    @Override
    public void onFlowersClick(FlowersResponse response) {
        Intent intent = new Intent(getContext(), ArticleDetailsActivity.class);
        intent.putExtra("flowerItemResponse", response);
        intent.putExtra("isFlowersResponse", true);
        startActivity(intent);
    }

    @Override
    public void onFruitClick(FruitsResponse response) {
        Intent intent = new Intent(getContext(), ArticleDetailsActivity.class);
        intent.putExtra("fruitItemResponse", response);
        intent.putExtra("isFruitResponse", true);
        startActivity(intent);
    }

    @Override
    public void onExpandItemClick(HowToExpandResponse response) {
        Intent intent = new Intent(getContext(), ArticleDetailsActivity.class);
        intent.putExtra("expandItemResponse", response);
        intent.putExtra("isExpandResponse", true);
        startActivity(intent);
    }
}