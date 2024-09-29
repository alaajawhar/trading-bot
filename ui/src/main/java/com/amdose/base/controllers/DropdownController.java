package com.amdose.base.controllers;

import com.amdose.base.models.enums.OutcomeResultEnum;
import com.amdose.base.payloads.DropDownResponse;
import com.amdose.base.payloads.KeyValueItem;
import com.amdose.database.entities.StrategyEntity;
import com.amdose.database.entities.SymbolEntity;
import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.database.repositories.IStrategyRepository;
import com.amdose.database.repositories.ISymbolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
@RestController
@RequiredArgsConstructor
public class DropdownController implements IDropdownController {

    private final IStrategyRepository strategyRepository;
    private final ISymbolRepository symbolRepository;

    @Override
    public DropDownResponse getDropdownStrategies() {
        DropDownResponse response = new DropDownResponse();

        List<StrategyEntity> strategies = strategyRepository.findAll();
        for (StrategyEntity strategyItem : strategies) {
            KeyValueItem item = new KeyValueItem();
            item.setId(String.valueOf(strategyItem.getId()));
            item.setValue(strategyItem.getName());
            response.addKeyValueItem(item);
        }

        return response;
    }

    @Override
    public DropDownResponse getDropdownOutcomeResults() {
        DropDownResponse response = new DropDownResponse();

        for (OutcomeResultEnum outcomeResult : OutcomeResultEnum.values()) {
            KeyValueItem item = new KeyValueItem();
            item.setId(outcomeResult.name());
            item.setValue(outcomeResult.getLabel());
            response.addKeyValueItem(item);
        }

        return response;
    }

    @Override
    public DropDownResponse getTimeFrames() {
        DropDownResponse response = new DropDownResponse();

        for (TimeFrameEnum outcomeResult : TimeFrameEnum.values()) {
            KeyValueItem item = new KeyValueItem();
            item.setId(outcomeResult.name());
            item.setValue(outcomeResult.getLabel());
            response.addKeyValueItem(item);
        }

        return response;
    }

    @Override
    public DropDownResponse getSymbols() {
        DropDownResponse response = new DropDownResponse();

        List<SymbolEntity> allSymbols = symbolRepository.findAll();
        for (SymbolEntity symbolItem : allSymbols) {
            KeyValueItem item = new KeyValueItem();
            item.setId(String.valueOf(symbolItem.getId()));
            item.setValue(symbolItem.getName());
            response.addKeyValueItem(item);
        }
        return response;
    }
}
