package com.project.agroworld.shopping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworld.R;
import com.project.agroworld.shopping.listener.OnProductListener;
import com.project.agroworld.shopping.model.ProductModel;
import com.project.agroworld.shopping.viewholder.ProductViewHolder;

import java.util.ArrayList;
import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    private List<ProductModel> productModelList;
    private final OnProductListener listener;

    public ProductAdapter(List<ProductModel> productModelList, OnProductListener clickListener) {
        this.productModelList = productModelList;
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
        ProductModel productModel = productModelList.get(position);
        holder.setData(productModel, listener);
    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public void searchInProductList(ArrayList<ProductModel> searchProductList) {
        productModelList = searchProductList;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        productModelList.remove(position);
        notifyItemRangeChanged(position, productModelList.size());
    }
}
