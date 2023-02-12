package com.project.agroworld.ui.transport.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworld.R;
import com.project.agroworld.ui.transport.model.VehicleModel;
import com.project.agroworld.ui.transport.viewHolder.VehicleViewHolder;

import java.util.ArrayList;
import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleViewHolder> {

    private List<VehicleModel> vehicleItemList;
    private final OnVehicleCallClick listener;

    public VehicleAdapter(List<VehicleModel> vehicleItemList, OnVehicleCallClick clickListener) {
        this.vehicleItemList = vehicleItemList;
        this.listener = clickListener;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transport_item_layout, parent, false);
        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        VehicleModel vehicleModel = vehicleItemList.get(position);
        holder.setData(vehicleModel, listener);
    }

    @Override
    public int getItemCount() {
        return vehicleItemList.size();
    }

    public void searchInVehicleList(ArrayList<VehicleModel> searchProductList) {
        vehicleItemList = searchProductList;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        vehicleItemList.remove(position);
        notifyItemRemoved(position);
    }
}
