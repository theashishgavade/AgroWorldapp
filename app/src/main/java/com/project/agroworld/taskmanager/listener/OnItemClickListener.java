package com.project.agroworld.taskmanager.listener;

import com.project.agroworld.db.FarmerModel;
public interface OnItemClickListener {
    void markTaskCompleted(FarmerModel model);

    void onDeleteClick(FarmerModel model);
}