package com.project.agroworldapp.shopping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.agroworldapp.R;
import com.project.agroworldapp.databinding.ActivityProductDetailBinding;
import com.project.agroworldapp.shopping.model.ProductModel;
import com.project.agroworldapp.utils.Constants;

import java.util.Objects;

public class ProductDetailActivity extends AppCompatActivity {
    private final int doubleButtonTap = 0;
    FirebaseAuth auth;
    FirebaseUser user;
    private ActivityProductDetailBinding binding;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        Log.d("customProgressCycle", "ProductDetailActivity- onCreate");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Intent intent = getIntent();
        ProductModel productModel = ((ProductModel) intent.getSerializableExtra("productModel"));
        updateUI(productModel);
    }

    private void updateUI(ProductModel productModel) {
        actionBar.setTitle(productModel.getTitle());
        Glide.with(binding.ivProductDetailView).load(productModel.getImageUrl()).into(binding.ivProductDetailView);
        binding.tvPriceDetail.setText(getString(R.string.price) + "- ₹ " + productModel.getPrice());
        binding.tvSeedTitleDetail.setText(productModel.getTitle());
        binding.tvPrice.setText("₹ " + productModel.getPrice());
        binding.tvProductDescription.setText(productModel.getDescription().replaceAll("~", "\n\n"));

        binding.btnBuyNow.setOnClickListener(v -> {
            if (binding.btnBuyNow.getText().toString().contains(getString(R.string.add_to_cart))) {
                addItemToCart(productModel);
            } else {
                startActivity(new Intent(ProductDetailActivity.this, AddToCartActivity.class));
            }
        });
    }

    private void addItemToCart(ProductModel productModel) {
        DatabaseReference database;
        if (Constants.selectedLanguage(this))
            database = FirebaseDatabase.getInstance().getReference(Constants.plainStringEmail(Objects.requireNonNull(user.getEmail())) + "-LocalizedCartItems");
        else
            database = FirebaseDatabase.getInstance().getReference(Constants.plainStringEmail(Objects.requireNonNull(user.getEmail())) + "-CartItems");

        database.child(productModel.getTitle()).setValue(productModel).addOnSuccessListener(unused -> {
            Constants.showToast(ProductDetailActivity.this, "Item added to cart");
            binding.btnBuyNow.setText(getString(R.string.go_to_cart));
        }).addOnFailureListener(e -> Constants.showToast(ProductDetailActivity.this, "Failed to add item"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 201) {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}