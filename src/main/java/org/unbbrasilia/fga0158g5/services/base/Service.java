package org.unbbrasilia.fga0158g5.services.base;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.unbbrasilia.fga0158g5.dao.Logger;

public abstract class Service {

    public abstract void startService();

    @Getter private final @NotNull Logger logger;

    public Service(@NotNull Logger logger){
        this.logger = logger;
    }

    public void shutdownService(){
        // needs to override.
    }

}