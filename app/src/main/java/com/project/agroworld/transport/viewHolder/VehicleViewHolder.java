package com.project.agroworld.transport.viewHolder;

import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworld.R;
import com.project.agroworld.databinding.TransportItemLayoutBinding;
import com.project.agroworld.transport.adapter.OnVehicleCallClick;
import com.project.agroworld.transport.model.VehicleModel;
import com.project.agroworld.utils.Constants;

public class VehicleViewHolder extends RecyclerView.ViewHolder {
    TransportItemLayoutBinding binding;

    public VehicleViewHolder(TransportItemLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void setData(VehicleModel vehicleModel, OnVehicleCallClick vehicleCallClick) {

        binding.tvVehicleName.setText(vehicleModel.getModel());
        binding.tvVehicleLocation.setText(vehicleModel.getAddress());
        Constants.bindImage(
                binding.ivTransport, vehicleModel.getImageUrl(), binding.ivTransport
        );
        binding.tvVehiclePrice.setText("₹ " + vehicleModel.getRates());
        binding.btnVehicleOwnerCall.setOnClickListener(v -> vehicleCallClick.callVehicleOwner(vehicleModel));
    }
}