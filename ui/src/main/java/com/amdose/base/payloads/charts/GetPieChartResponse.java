package com.amdose.base.payloads.charts;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetPieChartResponse {
    private List<String> labels;
    private List<PieChartItem> list = new ArrayList<>();

    public void addPieChart(PieChartItem item) {
        this.list.add(item);
    }
}
