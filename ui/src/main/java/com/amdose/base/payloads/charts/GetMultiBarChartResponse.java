package com.amdose.base.payloads.charts;

import lombok.Data;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetMultiBarChartResponse {
    private List<String> labels;
    private List<MultiBarChartItem> list;
}
