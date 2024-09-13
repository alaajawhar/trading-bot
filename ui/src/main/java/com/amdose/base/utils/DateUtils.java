package com.amdose.base.utils;

import com.amdose.base.constants.AppConstants;
import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Alaa Jawhar
 */
@UtilityClass
public class DateUtils {


    public static String getCurrentDate() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat(AppConstants.APPLICATION_DATE_FORMAT);
        return dateFormat.format(date);
    }
}
