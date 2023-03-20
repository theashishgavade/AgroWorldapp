package com.project.agroworld.transport.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.agroworld.R;
import com.project.agroworld.databinding.ActivityTransportDataBinding;
import com.project.agroworld.transport.adapter.OnVehicleCallClick;
import com.project.agroworld.transport.adapter.VehicleAdapter;
import com.project.agroworld.transport.model.VehicleModel;
import com.project.agroworld.viewmodel.AgroViewModel;
import com.project.agroworld.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class TransportDataActivity extends AppCompatActivity implements OnVehicleCallClick {
    private ActivityTransportDataBinding binding;
    private DatabaseReference databaseReference;
    private final ArrayList<VehicleModel> vehicleItemList = new ArrayList<>();
    private VehicleAdapter vehicleAdapter;
    private AgroViewModel agroViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transport_data);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        agroViewModel = ViewModelProviders.of(this).get(AgroViewModel.class);
        agroViewModel.init();
        getVehicleListFromFirebase();

        binding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvUsername.setVisibility(View.GONE);
                binding.ivSearch.setVisibility(View.GONE);
                binding.searchBar.setVisibility(View.VISIBLE);
            }
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
        agroViewModel.getVehicleModelLivedata().observe(this, vehicleModelResource -> {
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
                        updateUI(vehicleModelResource.data);
                    } else {
                        binding.shimmer.stopShimmer();
                        binding.shimmer.setVisibility(View.GONE);
                    }
                    break;
            }
        });
    }

    private void updateUI(List<VehicleModel> vehicleModelList) {
        if (vehicleModelList.isEmpty()) {
            binding.shimmer.stopShimmer();
            binding.shimmer.setVisibility(View.GONE);
            binding.tvNoDataFoundErr.setVisibility(View.VISIBLE);
            binding.tvNoDataFoundErr.setText(getText(R.string.no_data_found));
            binding.recyclerViewVehicle.setVisibility(View.GONE);
        } else {
            vehicleItemList.clear();
            vehicleItemList.addAll(vehicleModelList);
            binding.shimmer.stopShimmer();
            binding.shimmer.setVisibility(View.GONE);
            binding.tvNoDataFoundErr.setVisibility(View.GONE);
            binding.recyclerViewVehicle.setVisibility(View.VISIBLE);
            setRecyclerView();
        }
    }

    private void setRecyclerView() {
        vehicleAdapter = new VehicleAdapter(vehicleItemList, this);
        binding.recyclerViewVehicle.setAdapter(vehicleAdapter);
        binding.recyclerViewVehicle.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewVehicle.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Toast.makeText(TransportDataActivity.this, "On Move", Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAbsoluteAdapterPosition();
                onVehicleCardSwiped(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewVehicle);
    }

    private void onVehicleCardSwiped(int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TransportDataActivity.this);
        alertDialog.setTitle(getString(R.string.remove_vehicle_message));
        alertDialog.setMessage(getString(R.string.vehicle_remove_message));
        alertDialog.setIcon(R.drawable.app_icon4);

        alertDialog.setPositiveButton("Remove", (dialog, which) -> {
            databaseReference = FirebaseDatabase.getInstance().getReference("vehicle");
            databaseReference.child(vehicleItemList.get(position).getModel()).removeValue().addOnSuccessListener(unused -> {
                Constants.showToast(TransportDataActivity.this, "Vehicle removed");
                vehicleAdapter.removeItem(position);
                if (vehicleItemList.isEmpty()) {
                    binding.tvNoDataFoundErr.setVisibility(View.VISIBLE);
                    binding.tvNoDataFoundErr.setText(getString(R.string.no_data_found));
                    binding.recyclerViewVehicle.setVisibility(View.GONE);
                }
            }).addOnFailureListener(e -> Constants.showToast(TransportDataActivity.this, getString(R.string.failed_to_remove_vehicle)));
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                vehicleAdapter.notifyItemRemoved(position );
                vehicleAdapter.notifyItemRangeChanged(position, vehicleAdapter.getItemCount());
            }
        });
        alertDialog.show();
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

    @Override
    public void callVehicleOwner(VehicleModel vehicleModel) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + vehicleModel.getContact()));
        startActivity(intent);
    }
}