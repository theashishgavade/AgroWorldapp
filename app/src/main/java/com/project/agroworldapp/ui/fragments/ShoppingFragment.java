package com.project.agroworldapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.agroworldapp.BuildConfig;
import com.project.agroworldapp.R;
import com.project.agroworldapp.databinding.FragmentShoppingBinding;
import com.project.agroworldapp.db.PreferenceHelper;
import com.project.agroworldapp.manufacture.adapter.ProductAdapter;
import com.project.agroworldapp.payment.activities.PaymentHistoryActivity;
import com.project.agroworldapp.shopping.activity.AddToCartActivity;
import com.project.agroworldapp.shopping.activity.ProductDetailActivity;
import com.project.agroworldapp.shopping.listener.OnProductListener;
import com.project.agroworldapp.shopping.model.ProductModel;
import com.project.agroworldapp.ui.repository.AgroWorldRepositoryImpl;
import com.project.agroworldapp.utils.Constants;
import com.project.agroworldapp.utils.Permissions;
import com.project.agroworldapp.utils.Resource;
import com.project.agroworldapp.viewmodel.AgroViewModel;
import com.project.agroworldapp.viewmodel.AgroWorldViewModelFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ShoppingFragment extends Fragment implements OnProductListener {
    private final ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
    private PreferenceHelper preferenceHelper;
    private FragmentShoppingBinding binding;
    private ProductAdapter productAdapter;
    private AgroViewModel agroViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shopping, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeAgroWorldViewModel();
        preferenceHelper = PreferenceHelper.getInstance(getContext());

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
                        Comparator<ProductModel> lowComparator = (item1, item2) -> {
                            if (item1.getPrice() < item2.getPrice()) {
                                return -1;
                            } else if (item1.getPrice() > item2.getPrice()) {
                                return 1;
                            } else {
                                return 0;
                            }
                        };
                        Collections.sort(productModelArrayList, lowComparator);
                        productAdapter.notifyDataSetChanged();
                        return true;
                    case R.id.menuHighToLow:
                        Comparator<ProductModel> highComparator = (item1, item2) -> {
                            if (item1.getPrice() > item2.getPrice()) {
                                return -1;
                            } else if (item1.getPrice() < item2.getPrice()) {
                                return 1;
                            } else {
                                return 0;
                            }
                        };
                        Collections.sort(productModelArrayList, highComparator);
                        productAdapter.notifyDataSetChanged();
                        return true;
                }
                return true;
            });
            // Showing the popup menu
            popupMenu.show();
        });

        binding.ivCart.setOnClickListener(v -> {
            startActivityForResult(new Intent(getContext(), AddToCartActivity.class), Constants.REQUEST_CODE);
        });

        binding.ivHistoy.setOnClickListener(v -> {
            startActivityForResult(new Intent(getContext(), PaymentHistoryActivity.class), Constants.REQUEST_CODE);
        });
    }

    private void initializeAgroWorldViewModel() {
        AgroWorldRepositoryImpl agroWorldRepository = new AgroWorldRepositoryImpl();
        agroViewModel = ViewModelProviders.of(this, new AgroWorldViewModelFactory(agroWorldRepository, getContext())).get(AgroViewModel.class);
    }

    private void getProductListFromFirebase() {
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmer();
        LiveData<Resource<List<ProductModel>>> observeProductFirebaseLivedata;
        boolean selectedAppLanguage = preferenceHelper.getData(Constants.HINDI_KEY);
        if (selectedAppLanguage) {
            agroViewModel.getLocalizedProductDataList();
            observeProductFirebaseLivedata = agroViewModel.observeLocalizedProductLivedata;
        } else {
            agroViewModel.getProductModelLivedata();
            observeProductFirebaseLivedata = agroViewModel.observeProductLivedata;
        }

        observeProductFirebaseLivedata.observe(getViewLifecycleOwner(), productModelResource -> {
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
                        binding.shimmer.stopShimmer();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.tvNoDataFoundErr.setVisibility(View.VISIBLE);
                        binding.tvNoDataFoundErr.setText(getString(R.string.no_data_found));
                    }
                    break;
            }
        });
    }

    private void setRecyclerView() {
        productAdapter = new ProductAdapter(productModelArrayList, ShoppingFragment.this, 1);
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