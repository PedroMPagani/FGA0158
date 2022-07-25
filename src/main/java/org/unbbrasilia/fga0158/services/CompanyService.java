package org.unbbrasilia.fga0158.services;

import lombok.Getter;
import org.unbbrasilia.fga0158.component.ParkingLot;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CompanyService {

    @Getter private final List<ParkingLot> parkingLots;

    public CompanyService(CopyOnWriteArrayList<ParkingLot> parkingLots){
        this.parkingLots = parkingLots;
    }

    public CompanyService(){
        this(new CopyOnWriteArrayList<ParkingLot>());
    }


}