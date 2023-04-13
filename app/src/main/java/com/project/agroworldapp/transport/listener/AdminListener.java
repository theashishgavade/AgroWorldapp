package com.project.agroworldapp.transport.listener;

import com.project.agroworldapp.transport.model.VehicleModel;

public interface AdminListener {

    void performOnCardClickAction(VehicleModel vehicleModel);

    void performEditAction(VehicleModel vehicleModel);

    void performDeleteAction(VehicleModel vehicleModel);

}
