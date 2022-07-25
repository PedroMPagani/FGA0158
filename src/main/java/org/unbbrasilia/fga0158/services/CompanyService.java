package org.unbbrasilia.fga0158.services;

import lombok.Getter;
import org.unbbrasilia.fga0158.component.Park;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CompanyService {

    @Getter private final List<Park> parkingLots;

    public CompanyService(CopyOnWriteArrayList<Park> parkingLots){
        this.parkingLots = parkingLots;
    }

    public CompanyService(){
        this(new CopyOnWriteArrayList<Park>());
    }


}