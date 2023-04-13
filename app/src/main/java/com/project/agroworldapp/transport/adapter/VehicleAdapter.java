package com.project.agroworldapp.transport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworldapp.databinding.TransportAdminLayoutBinding;
import com.project.agroworldapp.databinding.TransportItemLayoutBinding;
import com.project.agroworldapp.transport.listener.AdminListener;
import com.project.agroworldapp.transport.model.VehicleModel;
import com.project.agroworldapp.transport.viewHolder.VehicleAdminViewHolder;
import com.project.agroworldapp.transport.viewHolder.VehicleViewHolder;

import java.util.ArrayList;
import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter {
    private AdminListener adminListener;
    private OnVehicleCallClick listener;
    private List<VehicleModel> vehicleItemList;
    private Context context;
    private final int type;

    public VehicleAdapter(List<VehicleModel> vehicleItemList, OnVehicleCallClick clickListener, int type) {
        this.vehicleItemList = vehicleItemList;
        this.listener = clickListener;
        this.type = type;
    }

    public VehicleAdapter(Context context, List<VehicleModel> vehicleModelList, AdminListener adminListener, int type) {
        this.context = context;
        this.vehicleItemList = vehicleModelList;
        this.adminListener = adminListener;
        this.type = type;
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new VehicleAdminViewHolder(TransportAdminLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new VehicleViewHolder(TransportItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VehicleModel vehicleModel = vehicleItemList.get(position);
        if (holder instanceof VehicleAdminViewHolder) {
            ((VehicleAdminViewHolder) holder).setData(vehicleModel, adminListener, context);
        } else {
            ((VehicleViewHolder) holder).setData(vehicleModel, listener);
        }
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
