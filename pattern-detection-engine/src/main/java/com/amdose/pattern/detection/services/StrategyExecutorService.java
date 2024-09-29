package com.amdose.pattern.detection.services;

import com.amdose.database.entities.*;
import com.amdose.database.enums.BotModeEnum;
import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.database.repositories.IBotRepository;
import com.amdose.database.repositories.ICandleRepository;
import com.amdose.database.repositories.ISignalRepository;
import com.amdose.pattern.detection.dtos.CandleItemDTO;
import com.amdose.pattern.detection.mappers.CandleMapper;
import com.amdose.pattern.detection.services.strategies.IStrategyService;
import com.amdose.pattern.detection.services.ta.Taj4JImpl;
import com.amdose.pattern.detection.services.ta.TechnicalAnalysisBaseService;
import com.amdose.utils.DateUtils;
import com.amdose.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StrategyExecutorService {

    private final List<IStrategyService> strategyServiceList;
    private final ICandleRepository candleRepository;
    private final ISignalRepository signalRepository;
    private final IBotRepository botRepository;

    @SneakyThrows
    public void runAllStrategies(TimeFrameEnum timeFrame) {
        List<SignalEntity> allDetectedSignals = new ArrayList<>();

        log.info("fetching all bots of timeFrame: [{}]", timeFrame);
        List<BotEntity> enabledBots = botRepository.findByModeNotAndTimeFrame(BotModeEnum.INACTIVE, timeFrame);

        log.info("[{}] bots have been found for timeFrame: [{}]", enabledBots.size(), timeFrame);
        log.debug("Available bots for timeFrame: [{}] are: [{}]", timeFrame, JsonUtils.convertToString(enabledBots));

        for (BotEntity bot : enabledBots) {

            List<SignalEntity> indicatorDetectedSignals = this.executeStrategy(bot.getStrategy(),
                    bot.getSymbol(), bot.getTimeFrame());

            List<SignalEntity> futureDetectedSignals = indicatorDetectedSignals.stream()
                    .filter(item -> DateUtils.isPresentOrFutureInHourMinuteSecond(item.getScheduledAt()))
                    .toList();

            for (SignalEntity signalEntity : futureDetectedSignals) {
                signalEntity.setBot(bot);
            }

            allDetectedSignals.addAll(futureDetectedSignals);

            log.info("Indicator: [{}] on Symbol: [{}] for timeFrame: [{}] has detected: [{}] signals"
                    , bot.getStrategy().getName()
                    , bot.getSymbol().getName()
                    , bot.getTimeFrame()
                    , futureDetectedSignals.size()
            );
        }

        if (allDetectedSignals.isEmpty()) {
            log.info("Do nothing.");
            return;
        }

        log.info("Saving signals...");
        log.debug("All detected signals: [{}]", JsonUtils.convertToString(allDetectedSignals));
        signalRepository.saveAll(allDetectedSignals);
    }

    public List<SignalEntity> executeStrategy(StrategyEntity strategyEntity, SymbolEntity symbolEntity, TimeFrameEnum timeFrame) {

        log.info("Running strategy: [{}] on Symbol: [{}] for timeFrame: [{}]", strategyEntity.getName(), symbolEntity.getName(), timeFrame);

        List<CandleEntity> candleEntityList = this.waitAndGetCandles(symbolEntity, timeFrame);

        return this.executeStrategy(strategyEntity, candleEntityList);
    }


    public List<SignalEntity> executeStrategy(StrategyEntity strategyEntity, List<CandleEntity> candleEntityList) {
        List<CandleItemDTO> candleItemDTOS = CandleMapper.INSTANCE.candleEntitiesToCandleItemDTOs(candleEntityList);
        log.debug("[{}] candles has been found", candleEntityList.size());

        log.debug("Applying Ta4j...");
        TechnicalAnalysisBaseService ta = new Taj4JImpl(candleItemDTOS);
        candleItemDTOS = ta.applyAll();
        log.debug("Ta4j has been applied");

        log.info("Running strategy: [{}]", strategyEntity.getName());

        Optional<IStrategyService> strategyService = strategyServiceList.stream()
                .filter(signal -> signal.getName().equalsIgnoreCase(strategyEntity.getName()))
                .findFirst();

        if (strategyService.isEmpty()) {
            log.info("No signals found for indicator: [{}]", strategyEntity.getName());
            return List.of();
        }

        List<SignalEntity> indicatorDetectedSignals = strategyService.get().executeStrategy(candleItemDTOS);

        log.info("Indicator: [{}] has detected: [{}] signals", strategyEntity.getName(), indicatorDetectedSignals.size());

        return indicatorDetectedSignals;
    }

    @SneakyThrows
    private List<CandleEntity> waitAndGetCandles(SymbolEntity symbolEntity, TimeFrameEnum timeFrame) {

        for (int i = 0; i < 3; i++) {
            log.debug("Waiting for 1sec then fetch candles of [{}]...", timeFrame);
            TimeUnit.SECONDS.sleep(1);

            List<CandleEntity> candleEntityList = candleRepository.findLastBySymbolAndTimeFrameOrderByDateAsc(
                    symbolEntity
                    , timeFrame
            );

            if (CollectionUtils.isEmpty(candleEntityList)) {
                log.debug("candleEntityList is empty continue...");
                continue;
            }

            log.debug("roundedNow: [{}], substrateTime: [{}], lastCandleDate:[{}], isEqual: [{}]"
                    , DateUtils.roundSecondsAndMilliseconds(DateUtils.getNow())
                    , timeFrame.subtractTime(DateUtils.roundSecondsAndMilliseconds(DateUtils.getNow()))
                    , candleEntityList.get(candleEntityList.size() - 1).getDate()
                    , timeFrame.subtractTime(DateUtils.roundSecondsAndMilliseconds(DateUtils.getNow())).equals(candleEntityList.get(candleEntityList.size() - 1).getDate())
            );

            boolean areDatesEquals = DateUtils.areDatesEqualInHourMinuteSecond(
                    timeFrame.subtractTime(DateUtils.roundSecondsAndMilliseconds(DateUtils.getNow()))
                    , candleEntityList.get(candleEntityList.size() - 1).getDate());

            if (areDatesEquals) {
                log.debug("Returning candles: [{}]", candleEntityList.size());
                return candleEntityList;
            }
        }

        throw new RuntimeException("Waited for 3 seconds but cant get last candle for timeFrame: [" + timeFrame + "]");
    }

}