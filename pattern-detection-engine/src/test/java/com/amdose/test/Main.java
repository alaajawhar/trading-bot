package com.amdose.test;

import com.amdose.pattern.detection.utils.DateUtils;

import java.util.Date;

/**
 * @author Alaa Jawhar
 */
public class Main {
    public static void main(String[] args) {
        Date now = DateUtils.convertToDate("19-02-2024 09:29:00");
        Date date = DateUtils.convertToDate("19-02-2024 09:30:00");

        Date d = DateUtils.roundSeconds(now);
        System.out.println(!date.before(now));
    }
}
