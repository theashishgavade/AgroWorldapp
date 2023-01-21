package com.project.agroworld.ui.product;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworld.R;

class ProductViewHolder extends RecyclerView.ViewHolder {

    private TextView tvProductName;
    private ImageView ivProductImage;
    private CardView crdProductView;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        initViews(itemView);
    }

    private void initViews(View itemView) {
        tvProductName = itemView.findViewById(R.id.tvProductName);
        ivProductImage = itemView.findViewById(R.id.ivProductImage);
        crdProductView = itemView.findViewById(R.id.crdProductView);
    }

    public void setData(ProductModelDashboard ProductModelDashboard, OnProductListener clickListener) {
//        tvProductName.setText(moviesModel.getMovieTitle());
//        ivProductImage.setImageResource(moviesModel.getThumbID());
        crdProductView.setRadius(7.8f);

        crdProductView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onProductClick();
            }
        });
    }
}