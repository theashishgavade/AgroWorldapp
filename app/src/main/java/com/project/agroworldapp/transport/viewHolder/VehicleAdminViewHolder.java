package com.project.agroworldapp.transport.viewHolder;

import android.content.Context;
import android.widget.PopupMenu;

import androidx.recyclerview.widget.RecyclerView;


import com.project.agroworldapp.R;
import com.project.agroworldapp.databinding.TransportAdminLayoutBinding;
import com.project.agroworldapp.transport.listener.AdminListener;
import com.project.agroworldapp.transport.model.VehicleModel;
import com.project.agroworldapp.utils.Constants;

public class VehicleAdminViewHolder extends RecyclerView.ViewHolder {
    final TransportAdminLayoutBinding binding;

    public VehicleAdminViewHolder(TransportAdminLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void setData(VehicleModel vehicleModel, AdminListener adminListener, Context context) {

        binding.tvVehicleName.setText(vehicleModel.getModel());
        binding.tvVehicleLocation.setText(vehicleModel.getAddress());
        Constants.bindImage(
                binding.ivTransport, vehicleModel.getImageUrl(), binding.ivTransport
        );
        binding.tvVehiclePrice.setText("₹ " + vehicleModel.getRates() + "/" + vehicleModel.getUnit());
        binding.btnVehicleOwnerCall.setOnClickListener(v -> adminListener.performOnCardClickAction(vehicleModel));

        binding.ivMoreOptionTransport.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, binding.ivMoreOptionTransport);
            popupMenu.getMenuInflater().inflate(R.menu.transport_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.editProductDetails:
                        adminListener.performEditAction(vehicleModel);
                        return true;
                    case R.id.deleteProductDetails:
                        adminListener.performDeleteAction(vehicleModel);
                        return true;
                }
                return true;
            });
            popupMenu.show();
        });
    }
}