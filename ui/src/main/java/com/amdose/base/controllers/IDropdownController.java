package com.amdose.base.controllers;

import com.amdose.base.constants.UriConstants;
import com.amdose.base.payloads.commons.DropDownResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Alaa Jawhar
 */
public interface IDropdownController {

    @GetMapping(UriConstants.GET_STRATEGIES)
    DropDownResponse getDropdownStrategies();

    @GetMapping(UriConstants.GET_OUTCOME_RESULTS)
    @Cacheable(value = "outcomeResults", keyGenerator = "localizedGenerator")
    DropDownResponse getDropdownOutcomeResults();

    @GetMapping(UriConstants.GET_TIMEFRAMES)
    @Cacheable(value = "timeFrames", keyGenerator = "localizedGenerator")
    DropDownResponse getTimeFrames();

    @GetMapping(UriConstants.GET_SYMBOLS)
    DropDownResponse getSymbols();
}
