package org.unbbrasilia.fga0158g5.dao;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class Logger {

    @Getter private final @NotNull String loggerName;

    public Logger(@NotNull String logger){
        this.loggerName = logger;
    }

    public void log(Object ... objects){
        for (int i = 0; i < objects.length; i++){
            System.out.println(objects[i]);
        }
    }

    public void log(Object object){
        System.out.println(object);
    }

}