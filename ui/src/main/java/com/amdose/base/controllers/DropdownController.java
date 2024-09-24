package com.amdose.base.controllers;

import com.amdose.base.models.enums.OutcomeResultEnum;
import com.amdose.base.payloads.DropDownResponse;
import com.amdose.base.payloads.KeyValueItem;
import com.amdose.database.entities.StrategyEntity;
import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.database.repositories.IStrategyRepository;
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

    @Override
    public DropDownResponse getDropdownStrategies() {
        DropDownResponse response = new DropDownResponse();

        List<StrategyEntity> strategies = strategyRepository.findAll();
        for (StrategyEntity bot : strategies) {
            KeyValueItem item = new KeyValueItem();
            item.setId(String.valueOf(bot.getId()));
            item.setValue(bot.getName());
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
}
