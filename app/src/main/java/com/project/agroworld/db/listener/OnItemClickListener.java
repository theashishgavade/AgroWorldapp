package com.project.agroworld.db.listener;

import com.project.agroworld.db.FarmerModel;
public interface OnItemClickListener {
    void markTaskCompleted(FarmerModel model);

    void onDeleteClick(FarmerModel model);
}