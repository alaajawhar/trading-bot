package com.amdose.base.models.dtos;

import com.amdose.database.enums.TimeFrameEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author Alaa Jawhar
 */
@Data
public class TradeDto {
    private Long strategyId;
    private double profit;
    private TimeFrameEnum timeFrame;
    private String metaData;
    private Date date;
}
