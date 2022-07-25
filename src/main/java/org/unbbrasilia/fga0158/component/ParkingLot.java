package org.unbbrasilia.fga0158.component;

import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class ParkingLot {

    /**
     * Park id of the company.
     */
    private final int parkingId;

    /**
     * List of VehicleAccess objects to be stored
     */
    private final List<VehicleAccess> carsList;

    /**
     * Maximum number of vehicles that can be stored in this place.
     */
    private final int maxVehicleCapacity;

    private final ParkingInformation pricingInformation;

    public ParkingLot(int parkId, CopyOnWriteArrayList<VehicleAccess> list, int maxVehicles, ParkingInformation parkingInformation){
        this.parkingId = parkId;
        this.carsList = list;
        this.maxVehicleCapacity = maxVehicles;
        this.pricingInformation = parkingInformation;
    }

    public ParkingLot(int parkId, int maxVehicles, ParkingInformation parkingInformation){
        this(parkId,new CopyOnWriteArrayList<VehicleAccess>(),maxVehicles, parkingInformation);
    }

}