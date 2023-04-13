package com.project.agroworldapp.manufacture.viewholder;

import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworldapp.databinding.ProductItemLayoutBinding;
import com.project.agroworldapp.shopping.listener.OnProductListener;
import com.project.agroworldapp.shopping.model.ProductModel;
import com.project.agroworldapp.utils.Constants;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    final ProductItemLayoutBinding binding;

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