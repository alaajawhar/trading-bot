package com.amdose.base.payloads.charts;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Data
public class MultiBarChartItem {
    private String chartName;
    private String chartColor;
    private List<Double> data = new ArrayList<>();

    public void addDataItem(Double item) {
        this.data.add(item);
    }
}
