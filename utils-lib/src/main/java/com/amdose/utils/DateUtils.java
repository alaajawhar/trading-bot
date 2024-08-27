package com.amdose.utils;

import com.amdose.utils.constants.UtilConstants;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Alaa Jawhar
 */
@Slf4j
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
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Boolean isPresentOrFutureInHourMinuteSecond(Date date) {
        log.debug("Date: [{}], isFuture: [{}], isPresent: [{}]", convertToString(date), isFutureInHourMinuteSecond(date), isPresentInHourMinuteSecond(date));
        log.debug("getNow(): [{}], date: [{}], result: [{}]", convertToString(getNow()), convertToString(date), getNow().before(date));
        log.debug("getNow(): [{}], date: [{}], isFutureInHourMinuteSecondResult: [{}]", convertToString(getNow()), convertToString(date), isFutureInHourMinuteSecond(date));
        log.debug("getNow(): [{}], date: [{}], result: [{}]", getNow().getTime(), date.getTime(), getNow().before(date));
        return getNow().before(date); // isFutureInHourMinuteSecond(date) || isPresentInHourMinuteSecond(date);
    }

    public static Boolean isFutureInHourMinuteSecond(Date date) {
        Date now = roundSeconds(getNow());
        return !date.before(now);
    }

    public static boolean isPresentInHourMinuteSecond(Date date) {
        Date truncatedDate1 = org.apache.commons.lang3.time.DateUtils.truncate(date, Calendar.SECOND);
        truncatedDate1 = org.apache.commons.lang3.time.DateUtils.truncate(truncatedDate1, Calendar.MILLISECOND);

        Date truncatedDate2 = org.apache.commons.lang3.time.DateUtils.truncate(getNow(), Calendar.SECOND);
        truncatedDate2 = org.apache.commons.lang3.time.DateUtils.truncate(truncatedDate2, Calendar.MILLISECOND);


        return truncatedDate1.equals(truncatedDate2);
    }

    public static boolean areDatesEqualInHourMinuteSecond(Date date1, Date date2) {
        Date truncatedDate1 = org.apache.commons.lang3.time.DateUtils.truncate(date1, Calendar.SECOND);
        truncatedDate1 = org.apache.commons.lang3.time.DateUtils.truncate(truncatedDate1, Calendar.MILLISECOND);

        Date truncatedDate2 = org.apache.commons.lang3.time.DateUtils.truncate(date2, Calendar.SECOND);
        truncatedDate2 = org.apache.commons.lang3.time.DateUtils.truncate(truncatedDate2, Calendar.MILLISECOND);

        return truncatedDate1.equals(truncatedDate2);
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
