package com.project.agroworld.ui.shopping.listener;

import com.project.agroworld.ui.shopping.model.ProductModel;

public interface ItemCartActionListener {
    void onIncreaseItemClick(ProductModel productModel, int quantity);
    void onDecreaseItemClick(ProductModel productModel, int quantity);

    void onRemovedItemClick(ProductModel productModel, int position);

}
