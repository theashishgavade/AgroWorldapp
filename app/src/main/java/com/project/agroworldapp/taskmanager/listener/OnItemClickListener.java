package com.project.agroworldapp.taskmanager.listener;

import com.project.agroworldapp.db.FarmerModel;

public interface OnItemClickListener {
    void markTaskCompleted(FarmerModel model);

    void onDeleteClick(FarmerModel model);
}