package com.project.agroworld.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.agroworld.R;
import com.project.agroworld.articles.activity.ArticleDetailsActivity;
import com.project.agroworld.articles.activity.DiseasesDetailsActivity;
import com.project.agroworld.articles.adapter.CropsAdapter;
import com.project.agroworld.articles.adapter.DiseaseAdapter;
import com.project.agroworld.articles.adapter.FlowersAdapter;
import com.project.agroworld.articles.adapter.FruitsAdapter;
import com.project.agroworld.articles.adapter.HowToExpandAdapter;
import com.project.agroworld.articles.adapter.InsectControlAdapter;
import com.project.agroworld.articles.listener.CropsClickListener;
import com.project.agroworld.articles.listener.DiseasesListener;
import com.project.agroworld.articles.listener.ExpandClickListener;
import com.project.agroworld.articles.listener.FlowerClickListener;
import com.project.agroworld.articles.listener.FruitsClickListener;
import com.project.agroworld.articles.listener.InsectControlListener;
import com.project.agroworld.articles.model.CropsResponse;
import com.project.agroworld.articles.model.DiseasesResponse;
import com.project.agroworld.articles.model.FlowersResponse;
import com.project.agroworld.articles.model.FruitsResponse;
import com.project.agroworld.articles.model.HowToExpandResponse;
import com.project.agroworld.articles.model.InsectControlResponse;
import com.project.agroworld.databinding.FragmentEducationBinding;
import com.project.agroworld.utils.Permissions;
import com.project.agroworld.viewmodel.AgroViewModel;

import java.util.ArrayList;


public class EducationFragment extends Fragment implements CropsClickListener, FruitsClickListener, FlowerClickListener, ExpandClickListener, DiseasesListener, InsectControlListener {

    private final ArrayList<CropsResponse> cropsResponseArrayList = new ArrayList<>();
    private final ArrayList<FruitsResponse> fruitsResponseArrayList = new ArrayList<>();
    private final ArrayList<FlowersResponse> flowersResponseArrayList = new ArrayList<>();
    private final ArrayList<HowToExpandResponse> expandResponseArrayList = new ArrayList<>();
    private final ArrayList<DiseasesResponse> diseasesResponseArrayList = new ArrayList<>();

    private final ArrayList<InsectControlResponse> insectControlResponseList = new ArrayList<>();

    private FragmentEducationBinding binding;

    private AgroViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_education, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AgroViewModel.class);
        viewModel.init(getContext());
        if (Permissions.checkConnection(getContext())) {
            checkPermissionCallApi();
        }
    }

    private void checkPermissionCallApi() {
        getCropsListFromApi();
        getFruitsListFromApi();
        getFlowersListFromApi();
        getExpandListFromApi();
        getInsectAndControlData();
        getDiseasesList();
    }

    private void getCropsListFromApi() {
        binding.progressBarCrop.setVisibility(View.VISIBLE);
        viewModel.getCropsResponseLivedata().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case ERROR:
                    binding.progressBarCrop.setVisibility(View.GONE);
                    binding.rvCropsEd.setVisibility(View.GONE);
                    binding.tvCropError.setVisibility(View.VISIBLE);
                    binding.tvCropError.setText(resource.message);
                    break;
                case LOADING:
                    break;
                case SUCCESS:
                    binding.progressBarCrop.setVisibility(View.GONE);
                    if (resource.data != null) {
                        cropsResponseArrayList.clear();
                        cropsResponseArrayList.addAll(resource.data);
                        binding.rvCropsEd.setVisibility(View.VISIBLE);
                        setRecyclerView();
                    } else {
                        binding.rvCropsEd.setVisibility(View.GONE);
                        binding.tvCropError.setVisibility(View.VISIBLE);
                        binding.tvCropError.setText(getString(R.string.no_data_found));
                    }
                    break;
            }
        });
    }

    private void getFlowersListFromApi() {
        binding.progressBarFlower.setVisibility(View.VISIBLE);
        viewModel.getFlowersResponseLivedata().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case ERROR:
                    binding.progressBarFlower.setVisibility(View.GONE);
                    binding.rvFlowersEd.setVisibility(View.GONE);
                    binding.tvFlowerError.setVisibility(View.VISIBLE);
                    binding.tvFlowerError.setText(resource.message);
                    break;
                case LOADING:
                    break;
                case SUCCESS:
                    binding.progressBarFlower.setVisibility(View.GONE);
                    if (resource.data != null) {
                        flowersResponseArrayList.clear();
                        flowersResponseArrayList.addAll(resource.data);
                        binding.rvFlowersEd.setVisibility(View.VISIBLE);
                        setFlowersRecyclerView();
                    } else {
                        binding.rvFlowersEd.setVisibility(View.GONE);
                        binding.tvFlowerError.setVisibility(View.VISIBLE);
                        binding.tvFlowerError.setText(getString(R.string.no_data_found));
                    }
                    break;
            }
        });
    }

    private void getFruitsListFromApi() {
        binding.progressBarFruit.setVisibility(View.VISIBLE);
        viewModel.getFruitsResponseLivedata().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case ERROR:
                    binding.progressBarFruit.setVisibility(View.GONE);
                    binding.rvFruitsEd.setVisibility(View.GONE);
                    binding.tvFruitError.setVisibility(View.VISIBLE);
                    binding.tvFruitError.setText(resource.message);
                    break;
                case LOADING:
                    break;
                case SUCCESS:
                    binding.progressBarFruit.setVisibility(View.GONE);
                    if (resource.data != null) {
                        fruitsResponseArrayList.clear();
                        fruitsResponseArrayList.addAll(resource.data);
                        binding.rvFruitsEd.setVisibility(View.VISIBLE);
                        setFruitsRecyclerView();
                    } else {
                        binding.rvFruitsEd.setVisibility(View.GONE);
                        binding.tvFruitError.setVisibility(View.VISIBLE);
                        binding.tvFruitError.setText(getString(R.string.no_data_found));
                    }
                    break;
            }
        });
    }

    private void getExpandListFromApi() {
        binding.progressBarExpand.setVisibility(View.VISIBLE);
        viewModel.getHowToExpandResponseLivedata().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case ERROR:
                    binding.progressBarExpand.setVisibility(View.GONE);
                    binding.rvHowToExpandEd.setVisibility(View.GONE);
                    binding.tvExpandError.setVisibility(View.VISIBLE);
                    binding.tvExpandError.setText(resource.message);
                    break;
                case LOADING:
                    break;
                case SUCCESS:
                    binding.progressBarExpand.setVisibility(View.GONE);
                    if (resource.data != null) {
                        expandResponseArrayList.clear();
                        expandResponseArrayList.addAll(resource.data);
                        binding.rvHowToExpandEd.setVisibility(View.VISIBLE);
                        setExpandRecyclerView();
                    } else {
                        binding.rvHowToExpandEd.setVisibility(View.GONE);
                        binding.tvExpandError.setVisibility(View.VISIBLE);
                        binding.tvExpandError.setText(getString(R.string.loader_message));
                    }
                    break;
            }
        });
    }

    private void getDiseasesList() {
        viewModel.getDiseasesResponseLivedata().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case ERROR:
                    binding.rvDiseasesEd.setVisibility(View.GONE);
                    binding.tvDiseasesEd.setVisibility(View.VISIBLE);
                    binding.tvDiseasesEd.setText(getString(R.string.something_wrong_err));
                    break;
                case LOADING:
                    break;
                case SUCCESS:
                    if (resource.data != null) {
                        diseasesResponseArrayList.clear();
                        diseasesResponseArrayList.addAll(resource.data);
                        binding.rvDiseasesEd.setVisibility(View.VISIBLE);
                        setDiseasesRecyclerView();
                    } else {
                        binding.rvDiseasesEd.setVisibility(View.GONE);
                        binding.tvDiseasesEd.setVisibility(View.VISIBLE);
                        binding.tvDiseasesEd.setText(getString(R.string.no_data_found));
                    }
                    break;
            }
        });
    }

    private void getInsectAndControlData() {
        binding.progressBarInsect.setVisibility(View.VISIBLE);
        viewModel.getInsectAndControlLivedata().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case ERROR:
                    binding.progressBarInsect.setVisibility(View.GONE);
                    binding.rvInsectEd.setVisibility(View.GONE);
                    binding.tvInsectError.setVisibility(View.VISIBLE);
                    binding.tvInsectError.setText(resource.message);
                    break;
                case LOADING:
                    break;
                case SUCCESS:
                    binding.progressBarInsect.setVisibility(View.GONE);
                    if (resource.data != null) {
                        insectControlResponseList.clear();
                        insectControlResponseList.addAll(resource.data);
                        binding.rvInsectEd.setVisibility(View.VISIBLE);
                        setInsectControlRecyclerView();
                    } else {
                        binding.rvInsectEd.setVisibility(View.GONE);
                        binding.tvInsectError.setVisibility(View.VISIBLE);
                        binding.tvInsectError.setText(getString(R.string.no_data_found));
                    }
                    break;
            }
        });
    }


    private void setRecyclerView() {
        CropsAdapter cropsAdapter = new CropsAdapter(cropsResponseArrayList, this);
        binding.rvCropsEd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCropsEd.setAdapter(cropsAdapter);
    }

    private void setFruitsRecyclerView() {
        FruitsAdapter fruitsAdapter = new FruitsAdapter(fruitsResponseArrayList, this);
        binding.rvFruitsEd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvFruitsEd.setAdapter(fruitsAdapter);
    }

    private void setFlowersRecyclerView() {
        FlowersAdapter flowersAdapter = new FlowersAdapter(flowersResponseArrayList, this);
        binding.rvFlowersEd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvFlowersEd.setAdapter(flowersAdapter);
    }

    private void setExpandRecyclerView() {
        HowToExpandAdapter expandAdapter = new HowToExpandAdapter(expandResponseArrayList, this);
        binding.rvHowToExpandEd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvHowToExpandEd.setAdapter(expandAdapter);
    }

    private void setDiseasesRecyclerView() {
        DiseaseAdapter diseaseAdapter = new DiseaseAdapter(diseasesResponseArrayList, this);
        binding.rvDiseasesEd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvDiseasesEd.setAdapter(diseaseAdapter);
    }

    private void setInsectControlRecyclerView() {
        InsectControlAdapter insectControlAdapter = new InsectControlAdapter(insectControlResponseList, this);
        binding.rvInsectEd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvInsectEd.setAdapter(insectControlAdapter);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void onDiseaseItemClick(DiseasesResponse response) {
        Intent intent = new Intent(getContext(), DiseasesDetailsActivity.class);
        intent.putExtra("diseasesResponse", response);
        intent.putExtra("isDiseasesResponse", true);
        startActivity(intent);
    }

    @Override
    public void onInsectControlItemClick(InsectControlResponse insectControlResponse) {
        Intent intent = new Intent(getContext(), DiseasesDetailsActivity.class);
        intent.putExtra("insectControlResponse", insectControlResponse);
        intent.putExtra("isInsectControlResponse", true);
        startActivity(intent);
    }
}