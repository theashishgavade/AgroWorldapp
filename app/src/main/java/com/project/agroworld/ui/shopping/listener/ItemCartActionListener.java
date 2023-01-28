package com.project.agroworld.ui.shopping.listener;

import com.project.agroworld.ui.shopping.model.ProductModel;

public interface ItemCartActionListener {
    void onIncreaseItemClick(ProductModel productModel);
    void onDecreaseItemClick(ProductModel productModel);

    void onRemovedItemClick(ProductModel productModel, int position);

}
