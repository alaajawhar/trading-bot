package com.amdose.base.repository.dao;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Alaa Jawhar
 */
@Data
public class StrategyPerformanceItemDao {
    private String performanceLabel;
    private BigDecimal performanceValue;
}
