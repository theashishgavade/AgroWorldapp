package com.project.agroworldapp.shopping.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworldapp.databinding.CartItemLayoutBinding;
import com.project.agroworldapp.shopping.listener.ItemCartActionListener;
import com.project.agroworldapp.shopping.model.ProductModel;
import com.project.agroworldapp.shopping.viewholder.CartViewHolder;

import java.util.List;

public class ProductCartAdapter extends RecyclerView.Adapter<CartViewHolder> {
    private final List<ProductModel> productModelArrayList;
    private final ItemCartActionListener listener;

    public ProductCartAdapter(List<ProductModel> productModelList, ItemCartActionListener listener) {
        this.productModelArrayList = productModelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(CartItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        ProductModel productModel = productModelArrayList.get(position);
        holder.bindItemData(productModel, listener);
    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }
}
