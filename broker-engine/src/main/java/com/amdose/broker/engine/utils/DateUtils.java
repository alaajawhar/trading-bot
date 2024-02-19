package com.amdose.broker.engine.utils;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Alaa Jawhar
 */
@UtilityClass
public class DateUtils {
    private final String dateFormatStr = "dd-MM-yyyy HH:mm:ss";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatStr);

    public static Date convertLongToDate(long timeStamp) {
        return new Date(timeStamp);
    }

    public static String convertToString(Date date) {
        return simpleDateFormat.format(date);
    }

    public static Date addDays(Date date, int numberOfDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, numberOfDays);
        return calendar.getTime();
    }

    public static Date addMinutes(Date date, int numberOfMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, numberOfMinutes);
        return calendar.getTime();
    }

    public static Date addHours(Date date, int numberOfHours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, numberOfHours);
        return calendar.getTime();
    }

}
