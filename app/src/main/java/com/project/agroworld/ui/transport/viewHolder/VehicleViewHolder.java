package com.project.agroworld.ui.transport.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.agroworld.R;
import com.project.agroworld.ui.transport.model.VehicleModel;
import com.project.agroworld.ui.transport.adapter.OnVehicleCallClick;

public class VehicleViewHolder extends RecyclerView.ViewHolder {

    private TextView tvVehicleName, tvVehicleLocation, tvVehiclePrice;
    private ImageView ivTransport;
    private CardView crdVehicleCard;

    public VehicleViewHolder(@NonNull View itemView) {
        super(itemView);
        initViews(itemView);
    }

    private void initViews(View itemView) {
        tvVehicleName = itemView.findViewById(R.id.tvVehicleName);
        tvVehicleLocation = itemView.findViewById(R.id.tvVehicleLocation);
        ivTransport = itemView.findViewById(R.id.ivTransport);
        tvVehiclePrice = itemView.findViewById(R.id.tvVehiclePrice);
        crdVehicleCard = itemView.findViewById(R.id.crdVehicleCard);

    }

    public void setData(VehicleModel vehicleModel, OnVehicleCallClick vehicleCallClick) {

        tvVehicleName.setText(vehicleModel.getModel());
        tvVehicleLocation.setText(vehicleModel.getAddress());
        Glide.with(ivTransport).load(vehicleModel.getImageUrl()).into(ivTransport);
        tvVehiclePrice.setText(R.string.price +"- ₹ " + vehicleModel.getRates());
        crdVehicleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicleCallClick.callVehicleOwner(vehicleModel);
            }
        });
    }
}