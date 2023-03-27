package com.project.agroworld.manufacture.viewholder;

import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworld.databinding.ProductItemLayoutBinding;
import com.project.agroworld.shopping.listener.OnProductListener;
import com.project.agroworld.shopping.model.ProductModel;
import com.project.agroworld.utils.Constants;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    ProductItemLayoutBinding binding;
    public ProductViewHolder(ProductItemLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void setData(ProductModel productModel, OnProductListener clickListener) {
        binding.tvProductName.setText(productModel.getTitle());
        binding.tvProductPrice.setText("₹" + productModel.getPrice());
        Constants.bindImage(
                binding.ivProductImage,
                productModel.getImageUrl(),
                binding.ivProductImage
        );
        binding.crdProductView.setOnClickListener(v -> clickListener.onProductClick(productModel));

    }
}