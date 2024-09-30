package com.amdose.base.payloads.dashboard;

import lombok.Data;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Data
public class MultiBarChartItem {
    private String chartName;
    private String chartColor;
    private List<Double> data;
}
