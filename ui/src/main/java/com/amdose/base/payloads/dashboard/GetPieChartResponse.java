package com.amdose.base.payloads.dashboard;

import lombok.Data;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetPieChartResponse {
    private List<String> labels;
    private List<PieChartItem> list;
}
