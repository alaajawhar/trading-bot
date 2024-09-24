package com.amdose.base.payloads.dashboard;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Data
public class StrategyPerformanceItem {
    private String strategyName;
    private String strategyColor;
    private List<BigDecimal> data;
}
