package com.project.agroworld.transport.listener;

import com.project.agroworld.transport.model.VehicleModel;

public interface TransportAdminListener {

    void performCallAction(VehicleModel vehicleModel);

    void performEditAction(VehicleModel vehicleModel);

    void performDeleteAction(VehicleModel vehicleModel);

}
