package com.ecocovoit.ecocovoit.entities;

import com.ecocovoit.ecocovoit.entities.geo.Location;

import java.util.Date;

public interface Ride extends Transferable, RemoteEntity, LocalEntity {

    Location getStartLocation();
    Date getStartTime();

    Location getEndLocation();
    Date getEndTime();

    RideProvider getRideProvider();
    RideUser getRideUser();

    TransportMean getTransportMean();

}
