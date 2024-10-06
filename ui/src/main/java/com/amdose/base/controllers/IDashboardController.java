package com.amdose.base.controllers;

import com.amdose.base.constants.UriConstants;
import com.amdose.base.payloads.dashboard.GetDashboardOverviewResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Alaa Jawhar
 */
public interface IDashboardController {

    @PostMapping(UriConstants.GET_DASHBOARD_OVERVIEW)
    @Cacheable(value = "dashboardOverview", keyGenerator = "localizedGenerator")
    GetDashboardOverviewResponse getDashboardOverview();
}
