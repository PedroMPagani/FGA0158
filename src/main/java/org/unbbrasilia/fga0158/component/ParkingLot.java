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
     * Current localzone, used to calculate the time of entering and leaving, using the System.currentTimeMillis()
     */
    private final String localZone;

    /**
     * List of VehicleAccess objects to be stored
     */
    private final List<VehicleAccess> carsList;

    /**
     * Maximum number of vehicles that can be stored in this place.
     */
    private final int maxVehicleCapacity;

    public ParkingLot(int parkId, String localZone, CopyOnWriteArrayList<VehicleAccess> list, int maxVehicles){
        this.parkingId = parkId;
        this.localZone = localZone;
        this.carsList = list;
        this.maxVehicleCapacity = maxVehicles;
    }

    public ParkingLot(int parkId, String localZone, int maxVehicles){
        this(parkId,localZone,new CopyOnWriteArrayList<VehicleAccess>(),maxVehicles);
    }

}