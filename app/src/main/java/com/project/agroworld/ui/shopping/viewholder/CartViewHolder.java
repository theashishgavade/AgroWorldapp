package com.project.agroworld.ui.shopping.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.agroworld.databinding.CartItemLayoutBinding;
import com.project.agroworld.ui.shopping.listener.ItemCartActionListener;
import com.project.agroworld.ui.shopping.model.ProductModel;
import com.project.agroworld.utils.Constants;

public class CartViewHolder extends RecyclerView.ViewHolder{

    private final CartItemLayoutBinding binding;
    private int itemCount  = 1;
    public CartViewHolder(@NonNull CartItemLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindItemData(ProductModel productModel, ItemCartActionListener listener){

        binding.ivIncreaseCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemCount == 20){
                    Constants.showToast(v.getContext(), "Can't order more than 20 items at a time");
                }else{
                    itemCount++;
                    binding.tvItemCount.setText(String.valueOf(itemCount));
                    listener.onIncreaseItemClick(productModel);
                }

            }
        });

        binding.ivDecreaseCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemCount == 1){
                    Constants.showToast(v.getContext(), "Default value");
                }else  {
                    itemCount--;
                    binding.tvItemCount.setText(String.valueOf(itemCount));
                    listener.onDecreaseItemClick(productModel);
                }
            }
        });

        binding.ivRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRemovedItemClick(productModel, getAbsoluteAdapterPosition());
            }
        });

        binding.tvProductPrice.setText("₹" + productModel.getPrice());
        binding.tvProductName.setText(productModel.getTitle());
        Constants.bindImage(binding.getRoot(), productModel.getImageUrl(), binding.ivProductImage);

    }
}
