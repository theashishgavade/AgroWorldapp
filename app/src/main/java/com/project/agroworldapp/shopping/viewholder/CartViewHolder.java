package com.project.agroworldapp.shopping.viewholder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworldapp.databinding.CartItemLayoutBinding;
import com.project.agroworldapp.shopping.listener.ItemCartActionListener;
import com.project.agroworldapp.shopping.model.ProductModel;
import com.project.agroworldapp.utils.Constants;

public class CartViewHolder extends RecyclerView.ViewHolder {

    private final CartItemLayoutBinding binding;
    private int itemCount = 1;

    public CartViewHolder(@NonNull CartItemLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindItemData(ProductModel productModel, ItemCartActionListener listener) {

        binding.ivIncreaseCount.setOnClickListener(v -> {
            if (itemCount == 20) {
                Constants.showToast(v.getContext(), "Can't order more than 20 items at a time");
            } else {
                itemCount++;
                binding.tvItemCount.setText(String.valueOf(itemCount));
                listener.onIncreaseItemClick(productModel, itemCount);
            }

        });

        binding.ivDecreaseCount.setOnClickListener(v -> {
            if (itemCount == 1) {
                Constants.showToast(v.getContext(), "Default value");
            } else {
                itemCount--;
                binding.tvItemCount.setText(String.valueOf(itemCount));
                listener.onDecreaseItemClick(productModel, itemCount);
            }
        });

        binding.ivRemoveItem.setOnClickListener(v -> listener.onRemovedItemClick(productModel, getAbsoluteAdapterPosition()));

        binding.tvProductPrice.setText("₹" + productModel.getPrice());
        binding.tvProductName.setText(productModel.getTitle());
        Constants.bindImage(binding.getRoot(), productModel.getImageUrl(), binding.ivProductImage);

    }
}
