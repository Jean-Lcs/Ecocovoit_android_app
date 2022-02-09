package com.ecocovoit.ecocovoit.entities;

import java.util.List;

public interface MultiRideProvider extends RideProvider {

    List<RideProvider> getProviders();
    void setProviders(List<RideProvider> providers);

}
