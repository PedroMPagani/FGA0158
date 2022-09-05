package org.unbbrasilia.fga0158g5.dao;

import lombok.Getter;
import lombok.Setter;
import org.unbbrasilia.fga0158g5.dao.base.Access;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter @Setter
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
    private int maxVehicleCapacity;

    private final ParkingInformation pricingInformation;

    public ParkingLot(int parkId, CopyOnWriteArrayList<Access> list, int maxVehicles, ParkingInformation parkingInformation){
        this.parkingId = parkId;
        this.acesses = list;
        this.maxVehicleCapacity = maxVehicles;
        this.pricingInformation = parkingInformation;
    }

    public boolean hasFreeSpot(){
        return (this.maxVehicleCapacity - getSpotsUsed()) > 0;
    }

    public ParkingLot(int parkId, int maxVehicles, ParkingInformation parkingInformation){
        this(parkId, new CopyOnWriteArrayList<>(),maxVehicles, parkingInformation);
    }

    public void addAccess(Access access){
        this.acesses.add(access);
    }

    public long getSpotsUsed(){
        Long currentTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        return (this.acesses.stream().filter(s->s.getLeaveTime() - currentTime > 0).count());
    }

    @Getter @Setter
    public static class ParkingInformation {

        private double fractionValue;
        private double fullHourDiscount;
        private double fullDayValue;
        private double fullNightPercentage; // night price is fullDayValue * fullNightPercentage (we'll store that in /100 already).
        private double percentReturnHiring;
        private double eventPrice;
        private double fullMonthPrice;

        // used to determine wether it will be a night period or not.
        private int startNightHour;
        private int startNightMinute;
        private int endNightHour;
        private int endNightMinute;

        private long opensAt; // wont consider day, only hour and minute.
        private long closesAt;

        public ParkingInformation(double fractionValue, double fullHourDiscount, double fullDayValue, double fullNightPercentage, double percentReturnHiring, long opensAt, long closesAt,
                                  double eventPrice, double monthPrice, int startNightHour, int startNightMinute, int endNightHour, int endNightMinute){
            this.fractionValue = fractionValue;
            this.fullHourDiscount = fullHourDiscount;
            this.fullDayValue = fullDayValue;
            this.fullNightPercentage = fullNightPercentage;
            this.percentReturnHiring = percentReturnHiring;
            this.opensAt = opensAt;
            this.closesAt = closesAt;
            this.eventPrice = eventPrice;
            this.fullMonthPrice = monthPrice;
            this.startNightHour = startNightHour;
            this.startNightMinute = startNightMinute;
            this.endNightHour = endNightHour;
            this.endNightMinute = endNightMinute;
        }

    }


}