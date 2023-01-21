package com.project.agroworld.ui.product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworld.R;

import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    private List<ProductModelDashboard> moviesModelList;
    private OnProductListener listener;

    public ProductAdapter(List<ProductModelDashboard> moviesModelList, OnProductListener clickListener) {
        this.moviesModelList = moviesModelList;
        this.listener = clickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModelDashboard moviesModel = moviesModelList.get(position);
        holder.setData(moviesModel, listener);
    }

    @Override
    public int getItemCount() {
        return moviesModelList.size();
    }
}
