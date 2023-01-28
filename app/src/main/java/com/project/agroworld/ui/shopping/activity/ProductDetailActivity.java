package com.project.agroworld.ui.shopping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.agroworld.R;
import com.project.agroworld.databinding.ActivityProductDetailBinding;
import com.project.agroworld.ui.shopping.model.ProductModel;
import com.project.agroworld.utils.Constants;

public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailBinding binding;
    private DatabaseReference database;
    private ProductModel productModel;
    private int doubleButtonTap = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        productModel = ((ProductModel) intent.getSerializableExtra("productModel"));
        updateUI(productModel);
    }

    private void updateUI(ProductModel productModel) {
        Glide.with(binding.ivProductDetailView).load(productModel.getImageUrl()).into(binding.ivProductDetailView);
        binding.tvPriceDetail.setText("Price- ₹ " + productModel.getPrice());
        binding.tvSeedTitleDetail.setText(productModel.getTitle());
        binding.tvPrice.setText("₹ " + productModel.getPrice());
        binding.tvProductDescription.setText(productModel.getDescription().replaceAll("~", "\n\n"));
        binding.btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doubleButtonTap % 2 == 0){
                    binding.btnBuyNow.setText("Add To Cart");
                    addItemToCart(productModel);
                }else {
                    binding.btnBuyNow.setText("Go To Cart");
                    startActivity(new Intent(ProductDetailActivity.this, AddToCartActivity.class));
                }
                doubleButtonTap++;
            }
        });
    }

    private void addItemToCart(ProductModel productModel){
        database = FirebaseDatabase.getInstance().getReference("CartItemList");
        database.child(productModel.getTitle()).setValue(productModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Constants.showToast(ProductDetailActivity.this, "Item added to cart");
                binding.btnBuyNow.setText("Go To Cart");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Constants.showToast(ProductDetailActivity.this, "Failed to add item");
            }
        });
    }
}