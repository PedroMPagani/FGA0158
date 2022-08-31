package org.unbbrasilia.fga0158g5.dao.base;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter @Setter
public abstract class Access {

    private @NotNull Long entryTime;
    private @NotNull Long leaveTime;
    private @NotNull String vehiclePlate;
    private @Nullable Double pricePaid = null;

    public Access(@NotNull Long entry, @NotNull Long leave, @NotNull String vehicleId){
        this.entryTime = entry;
        this.leaveTime = leave;
        this.vehiclePlate = vehicleId;
    }

    public abstract String getAccessName();

}