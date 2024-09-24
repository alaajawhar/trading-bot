package com.amdose.base.controllers;

import com.amdose.base.constants.UriConstants;
import com.amdose.base.payloads.dashboard.DashboardRequest;
import com.amdose.base.payloads.dashboard.GetDashboardSummaryResponse;
import com.amdose.base.payloads.dashboard.GetStrategiesPerformanceBaseOnTimeframesResponse;
import com.amdose.base.payloads.dashboard.GetStrategiesPerformanceOverTimeResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Alaa Jawhar
 */
public interface IDashboardController {

    @PostMapping(UriConstants.GET_DASHBOARD_SUMMARY)
    GetDashboardSummaryResponse getDashboardSummary(@RequestBody @Valid DashboardRequest request);

    @PostMapping(UriConstants.GET_DASHBOARD_STRATEGY_PERFORMANCE_OVER_PERIOD)
    GetStrategiesPerformanceOverTimeResponse getStrategiesPerformanceOverTime(@RequestBody @Valid DashboardRequest request);

    @PostMapping(UriConstants.GET_DASHBOARD_BOTS_PERFORMANCE_BASE_ON_TIMEFRAME)
    GetStrategiesPerformanceBaseOnTimeframesResponse getStrategiesPerformanceOverTimeframes(@RequestBody @Valid DashboardRequest request);
}
