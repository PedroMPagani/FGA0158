package org.unbbrasilia.fga0158.component;

import lombok.Getter;

// this class isn't called Car because there could be a need in the future to specify more things about the vehicle, such as size, spots inside, etc.
@Getter
public class VehicleAccess {

    /**
     * Vehicle license plate.
     */
    private final String vehiclePlate;

    /**
     * System millis of when it joined, combined with leaveMillis is used to calculate the total & final price.
     */
    private final Long enterMillis;

    /**
     * System millis of the time the vehicle left the parking lot.
     */
    private final Long leaveMillis;


    /**
     * Enum attribute used to define what day time this vehicle entered the parking lot.
     */
    private final AccessType accessType;

    public VehicleAccess(Long enter, String vehiclePlate, Long leaveMillis, AccessType type){
        this.vehiclePlate = vehiclePlate;
        this.enterMillis = enter;
        this.leaveMillis = leaveMillis;
        this.accessType = type;
    }

}