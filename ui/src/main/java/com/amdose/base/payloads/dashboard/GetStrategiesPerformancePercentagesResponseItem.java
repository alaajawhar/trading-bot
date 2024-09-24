package com.amdose.base.payloads.dashboard;

import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetStrategiesPerformancePercentagesResponseItem {
    private String strategyName;
    private String strategyColor;
    private Double percentage;
}
