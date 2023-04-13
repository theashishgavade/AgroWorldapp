package com.project.agroworldapp.taskmanager.viewholder;

import android.content.Context;
import android.view.Menu;
import android.widget.PopupMenu;

import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworldapp.databinding.FarmerItemLayoutBinding;
import com.project.agroworldapp.db.FarmerModel;
import com.project.agroworldapp.taskmanager.listener.OnItemClickListener;

public class FarmerViewHolder extends RecyclerView.ViewHolder {
    // view holder class to create a variable for each view.
    private final FarmerItemLayoutBinding binding;

    public FarmerViewHolder(FarmerItemLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindData(FarmerModel model, OnItemClickListener listener, Context context) {
        binding.tvRoutineDate.setText(model.getDate());
        binding.tvRoutineTime.setText(model.getTime());
        binding.tvRoutineDecs.setText(model.getDesc());
        binding.tvRoutineTitle.setText(model.getTask());

        binding.tvMenu.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(context, v);
            menu.getMenu().add(Menu.NONE, 1, 1, "Delete");
            menu.getMenu().add(Menu.NONE, 2, 2, "Completed");
            menu.show();
            menu.setOnMenuItemClickListener(item -> {
                int i = item.getItemId();
                if (i == 1) {
                    listener.onDeleteClick(model);
                    return true;
                } else if (i == 2) {
                    listener.markTaskCompleted(model);
                    return true;
                } else {
                    return false;
                }
            });
        });
    }
}