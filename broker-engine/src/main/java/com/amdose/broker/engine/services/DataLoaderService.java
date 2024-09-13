package com.amdose.broker.engine.services;

import com.amdose.broker.engine.brokers.BinanceBroker;
import com.amdose.database.entities.SymbolEntity;
import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.database.repositories.ISymbolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataLoaderService {

    private final BinanceBroker binanceBroker;
    private final ISymbolRepository symbolRepository;

    public void updateAllSymbols(TimeFrameEnum interval) {
        List<SymbolEntity> symbolList = symbolRepository.findAll();
        for (SymbolEntity symbolEntity : symbolList) {
            log.debug("update data of symbol: [{}] and [{}] interval", symbolEntity.getName(), interval);
            binanceBroker.updateCandles(symbolEntity, interval);
        }
    }
}
