package com.amdose.pattern.detection.services;

import com.amdose.database.entities.*;
import com.amdose.database.enums.BotModeEnum;
import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.database.repositories.IBotRepository;
import com.amdose.database.repositories.ICandleRepository;
import com.amdose.database.repositories.ISignalRepository;
import com.amdose.pattern.detection.dtos.SignalItemDTO;
import com.amdose.pattern.detection.strategies.IStrategyService;
import com.amdose.pattern.detection.utils.CandlesConvertorUtils;
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

            List<SignalItemDTO> indicatorDetectedSignals = this.executeStrategy(bot.getStrategy(),
                    bot.getSymbol(), bot.getTimeFrame());

            List<SignalItemDTO> futureDetectedSignals = indicatorDetectedSignals.stream()
                    .filter(item -> DateUtils.isPresentOrFutureInHourMinuteSecond(item.getScheduledAt()))
                    .toList();

            for (SignalItemDTO strategySignal : futureDetectedSignals) {
                SignalEntity signalEntity = strategySignal.toSignalEntity();
                signalEntity.setBot(bot);
                allDetectedSignals.add(signalEntity);
            }

            log.info("Indicator: [{}] on Symbol: [{}] for timeFrame: [{}] has detected: [{}] future signals"
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

    public List<SignalItemDTO> executeStrategy(StrategyEntity strategyEntity, SymbolEntity symbolEntity, TimeFrameEnum timeFrame) {

        log.info("Running strategy: [{}] on Symbol: [{}] for timeFrame: [{}]", strategyEntity.getName(), symbolEntity.getName(), timeFrame);

        List<CandleEntity> candleEntityList = this.waitAndGetCandles(symbolEntity, timeFrame);

        return this.executeStrategy(strategyEntity, candleEntityList);
    }


    public List<SignalItemDTO> executeStrategy(StrategyEntity strategyEntity, List<CandleEntity> candleEntityList) {
        log.debug("Executing strategy: [{}] on [{}] candles has been found", strategyEntity.getName(), candleEntityList.size());

        Optional<IStrategyService> strategyService = strategyServiceList.stream()
                .filter(signal -> signal.getName().equalsIgnoreCase(strategyEntity.getName()))
                .findFirst();

        if (strategyService.isEmpty()) {
            log.info("Strategy has not found: [{}]", strategyEntity.getName());
            return List.of();
        }

        List<SignalItemDTO> detectedSignals = strategyService.get().executeStrategy(CandlesConvertorUtils.convertToCandlesDto(candleEntityList));

        log.info("Strategy: [{}] has detected: [{}] signals", strategyEntity.getName(), detectedSignals.size());

        return detectedSignals;
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
