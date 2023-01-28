package com.project.agroworld.ui.shopping.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.agroworld.R;
import com.project.agroworld.databinding.ActivityAddToCartBinding;
import com.project.agroworld.ui.shopping.adapter.ProductCartAdapter;
import com.project.agroworld.ui.shopping.listener.ItemCartActionListener;
import com.project.agroworld.ui.shopping.model.ProductModel;
import com.project.agroworld.utils.Constants;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AddToCartActivity extends AppCompatActivity implements ItemCartActionListener {

    private ActivityAddToCartBinding binding;
    private ArrayList<ProductModel> productCartList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private ProductCartAdapter productAdapter;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    ActionBar actionBar;
    private double totalItemAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_to_cart);
        actionBar = getSupportActionBar();
        actionBar.hide();
        getProductListFromFirebase();
        binding.tvAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.showToast(AddToCartActivity.this, "Proceed to add address");
            }
        });
    }

    private void getProductListFromFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("CartItemList");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    productCartList.clear();
                    totalItemAmount = 0.0;
                    for (DataSnapshot product : snapshot.getChildren()) {
                        ProductModel productItem = product.getValue(ProductModel.class);
                        productCartList.add(productItem);
                        totalItemAmount += productItem.getPrice();
                    }
                    if (productCartList.isEmpty()) {
                        binding.tvNoCartDataFound.setVisibility(View.VISIBLE);
                    }
                    binding.tvTotalAmount.setText("₹" + df.format(totalItemAmount));
                    setRecyclerView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Constants.showToast(AddToCartActivity.this, error.toString());
            }
        });
    }

    private void setRecyclerView() {
        productAdapter = new ProductCartAdapter(productCartList, AddToCartActivity.this);
        binding.recyclerViewCrtList.setAdapter(productAdapter);
        binding.recyclerViewCrtList.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewCrtList.setHasFixedSize(true);
    }

    @Override
    public void onIncreaseItemClick(ProductModel productModel) {
        String [] currentValue = binding.tvTotalAmount.getText().toString().split("₹");
        double currentDoubleValue = Double.valueOf(currentValue[1] )+ productModel.getPrice();
        binding.tvTotalAmount.setText("₹" + df.format(currentDoubleValue));
    }

    @Override
    public void onDecreaseItemClick(ProductModel productModel) {
        String [] currentValue = binding.tvTotalAmount.getText().toString().split("₹");
        double currentDoubleValue = Double.valueOf(currentValue[1]) - productModel.getPrice();
        binding.tvTotalAmount.setText("₹" + df.format(currentDoubleValue));
    }

    @Override
    public void onRemovedItemClick(ProductModel productModel, int position) {
        databaseReference.child(productModel.getTitle()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                totalItemAmount -= productModel.getPrice();
                productAdapter.notifyDataSetChanged();
                Constants.showToast(AddToCartActivity.this, "Item removed successfully price " + productModel.getPrice());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Constants.showToast(AddToCartActivity.this, "Failed to  removed item");
            }
        });
    }
}