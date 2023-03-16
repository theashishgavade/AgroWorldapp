package com.project.agroworld.shopping.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.agroworld.R;
import com.project.agroworld.shopping.model.ProductModel;
import com.project.agroworld.shopping.listener.OnProductListener;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    private TextView tvProductName, tvProductPrice;
    private ImageView ivProductImage;
    private CardView crdProductView;
    private Button btnBuyNow;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        initViews(itemView);
    }

    private void initViews(View itemView) {
        tvProductName = itemView.findViewById(R.id.tvProductName);
        tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
        ivProductImage = itemView.findViewById(R.id.ivProductImage);
        crdProductView = itemView.findViewById(R.id.crdProductView);
        btnBuyNow = itemView.findViewById(R.id.btnViewMore);

    }

    public void setData(ProductModel productModel, OnProductListener clickListener) {

        tvProductName.setText(productModel.getTitle());
        tvProductPrice.setText("₹" + productModel.getPrice());

        Glide.with(ivProductImage).load(productModel.getImageUrl()).into(ivProductImage);
        crdProductView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onProductClick(productModel);
            }
        });

    }
}