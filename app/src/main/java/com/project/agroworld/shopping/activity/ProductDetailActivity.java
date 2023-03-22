package com.project.agroworld.shopping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.agroworld.R;
import com.project.agroworld.databinding.ActivityProductDetailBinding;
import com.project.agroworld.shopping.model.ProductModel;
import com.project.agroworld.utils.Constants;

public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailBinding binding;
    private DatabaseReference database;
    private ProductModel productModel;
    private int doubleButtonTap = 0;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Log.d("customProgressCycle", "ProductDetailActivity- onCreate");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Intent intent = getIntent();
        productModel = ((ProductModel) intent.getSerializableExtra("productModel"));
        updateUI(productModel);
    }

    private void updateUI(ProductModel productModel) {
        Glide.with(binding.ivProductDetailView).load(productModel.getImageUrl()).into(binding.ivProductDetailView);
        binding.tvPriceDetail.setText(getString(R.string.price) + "- ₹ " + productModel.getPrice());
        binding.tvSeedTitleDetail.setText(productModel.getTitle());
        binding.tvPrice.setText("₹ " + productModel.getPrice());
        binding.tvProductDescription.setText(productModel.getDescription().replaceAll("~", "\n\n"));
        binding.btnBuyNow.setOnClickListener(v -> {
            if (binding.btnBuyNow.getText().toString().contains(getString(R.string.add_to_cart))) {
                addItemToCart(productModel);
            } else {
                startActivityForResult(new Intent(ProductDetailActivity.this, AddToCartActivity.class), 120);
            }
        });
    }

    private void addItemToCart(ProductModel productModel) {
        database = FirebaseDatabase.getInstance().getReference(Constants.plainStringEmail(user.getEmail()) + "-CartItems");
        database.child(productModel.getTitle()).setValue(productModel).addOnSuccessListener(unused -> {
            Constants.showToast(ProductDetailActivity.this, "Item added to cart");
            binding.btnBuyNow.setText(getString(R.string.go_to_cart));
        }).addOnFailureListener(e -> Constants.showToast(ProductDetailActivity.this, "Failed to add item"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 120) {
            finish();
        }
    }
}