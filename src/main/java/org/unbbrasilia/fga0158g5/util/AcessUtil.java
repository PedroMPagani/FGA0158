package org.unbbrasilia.fga0158g5.util;

import org.unbbrasilia.fga0158g5.dao.*;
import org.unbbrasilia.fga0158g5.dao.base.Access;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class AcessUtil {

    protected static final ZoneId id = ZoneId.systemDefault();

    public static Access returnRegistry(ParkingLot parkingLot, String plate, LocalDateTime in, LocalDateTime out){
        Long start = in.atZone(id).toEpochSecond();
        Long end = out.atZone(id).toEpochSecond();
        if(isEventAccess(parkingLot, start, end)){
            return new EventAccess(start, end, plate);
        }
        if(isNightAccess(parkingLot, in, out)){
            return new DailyNightAccess(start, end, plate);
        }
        if(isDailyAccess(in, out)){
            return new DailyDayAccess(start, end, plate);
        }
        if(isMonthAccess(in, out)){
            return new MonthlyAccess(start, end, plate);
        }
        // check if its hour access.
        if(hasAtLeast1HourDifference(in, out)){
            return new DailyDayAccess(start, end, plate);
        }
        return new FractionAccess(start, end, plate);
    }

    private static boolean isMonthAccess(LocalDateTime in, LocalDateTime out){
        return getDayDiference(in, out) >= 30; // will consider month 30 days, even tho that info does not seem to be provided in the readme
    }

    private static boolean isDailyAccess(LocalDateTime in, LocalDateTime out){
        return (isSameMonth(in,out) && getDayDiference(in, out) == 0 &&
                getHourDiference(in,out) >= 9);
    }

    private static boolean isSameDay(LocalDateTime in1, LocalDateTime in2){
        return in1.getDayOfMonth() == in2.getDayOfMonth();
    }

    private static boolean isSameMonth(LocalDateTime in1, LocalDateTime in2){
        return in1.getMonth().equals(in2.getMonth());
    }

    private static boolean sameHour(LocalDateTime in1, LocalDateTime in2){
        return in1.getHour() == in2.getHour();
    }

    private static int getHourDiference(LocalDateTime in, LocalDateTime out){
        return out.getHour()-in.getHour();
    }

    private static int getMinuteDiference(LocalDateTime in, LocalDateTime out){
        return out.getMinute()-in.getMinute();
    }

    private static int getDayDiference(LocalDateTime in, LocalDateTime out){
        return out.getDayOfMonth()-in.getDayOfMonth();
    }

    private static boolean isEventAccess(ParkingLot parkingLot, Long in, Long out){
        return parkingLot.getEvents().stream().anyMatch(event-> event.underEvent(in, out));
    }

    private static boolean isNightAccess(ParkingLot parkingLot, LocalDateTime in, LocalDateTime out){
        if (isSameMonth(in, out)){
            ParkingLot.ParkingInformation information = parkingLot.getPricingInformation();
            return (getDayDiference(in, out) == 1 && information.getEndNightHour() >= out.getHour()) &&
                    (information.getEndNightMinute() >= out.getMinute()) &&
                    (information.getStartNightHour() >= in.getHour()) &&
                    (information.getStartNightMinute() >= in.getMinute());
        }
        return false;
    }

    private static boolean hasAtLeast1HourDifference(LocalDateTime in, LocalDateTime out){
        return (in.getMonth() != out.getMonth()) || (in.getDayOfMonth() != out.getDayOfMonth()) ||
                (in.getHour() - out.getHour() >= 1);
    }

}