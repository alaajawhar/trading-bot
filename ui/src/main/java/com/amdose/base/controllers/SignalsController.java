package com.amdose.base.controllers;

import com.amdose.base.payloads.TradeItem;
import com.amdose.base.payloads.TradeTransactionItem;
import com.amdose.base.payloads.signals.GetSignalByIdResponse;
import com.amdose.base.payloads.signals.GetSignalListRequest;
import com.amdose.base.payloads.signals.GetSignalListResponse;
import com.amdose.base.repository.signals.DetectedSignalsRepository;
import com.amdose.base.utils.JsonUtils;
import com.amdose.database.entities.ActionEntity;
import com.amdose.database.entities.SignalEntity;
import com.amdose.database.repositories.ISignalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
@RestController
@RequiredArgsConstructor
public class SignalsController implements ISignalsController {

    private final DetectedSignalsRepository detectedSignalsRepository;
    private final ISignalRepository signalRepository;

    @Override
    public GetSignalListResponse getSignalList(GetSignalListRequest filters) {
        return detectedSignalsRepository.getSignalList(filters);
    }

    @Override
    public GetSignalByIdResponse getSignalId(String detectionId) {
        GetSignalByIdResponse response = new GetSignalByIdResponse();

        List<SignalEntity> signal = signalRepository.findByDetectionId(detectionId);

        response.setDetectionId(detectionId);
        response.setBotId(signal.get(0).getBot().getId());
        response.setTimeframe(signal.get(0).getBot().getTimeFrame());
        response.setMetaData(JsonUtils.parseJsonToObject(signal.get(0).getMetaData(), Object.class));

        for (SignalEntity signalItem : signal) {
            TradeItem tradeItem = new TradeItem();
            tradeItem.setSignalId(signalItem.getId());
            tradeItem.setTradeAction(signalItem.getAction());

            for (ActionEntity actionEntity : signalItem.getActionTaken()) {
                TradeTransactionItem tradeTransactionItem = new TradeTransactionItem();
                tradeTransactionItem.setTradeId(actionEntity.getId());
                tradeTransactionItem.setStatus(actionEntity.getStatus());
                tradeTransactionItem.setDate(actionEntity.getAddedDate());
                tradeItem.addTrade(tradeTransactionItem);
            }

            response.addTrade(tradeItem);
        }
        return response;
    }
}
