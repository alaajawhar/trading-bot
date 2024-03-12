package com.amdose.utils;

import com.amdose.utils.constants.UtilConstants;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Alaa Jawhar
 */
@UtilityClass
public class DateUtils {
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(UtilConstants.DATE_FORMAT);

    public static Date convertLongToDate(long timeStamp) {
        return new Date(timeStamp);
    }

    @SneakyThrows
    public static Date convertToDate(String dateStr) {
        return dateFormatter.parse(dateStr);
    }

    public static String convertToString(Date date) {
        return dateFormatter.format(date);
    }

    public static Date getNow() {
        return new Date();
    }

    public static Date roundSeconds(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Boolean roundNowAndCheckIfFuture(Date date) {
        Date now = roundSeconds(getNow());
        return !date.before(now);
    }

    public static Boolean isFuture(Date date) {
        Date now = getNow();
        return !date.before(now);
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

    @SneakyThrows
    public static Date dateOf(String dateAsStr) {
        return dateFormatter.parse(dateAsStr);
    }

}
