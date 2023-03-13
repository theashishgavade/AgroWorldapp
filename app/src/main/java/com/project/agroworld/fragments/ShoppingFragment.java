package com.project.agroworld.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DatabaseReference;
import com.project.agroworld.R;
import com.project.agroworld.databinding.FragmentShoppingBinding;
import com.project.agroworld.ui.payment.PaymentHistoryActivity;
import com.project.agroworld.ui.shopping.activity.AddToCartActivity;
import com.project.agroworld.ui.shopping.activity.ProductDetailActivity;
import com.project.agroworld.ui.shopping.adapter.ProductAdapter;
import com.project.agroworld.ui.shopping.listener.OnProductListener;
import com.project.agroworld.ui.shopping.model.ProductModel;
import com.project.agroworld.viewmodel.AgroViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ShoppingFragment extends Fragment implements OnProductListener {

    private FragmentShoppingBinding binding;
    private DatabaseReference databaseReference;
    private final ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
    private ProductAdapter productAdapter;
    private AgroViewModel agroViewModel;

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
        agroViewModel = ViewModelProviders.of(this).get(AgroViewModel.class);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.hide();
        agroViewModel.init();
        getProductListFromFirebase();
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

            // Inflating popup menu from popup_menu.xml file
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
                        startActivity(new Intent(getContext(), AddToCartActivity.class));
                         return true;
                    case R.id.menuHistoryActivity:
                        startActivity(new Intent(getContext(), PaymentHistoryActivity.class));
                        return true;
                }
                return true;
            });
            // Showing the popup menu
            popupMenu.show();
        });

    }

    private void getProductListFromFirebase() {
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
                        binding.tvNoDataFoundErr.setText("Looks like Admin haven't added any item yet.");
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.priority_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.high: {
                Toast.makeText(getContext(), "High  ", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.medium: {
                Toast.makeText(getContext(), "medium  ", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.low: {
                Toast.makeText(getContext(), "Low  ", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProductClick(ProductModel productModel) {
        Intent intent = new Intent(requireContext(), ProductDetailActivity.class);
        intent.putExtra("productModel", productModel);
        startActivity(intent);
    }
}