package com.project.agroworld.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.agroworld.R;
import com.project.agroworld.databinding.FragmentShoppingBinding;
import com.project.agroworld.payment.activities.PaymentHistoryActivity;
import com.project.agroworld.shopping.activity.AddToCartActivity;
import com.project.agroworld.shopping.activity.ProductDetailActivity;
import com.project.agroworld.shopping.adapter.ProductAdapter;
import com.project.agroworld.shopping.listener.OnProductListener;
import com.project.agroworld.shopping.model.ProductModel;
import com.project.agroworld.utils.Constants;
import com.project.agroworld.utils.Permissions;
import com.project.agroworld.viewmodel.AgroViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ShoppingFragment extends Fragment implements OnProductListener {
    private FragmentShoppingBinding binding;
    private final ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
    private ProductAdapter productAdapter;
    private AgroViewModel agroViewModel;

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
        agroViewModel = ViewModelProviders.of(this).get(AgroViewModel.class);
        agroViewModel.init();

        if (Permissions.checkConnection(getContext())) {
            binding.tvNoDataFoundErr.setVisibility(View.GONE);
            getProductListFromFirebase();
        } else {
            binding.recyclerView.setVisibility(View.GONE);
            binding.shimmer.setVisibility(View.GONE);
            binding.tvNoDataFoundErr.setVisibility(View.VISIBLE);
            binding.tvNoDataFoundErr.setText(getString(R.string.check_internet_connection));
        }
        binding.searchBar.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProduct(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProduct(newText);
                return false;
            }
        });

        binding.ivMoreOption.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), binding.ivMoreOption);
            popupMenu.getMenuInflater().inflate(R.menu.shopping_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                // Toast message on menu item clicked
                switch (menuItem.getItemId()) {
                    case R.id.menuLowToHigh:
                        Comparator<ProductModel> lowComparator = new Comparator<ProductModel>() {
                            @Override
                            public int compare(ProductModel item1, ProductModel item2) {
                                if (item1.getPrice() < item2.getPrice()) {
                                    return -1;
                                } else if (item1.getPrice() > item2.getPrice()) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            }
                        };
                        Collections.sort(productModelArrayList, lowComparator);
                        productAdapter.notifyDataSetChanged();
                        return true;
                    case R.id.menuHighToLow:
                        Comparator<ProductModel> highComparator = new Comparator<ProductModel>() {
                            @Override
                            public int compare(ProductModel item1, ProductModel item2) {
                                if (item1.getPrice() > item2.getPrice()) {
                                    return -1;
                                } else if (item1.getPrice() < item2.getPrice()) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            }
                        };
                        Collections.sort(productModelArrayList, highComparator);
                        productAdapter.notifyDataSetChanged();
                        return true;
                    case R.id.menuCartActivity:
                        startActivityForResult(new Intent(getContext(), AddToCartActivity.class), Constants.REQUEST_CODE);
                        return true;
                    case R.id.menuHistoryActivity:
                        startActivityForResult(new Intent(getContext(), PaymentHistoryActivity.class), Constants.REQUEST_CODE);
                        return true;
                }
                return true;
            });
            // Showing the popup menu
            popupMenu.show();
        });

    }

    private void getProductListFromFirebase() {
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmer();
        agroViewModel.getProductModelLivedata().observe(getViewLifecycleOwner(), productModelResource -> {
            switch (productModelResource.status) {
                case ERROR:
                    binding.shimmer.stopShimmer();
                    binding.shimmer.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.tvNoDataFoundErr.setVisibility(View.VISIBLE);
                    binding.tvNoDataFoundErr.setText(productModelResource.message);
                    break;
                case LOADING:
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
        if (searchProductList.isEmpty()) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.tvNoDataFoundErr.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.tvNoDataFoundErr.setVisibility(View.GONE);
            productAdapter.searchInProductList(searchProductList);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE) {
            getProductListFromFirebase();
        }
    }

    @Override
    public void onProductClick(ProductModel productModel) {
        Intent intent = new Intent(requireContext(), ProductDetailActivity.class);
        intent.putExtra("productModel", productModel);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }
}