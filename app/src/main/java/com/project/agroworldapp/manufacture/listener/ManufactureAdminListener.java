package com.project.agroworldapp.manufacture.listener;

import com.project.agroworldapp.shopping.model.ProductModel;

public interface ManufactureAdminListener {
    void performOnCardClickAction(ProductModel productModel);

    void performEditAction(ProductModel productModel, int position);

    void performDeleteAction(ProductModel productModel, int position);
}
