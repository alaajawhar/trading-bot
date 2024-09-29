package com.amdose.base.payloads.dashboard;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Data
public class ChartItem {
    private String chartName;
    private String chartColor;
    private List<Double> data = new ArrayList<>();

    public void addData(Double value) {
        this.data.add(value);
    }
}
