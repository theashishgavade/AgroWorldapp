package com.project.agroworld.manufacture.listener;

import com.project.agroworld.shopping.model.ProductModel;

public interface ManufactureAdminListener {
    void performOnCardClickAction(ProductModel productModel);

    void performEditAction(ProductModel productModel, int position);

    void performDeleteAction(ProductModel productModel, int position);
}
