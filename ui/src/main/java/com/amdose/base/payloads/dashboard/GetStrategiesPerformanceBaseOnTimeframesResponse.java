package com.amdose.base.payloads.dashboard;

import lombok.Data;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetStrategiesPerformanceBaseOnTimeframesResponse {
    private List<String> labels;
    private List<ChartItem> list;
}
