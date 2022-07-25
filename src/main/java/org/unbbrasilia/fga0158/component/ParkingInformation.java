package org.unbbrasilia.fga0158.component;

import lombok.Getter;

@Getter
public class ParkingInformation {

    private double fractionValue;
    private double fullHourDiscount;
    private double fullDayValue;
    private double fullNightPercentage; // night price is fullDayValue * fullNightPercentage (we'll store that in /100 already).

    private long opensAt;
    private long closesAt;
    private String localZone;
    private ParkingType parkingType;

    public ParkingInformation(double fractionValue, double fullHourDiscount, double fullDayValue, double fullNightPercentage, long opensAt, long closesAt, ParkingType parkingType, String localZone){
        this.fractionValue = fractionValue;
        this.fullHourDiscount = fullHourDiscount;
        this.fullDayValue = fullDayValue;
        this.fullNightPercentage = fullNightPercentage;
        this.opensAt = opensAt;
        this.closesAt = closesAt;
        this.parkingType = parkingType;
        this.localZone = localZone;
    }

}