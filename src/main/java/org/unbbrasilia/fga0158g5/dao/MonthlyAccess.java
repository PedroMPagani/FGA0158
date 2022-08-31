package org.unbbrasilia.fga0158g5.dao;

import org.jetbrains.annotations.NotNull;
import org.unbbrasilia.fga0158g5.dao.base.Access;

public class MonthlyAccess extends Access {

    public MonthlyAccess(@NotNull Long entry, @NotNull Long leave, @NotNull String vehicleId) {
        super(entry, leave, vehicleId);
    }

    @Override
    public String getAccessName() {
        return "Accesso mensal";
    }

}