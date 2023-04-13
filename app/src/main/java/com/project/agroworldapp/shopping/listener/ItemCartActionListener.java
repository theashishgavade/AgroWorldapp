package com.project.agroworldapp.shopping.listener;

import com.project.agroworldapp.shopping.model.ProductModel;

public interface ItemCartActionListener {
    void onIncreaseItemClick(ProductModel productModel, int quantity);

    void onDecreaseItemClick(ProductModel productModel, int quantity);

    void onRemovedItemClick(ProductModel productModel, int position);

}
