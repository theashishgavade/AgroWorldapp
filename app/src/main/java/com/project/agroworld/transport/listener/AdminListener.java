package com.project.agroworld.transport.listener;

import com.project.agroworld.transport.model.VehicleModel;

public interface AdminListener {

    void performOnCardClickAction(VehicleModel vehicleModel);

    void performEditAction(VehicleModel vehicleModel);

    void performDeleteAction(VehicleModel vehicleModel);

}
