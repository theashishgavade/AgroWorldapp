package com.project.agroworld.manufacture;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.agroworld.R;
import com.project.agroworld.databinding.ActivityManufactureDataBinding;
import com.project.agroworld.shopping.activity.ProductDetailActivity;
import com.project.agroworld.shopping.adapter.ProductAdapter;
import com.project.agroworld.shopping.listener.OnProductListener;
import com.project.agroworld.shopping.model.ProductModel;
import com.project.agroworld.viewmodel.AgroViewModel;
import com.project.agroworld.utils.Constants;

import java.util.ArrayList;

public class ManufactureDataActivity extends AppCompatActivity implements OnProductListener {

    private ActivityManufactureDataBinding binding;
    private DatabaseReference databaseReference;
    private final ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
    private ProductAdapter productAdapter;
    private AgroViewModel agroViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manufacture_data);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        agroViewModel = ViewModelProviders.of(this).get(AgroViewModel.class);
        agroViewModel.init(this);
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
        agroViewModel.getProductModelLivedata().observe(this, productModelResource -> {
            switch (productModelResource.status) {
                case ERROR:
                    binding.shimmer.stopShimmer();
                    binding.shimmer.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.tvNoDataFoundErr.setVisibility(View.VISIBLE);
                    binding.tvNoDataFoundErr.setText(productModelResource.message);
                    break;
                case LOADING:
                    binding.shimmer.startShimmer();
                    break;
                case SUCCESS:
                    if (productModelResource.data != null) {
                        productModelArrayList.clear();
                        productModelArrayList.addAll(productModelResource.data);
                        binding.shimmer.stopShimmer();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        setRecyclerView();
                    } else {
                        binding.tvNoDataFoundErr.setVisibility(View.VISIBLE);
                        binding.tvNoDataFoundErr.setText(getString(R.string.no_data_found));
                    }
                    break;
            }
        });
    }

    private void setRecyclerView() {
        productAdapter = new ProductAdapter(productModelArrayList, ManufactureDataActivity.this);
        binding.recyclerView.setAdapter(productAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Toast.makeText(ManufactureDataActivity.this, "On Move", Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAbsoluteAdapterPosition();

                if (productModelArrayList.isEmpty()) {
                    binding.tvNoDataFoundErr.setVisibility(View.VISIBLE);
                    binding.tvNoDataFoundErr.setText(getString(R.string.no_data_found));
                    binding.recyclerView.setVisibility(View.GONE);
                }
                onProductCardSwiped(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);
    }

    private void onProductCardSwiped(int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ManufactureDataActivity.this);
        alertDialog.setTitle("Are you sure,you want to remove product??");
        alertDialog.setMessage(getString(R.string.product_remove_message));
        alertDialog.setIcon(R.drawable.app_icon4);

        alertDialog.setPositiveButton("Remove", (dialog, which) -> {
            agroViewModel.removeProductFromFirebase(productModelArrayList.get(position).getTitle());
            getProductListFromFirebase();
            removeProductFromCartIfAdded(position);
            productAdapter.notifyDataSetChanged();
        });

        alertDialog.setNegativeButton("Cancel", (dialog, which) -> {
            productAdapter.notifyItemRemoved(position);
            productAdapter.notifyItemRangeChanged(position, productAdapter.getItemCount());
        });
        alertDialog.show();
    }

    private void removeProductFromCartIfAdded(int position) {
        databaseReference = FirebaseDatabase.getInstance().getReference("CartItemList");
        databaseReference.child(productModelArrayList.get(position).getTitle()).removeValue().addOnSuccessListener(unused -> {
            Log.d("removeCartItemTransport", "onSuccess");
            databaseReference = null;
        }).addOnFailureListener(command -> {
            Log.d("removeCartItemTransport", command.getLocalizedMessage());
            databaseReference = null;
        });
    }

    private void searchProduct(String query) {
        ArrayList<ProductModel> searchProductList = new ArrayList<ProductModel>();
        for (int i = 0; i < productModelArrayList.size(); i++) {
            if (productModelArrayList.get(i).getTitle().toLowerCase().contains(query.toLowerCase())) {
                searchProductList.add(productModelArrayList.get(i));
            }
        }
        if (productModelArrayList.isEmpty()) {
            Constants.showToast(this, getString(R.string.no_data_found));
        } else {
            productAdapter.searchInProductList(searchProductList);
        }
    }

    @Override
    public void onProductClick(ProductModel productModel) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("productModel", productModel);
        startActivity(intent);
    }
}