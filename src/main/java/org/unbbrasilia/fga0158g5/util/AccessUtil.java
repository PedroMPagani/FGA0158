package org.unbbrasilia.fga0158g5.util;

import org.unbbrasilia.fga0158g5.Main;
import org.unbbrasilia.fga0158g5.dao.*;
import org.unbbrasilia.fga0158g5.dao.base.Access;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AccessUtil {

    public static final ZoneId id = ZoneId.systemDefault();

    // key is preco total, value retorno contratante.
    // the system should cache this value in the Access itself.
    public static Pair<Double, Double> getAccessPrice(ParkingLot parkingLot, Access access){
        ParkingLot.ParkingInformation info = parkingLot.getPricingInformation();
        if(access instanceof MonthlyAccess){
            return new Pair<>(info.getFullMonthPrice(), info.getFullMonthPrice()*info.getPercentReturnHiring());
        }
        if(access instanceof DailyDayAccess){
            return new Pair<>(info.getFullDayValue(), info.getFullDayValue()*info.getPercentReturnHiring());
        }
        if(access instanceof DailyNightAccess){
            // this access only happens if the user joins in the specified time.
            return new Pair<>(info.getFullDayValue()*info.getFullNightPercentage(), info.getFullDayValue()*info.getFullNightPercentage()*info.getPercentReturnHiring());
        }
        if(access instanceof HourlyAccess){
            // we need to calculate the discount of the hour ->
            int hours = (int) ((access.getLeaveTime()-access.getEntryTime())/3600000L);
            double price = hours*4*info.getFractionValue()*(1-info.getFullHourDiscount());
            int fractions = (int) Math.ceil(((access.getLeaveTime()-access.getEntryTime()*1.0) - (hours*3600000L))/900000L);
            double finalAmount = price + (fractions*info.getFractionValue());
            return new Pair<>(finalAmount, finalAmount*info.getPercentReturnHiring());
        }
        if(access instanceof EventAccess){
            return new Pair<>(info.getEventPrice(), info.getEventPrice()*info.getPercentReturnHiring());
        }
        // fraction value ->
        int fractions = (int) Math.ceil((access.getLeaveTime()-access.getEntryTime()*1.0)/900000L);
        if(fractions == 0) fractions = 1;// entrou pagou haha, mesmo 1min.
        return new Pair<>(info.getFractionValue()*fractions, info.getFractionValue()*fractions*info.getPercentReturnHiring());
    }

    public static Access returnRegistry(ParkingLot parkingLot, String plate, Date in, Date out){
        Long start = in.getTime();
        Long end = out.getTime();
        if(isEventAccess(start, end)){
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

    private static boolean isMonthAccess(Date in, Date out){
        return getDayDiference(in, out) >= 30; // will consider month 30 days, even tho that info does not seem to be provided in the readme
    }

    private static boolean isDailyAccess(Date in, Date out){
        return (isSameMonth(in,out) && getDayDiference(in, out) == 0 &&
                getHourDiference(in,out) >= 9);
    }

    private static boolean isSameDay(Date in1, Date in2){
        return getCalendar(in1).get(Calendar.DAY_OF_YEAR) == getCalendar(in2).get(Calendar.DAY_OF_YEAR);
    }

    private static boolean isSameMonth(Date in1, Date in2){
        return getCalendar(in1).get(Calendar.MONTH) == getCalendar(in2).get(Calendar.MONTH);
    }

    private static boolean sameHour(Date in1, Date in2){
        return getCalendar(in1).get(Calendar.HOUR_OF_DAY) == getCalendar(in2).get(Calendar.HOUR_OF_DAY);
    }

    private static int getHourDiference(Date in, Date out){
        return getCalendar(out).get(Calendar.HOUR_OF_DAY) - getCalendar(in).get(Calendar.HOUR_OF_DAY);
    }

    private static int getMinuteDiference(Date in, Date out){
        return getCalendar(out).get(Calendar.MINUTE) - getCalendar(in).get(Calendar.MINUTE);
    }

    private static int getDayDiference(Date in, Date out){
        return getCalendar(out).get(Calendar.DAY_OF_MONTH) - getCalendar(in).get(Calendar.DAY_OF_MONTH);
    }

    private static Calendar getCalendar(Date date){
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal;
    }

    private static boolean isEventAccess(Long in, Long out){
        return Main.events.stream().anyMatch(event-> event.underEvent(in, out));
    }

    private static boolean isNightAccess(ParkingLot parkingLot, Date in, Date out){
        if (isSameMonth(in, out)){
            ParkingLot.ParkingInformation information = parkingLot.getPricingInformation();
            return (getDayDiference(in, out) == 1 && information.getEndNightHour() >= out.getHours()) &&
                    (information.getEndNightMinute() >= out.getMinutes()) &&
                    (information.getStartNightHour() >= in.getHours()) &&
                    (information.getStartNightMinute() >= in.getMinutes());
        }
        return false;
    }

    private static boolean hasAtLeast1HourDifference(Date in, Date out){
        return (getCalendar(in).get(Calendar.MONTH) == getCalendar(out).get(Calendar.MONTH)) &&
                (getCalendar(out).get(Calendar.HOUR_OF_DAY) - getCalendar(in).get(Calendar.HOUR_OF_DAY) >= 1);
    }

}