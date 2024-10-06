package com.amdose.utils;

import com.amdose.utils.constants.UtilConstants;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
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

    public static Date roundSecondsAndMilliseconds(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Boolean isPresentOrFutureInHourMinuteSecond(Date date) {
        return getNow().before(date); // isFutureInHourMinuteSecond(date) || isPresentInHourMinuteSecond(date);
    }

    public static Boolean isFutureInHourMinuteSecond(Date date) {
        Date now = roundSecondsAndMilliseconds(getNow());
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

    public static Date getFirstDayOfCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        return Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date getFirstDayOfCurrentWeek() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return Date.from(firstDayOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date getTodayAtStartOfDay() {
        LocalDate today = LocalDate.now();
        return Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }


    @SneakyThrows
    public static Date dateOf(String dateAsStr) {
        return dateFormatter.parse(dateAsStr);
    }

}
