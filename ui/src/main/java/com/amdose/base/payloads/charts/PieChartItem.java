package com.amdose.base.payloads.charts;

import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class PieChartItem {
    private String chartName;
    private String chartColor;
    private Double data;
}
