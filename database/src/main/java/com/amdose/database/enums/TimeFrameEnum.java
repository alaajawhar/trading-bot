package com.amdose.database.enums;

import lombok.Getter;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * @author Alaa Jawhar
 */
@Getter
public enum TimeFrameEnum {
    ONE_SECOND("1 * * * * *"),
    ONE_MINUTE("0 */1 * * * *"),
    THREE_MINUTES("0 */3 * * * *"),
    FIFTEEN_MINUTES("0 */15 * * * *"),
    ONE_HOUR("0 0 */1 * * *"),
    FOUR_HOURS("0 0 */4 * * *"),
    ONE_DAY("0 0 0 * * ?");

    private String cronExpression;

    TimeFrameEnum(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Date addTime(Date date) {
        return switch (this) {
            case ONE_SECOND -> DateUtils.addSeconds(date, 1);
            case ONE_MINUTE -> DateUtils.addMinutes(date, 1);
            case THREE_MINUTES -> DateUtils.addMinutes(date, 3);
            case FIFTEEN_MINUTES -> DateUtils.addMinutes(date, 15);
            case ONE_HOUR -> DateUtils.addHours(date, 1);
            case FOUR_HOURS -> DateUtils.addHours(date, 4);
            case ONE_DAY -> DateUtils.addDays(date, 1);
        };
    }

    public Date subtractTime(Date date) {
        return switch (this) {
            case ONE_SECOND -> DateUtils.addSeconds(date, -1);
            case ONE_MINUTE -> DateUtils.addMinutes(date, -1);
            case THREE_MINUTES -> DateUtils.addMinutes(date, -3);
            case FIFTEEN_MINUTES -> DateUtils.addMinutes(date, -15);
            case ONE_HOUR -> DateUtils.addHours(date, -1);
            case FOUR_HOURS -> DateUtils.addHours(date, -4);
            case ONE_DAY -> DateUtils.addDays(date, -1);
        };
    }

    public String getBinanceInterval() {
        return switch (this) {
            case ONE_SECOND -> "1s";
            case ONE_MINUTE -> "1m";
            case THREE_MINUTES -> "3m";
            case FIFTEEN_MINUTES -> "15m";
            case ONE_HOUR -> "1h";
            case FOUR_HOURS -> "4h";
            case ONE_DAY -> "1d";
        };
    }
}
