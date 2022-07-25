package org.unbbrasilia.fga0158.component;

import lombok.Getter;

@Getter
public class Access {

    private final String carPlate;
    private final Long enterMillis;
    private final Long leaveMillis;

    public Access(Long enter, String carPlate, Long leaveMillis){
        this.carPlate = carPlate;
        this.enterMillis = enter;
        this.leaveMillis = leaveMillis;
    }

}