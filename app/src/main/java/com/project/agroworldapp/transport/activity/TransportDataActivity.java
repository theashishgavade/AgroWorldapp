package com.project.agroworldapp.transport.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.agroworldapp.R;
import com.project.agroworldapp.databinding.ActivityTransportDataBinding;
import com.project.agroworldapp.transport.adapter.VehicleAdapter;
import com.project.agroworldapp.transport.listener.AdminListener;
import com.project.agroworldapp.transport.model.VehicleModel;
import com.project.agroworldapp.ui.repository.AgroWorldRepositoryImpl;
import com.project.agroworldapp.utils.Constants;
import com.project.agroworldapp.utils.Permissions;
import com.project.agroworldapp.viewmodel.AgroViewModel;
import com.project.agroworldapp.viewmodel.AgroWorldViewModelFactory;

import java.util.ArrayList;

public class TransportDataActivity extends AppCompatActivity implements AdminListener {
    private final ArrayList<VehicleModel> vehicleItemList = new ArrayList<>();
    private ActivityTransportDataBinding binding;
    private VehicleAdapter vehicleAdapter;
    private AgroViewModel agroViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transport_data);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initializeAgroWorldViewModel();
        if (Permissions.checkConnection(this)) {
            getVehicleListFromFirebase();
        }
        binding.ivSearch.setOnClickListener(v -> {
            binding.tvUsername.setVisibility(View.GONE);
            binding.ivSearch.setVisibility(View.GONE);
            binding.searchBar.setVisibility(View.VISIBLE);
        });

        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                binding.searchBar.setVisibility(View.GONE);
                binding.tvUsername.setVisibility(View.VISIBLE);
                binding.ivSearch.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProduct(newText);
                return false;
            }
        });
    }

    private void getVehicleListFromFirebase() {
        binding.shimmer.startShimmer();
        agroViewModel.getVehicleModelLivedata();
        agroViewModel.observeTransportResourceLiveData.observe(this, vehicleModelResource -> {
            switch (vehicleModelResource.status) {
                case ERROR:
                    binding.shimmer.stopShimmer();
                    binding.shimmer.setVisibility(View.GONE);
                    binding.recyclerViewVehicle.setVisibility(View.GONE);
                    binding.tvNoDataFoundErr.setVisibility(View.VISIBLE);
                    binding.tvNoDataFoundErr.setText(vehicleModelResource.message);
                    break;
                case LOADING:
                    binding.shimmer.startShimmer();
                    break;
                case SUCCESS:
                    if (vehicleModelResource.data != null) {
                        vehicleItemList.clear();
                        vehicleItemList.addAll(vehicleModelResource.data);
                        binding.shimmer.stopShimmer();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.tvNoDataFoundErr.setVisibility(View.GONE);
                        binding.recyclerViewVehicle.setVisibility(View.VISIBLE);
                        setRecyclerView();
                    } else {
                        vehicleItemList.clear();
                        binding.shimmer.stopShimmer();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.tvNoDataFoundErr.setVisibility(View.VISIBLE);
                        binding.tvNoDataFoundErr.setText(getText(R.string.no_data_found));
                        binding.recyclerViewVehicle.setVisibility(View.GONE);
                    }
                    break;
            }
        });
    }

    private void setRecyclerView() {
        vehicleAdapter = new VehicleAdapter(this, vehicleItemList, this, 0);
        binding.recyclerViewVehicle.setAdapter(vehicleAdapter);
        binding.recyclerViewVehicle.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewVehicle.setHasFixedSize(true);
    }


    private void searchProduct(String query) {
        ArrayList<VehicleModel> searchProductList = new ArrayList<VehicleModel>();
        for (int i = 0; i < vehicleItemList.size(); i++) {
            if (vehicleItemList.get(i).getModel().toLowerCase().contains(query.toLowerCase())) {
                searchProductList.add(vehicleItemList.get(i));
            }
        }
        if (vehicleItemList.isEmpty()) {
            Constants.showToast(this, "No product found");
        } else {
            vehicleAdapter.searchInVehicleList(searchProductList);
        }
    }

    public void initializeAgroWorldViewModel() {
        AgroWorldRepositoryImpl agroWorldRepository = new AgroWorldRepositoryImpl();
        agroViewModel = ViewModelProviders.of(this, new AgroWorldViewModelFactory(agroWorldRepository, this)).get(AgroViewModel.class);
    }

    @Override
    public void performOnCardClickAction(VehicleModel vehicleModel) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + vehicleModel.getContact()));
        startActivity(intent);
    }

    @Override
    public void performEditAction(VehicleModel vehicleModel) {
        Intent intent = new Intent(this, TransportActivity.class);
        intent.putExtra("vehicleModel", vehicleModel);
        intent.putExtra("isActionWithData", true);
        startActivity(intent);
        finish();
    }

    @Override
    public void performDeleteAction(VehicleModel vehicleModel) {
        performVehicleRemovalAction(vehicleModel.getModel());
    }

    private void performVehicleRemovalAction(String model) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TransportDataActivity.this);
        alertDialog.setTitle(R.string.remove_product_dialog_msg);
        alertDialog.setMessage(getString(R.string.vehicle_remove_message));
        alertDialog.setIcon(R.drawable.app_icon4);
        alertDialog.setPositiveButton(R.string.remove, (dialog, which) -> {
            agroViewModel.performVehicleRemovalAction(model);
            agroViewModel.observeVehicleRemovalLivedata.observe(this, stringResource -> {
                switch (stringResource.status) {
                    case ERROR:
                        Constants.showToast(this, getString(R.string.failed_to_remove_prodcut));
                        break;
                    case LOADING:
                        break;
                    case SUCCESS:
                        Constants.showToast(this, "Successfully removed item from list.");
                        getVehicleListFromFirebase();
                        vehicleAdapter.notifyDataSetChanged();
                        break;
                }
            });
        });

        alertDialog.setNegativeButton(R.string.cancel, (dialog, which) -> {
            dialog.dismiss();
        });
        alertDialog.show();
    }
}