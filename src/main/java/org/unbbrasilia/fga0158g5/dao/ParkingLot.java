package org.unbbrasilia.fga0158g5.dao;

import lombok.Getter;
import org.unbbrasilia.fga0158g5.dao.base.Access;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class ParkingLot {

    /**
     * Park id of the company.
     * <p>
     * identifies the ID of this parking lot, make it PRIMARY UNIQUE key on database
     */
    private final int parkingId;

    /**
     * List of VehicleAccess object references to be stored
     * <p>
     * pull from database ? (not scalable)
     */
    private final List<Access> acesses;

    /**
     * Maximum number of vehicles that can be stored in this place.
     */
    private final int maxVehicleCapacity;

    private final ParkingInformation pricingInformation;

    public ParkingLot(int parkId, CopyOnWriteArrayList<Access> list, int maxVehicles, ParkingInformation parkingInformation){
        this.parkingId = parkId;
        this.acesses = list;
        this.maxVehicleCapacity = maxVehicles;
        this.pricingInformation = parkingInformation;
    }

    public ParkingLot(int parkId, int maxVehicles, ParkingInformation parkingInformation){
        this(parkId, new CopyOnWriteArrayList<>(),maxVehicles, parkingInformation);
    }

    @Getter
    public static class ParkingInformation {

        private double fractionValue;
        private double fullHourDiscount;
        private double fullDayValue;
        private double fullNightPercentage; // night price is fullDayValue * fullNightPercentage (we'll store that in /100 already).
        private double percentReturnHiring;

        private long opensAt;
        private long closesAt;
        private String localZone;

        public ParkingInformation(double fractionValue, double fullHourDiscount, double fullDayValue, double fullNightPercentage, double percentReturnHiring, long opensAt, long closesAt, String localZone){
            this.fractionValue = fractionValue;
            this.fullHourDiscount = fullHourDiscount;
            this.fullDayValue = fullDayValue;
            this.fullNightPercentage = fullNightPercentage;
            this.percentReturnHiring = percentReturnHiring;
            this.opensAt = opensAt;
            this.closesAt = closesAt;
            this.localZone = localZone;
        }

    }


}