package com.project.agroworld.transport.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworld.databinding.TransportItemLayoutBinding;
import com.project.agroworld.transport.model.VehicleModel;
import com.project.agroworld.transport.viewHolder.VehicleViewHolder;

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
        return new VehicleViewHolder(TransportItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
