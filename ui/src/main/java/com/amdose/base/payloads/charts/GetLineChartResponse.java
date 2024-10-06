package com.amdose.base.payloads.charts;

import lombok.Data;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetLineChartResponse {
    private List<String> labels;
    private List<LineChartItem> list;
}
