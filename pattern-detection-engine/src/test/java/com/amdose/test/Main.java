package com.amdose.test;

import com.amdose.utils.DateUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Alaa Jawhar
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Date now = new Date();
        TimeUnit.MILLISECONDS.sleep(30);

        DateUtils.isPresentOrFutureInHourMinuteSecond(now);
    }
}
