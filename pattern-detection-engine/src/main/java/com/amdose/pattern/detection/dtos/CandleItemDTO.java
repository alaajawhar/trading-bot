package com.amdose.pattern.detection.dtos;

import lombok.Data;

import java.util.Date;

/**
 * @author Alaa Jawhar
 */
@Data
public class CandleItemDTO {
    private double open;

    private double high;

    private double low;

    private double close;

    private double volume;

    private Date date;
}
