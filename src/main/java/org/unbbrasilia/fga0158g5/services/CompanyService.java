package org.unbbrasilia.fga0158g5.services;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.unbbrasilia.fga0158g5.dao.Logger;
import org.unbbrasilia.fga0158g5.dao.ParkingLot;
import org.unbbrasilia.fga0158g5.services.base.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CompanyService extends Service {

    @Getter protected final @NotNull List<ParkingLot> parkingLots
            = new CopyOnWriteArrayList<>();

    public CompanyService() {
        super(new Logger("Company Service Logger"));
    }

    public void startService(){
        // pull all data from database?
    }

    @Override
    public void shutdownService(){
        this.getLogger().log("Company Service shutting down..");
    }

}