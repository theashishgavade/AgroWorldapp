package com.project.agroworld.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.agroworld.R;
import com.project.agroworld.databinding.FragmentTransportBinding;
import com.project.agroworld.ui.AgroViewModel;
import com.project.agroworld.ui.transport.model.VehicleModel;
import com.project.agroworld.ui.transport.adapter.OnVehicleCallClick;
import com.project.agroworld.ui.transport.adapter.VehicleAdapter;
import com.project.agroworld.utils.Constants;

import java.util.ArrayList;


public class TransportFragment extends Fragment implements OnVehicleCallClick {

    private FragmentTransportBinding binding;
    private final ArrayList<VehicleModel> vehicleItemList = new ArrayList<>();
    private VehicleAdapter vehicleAdapter;
    private AgroViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transport, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.hide();
        viewModel = new ViewModelProvider(this).get(AgroViewModel.class);
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
        binding.shimmerVehicle.startShimmer();
        viewModel.getVehicleList().observe(getViewLifecycleOwner(), vehicleModelsList -> {
            if (!vehicleModelsList.isEmpty()){
                vehicleItemList.addAll(vehicleModelsList);
            }
            Log.d("productListsCount", String.valueOf(vehicleItemList.size()));
            binding.shimmerVehicle.stopShimmer();
            binding.shimmerVehicle.setVisibility(View.GONE);
            binding.recyclerViewVehicle.setVisibility(View.VISIBLE);
            setRecyclerView();
        });
    }

    private void setRecyclerView() {
        vehicleAdapter = new VehicleAdapter(vehicleItemList, TransportFragment.this);
        binding.recyclerViewVehicle.setAdapter(vehicleAdapter);
        binding.recyclerViewVehicle.setLayoutManager(new LinearLayoutManager(getContext()));
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
            Constants.showToast(requireContext(), "No product found");
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