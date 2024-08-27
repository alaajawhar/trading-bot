package com.amdose.utils.tests;

import com.amdose.utils.DateUtils;

import java.util.Date;

/**
 * @author Alaa Jawhar
 */
public class MainTest {
    public static void main(String[] args) {
        Date now = new Date();
        Date roundedDate = DateUtils.roundSeconds(now);
        System.out.println(roundedDate.before(roundedDate));

    }
}
