package com.amdose.base.controllers;

import com.amdose.base.constants.UriConstants;
import com.amdose.base.payloads.test.strategy.GetStrategyTestRequest;
import com.amdose.base.payloads.test.strategy.GetStrategyTestResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Alaa Jawhar
 */
public interface ITestController {

    @PostMapping(UriConstants.GET_DASHBOARD_SUMMARY_TEST)
    GetStrategyTestResponse getStrategyTest(@RequestBody @Valid GetStrategyTestRequest request);
}
