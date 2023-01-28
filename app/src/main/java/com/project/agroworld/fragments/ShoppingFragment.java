package com.project.agroworld.fragments;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.agroworld.R;
import com.project.agroworld.databinding.FragmentShoppingBinding;
import com.project.agroworld.ui.shopping.model.ProductModel;
import com.project.agroworld.ui.shopping.listener.OnProductListener;
import com.project.agroworld.ui.shopping.adapter.ProductAdapter;
import com.project.agroworld.ui.shopping.activity.ProductDetailActivity;
import com.project.agroworld.utils.Constants;

import java.util.ArrayList;


public class ShoppingFragment extends Fragment implements OnProductListener {

    private FragmentShoppingBinding binding;
    private DatabaseReference databaseReference;
    private final ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
    private ProductAdapter productAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shopping, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.hide();
        getProductListFromFirebase();

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

    private void getProductListFromFirebase() {
        binding.shimmer.startShimmer();
        databaseReference = FirebaseDatabase.getInstance().getReference("product");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    productModelArrayList.clear();
                    for (DataSnapshot product : snapshot.getChildren()) {
                        ProductModel productItem = product.getValue(ProductModel.class);
                        if (productItem != null) {
                            productModelArrayList.add(productItem);
                        }
                    }

                    Log.d("productListsCount", String.valueOf(productModelArrayList.size()));
                    binding.shimmer.stopShimmer();
                    binding.shimmer.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    setRecyclerView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Constants.showToast(getContext(), error.toString());
            }
        });
    }

    private void setRecyclerView() {
        productAdapter = new ProductAdapter(productModelArrayList, ShoppingFragment.this);
        binding.recyclerView.setAdapter(productAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setHasFixedSize(true);
    }

    private void searchProduct(String query) {
        ArrayList<ProductModel> searchProductList = new ArrayList<ProductModel>();
        for (int i = 0; i < productModelArrayList.size(); i++) {
            if (productModelArrayList.get(i).getTitle().toLowerCase().contains(query.toLowerCase())) {
                searchProductList.add(productModelArrayList.get(i));
            }
        }
        if (productModelArrayList.isEmpty()) {
            Constants.showToast(requireContext(), "No product found");
        } else {
            productAdapter.searchInProductList(searchProductList);
        }
    }

    @Override
    public void onProductClick(ProductModel productModel) {
        Intent intent = new Intent(requireContext(), ProductDetailActivity.class);
        intent.putExtra("productModel", productModel);
        startActivity(intent);
    }
}