package org.unbbrasilia.fga0158g5.dao.base;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.unbbrasilia.fga0158g5.dao.Pair;
import org.unbbrasilia.fga0158g5.dao.ParkingLot;
import org.unbbrasilia.fga0158g5.util.AccessUtil;

@Getter @Setter
public abstract class Access {

    private @NotNull Long entryTime;
    private @NotNull Long leaveTime;
    private @NotNull String vehiclePlate;
    private @Nullable Double pricePaid = null;
    private Pair<Double, Double> price;

    public Access(@NotNull Long entry, @NotNull Long leave, @NotNull String vehicleId){
        this.entryTime = entry;
        this.leaveTime = leave;
        this.vehiclePlate = vehicleId;
    }

    public abstract String getAccessName();

    public void calculatePrice(ParkingLot parkingLot){
        this.price = AccessUtil.getAccessPrice(parkingLot, this);
    }

}