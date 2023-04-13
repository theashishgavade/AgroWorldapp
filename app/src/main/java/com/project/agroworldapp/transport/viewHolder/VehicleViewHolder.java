package com.project.agroworldapp.transport.viewHolder;

import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworldapp.databinding.TransportItemLayoutBinding;
import com.project.agroworldapp.transport.adapter.OnVehicleCallClick;
import com.project.agroworldapp.transport.model.VehicleModel;
import com.project.agroworldapp.utils.Constants;

public class VehicleViewHolder extends RecyclerView.ViewHolder {
    final TransportItemLayoutBinding binding;

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
        binding.tvVehiclePrice.setText("₹ " + vehicleModel.getRates() + "/" + vehicleModel.getUnit());
        binding.btnVehicleOwnerCall.setOnClickListener(v -> vehicleCallClick.callVehicleOwner(vehicleModel));
    }
}