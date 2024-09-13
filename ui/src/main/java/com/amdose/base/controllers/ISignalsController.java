package com.amdose.base.controllers;

import com.amdose.base.constants.UriConstants;
import com.amdose.base.payloads.signals.GetSignalByIdResponse;
import com.amdose.base.payloads.signals.GetSignalListRequest;
import com.amdose.base.payloads.signals.GetSignalListResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Alaa Jawhar
 */
public interface ISignalsController {

    @PostMapping(UriConstants.GET_SIGNAL_LIST)
    GetSignalListResponse getSignalList(@RequestBody @Valid GetSignalListRequest testRequest);

    @GetMapping(UriConstants.GET_SIGNAL_BY_ID)
    GetSignalByIdResponse getSignalId(@PathVariable String detectionId);
}
