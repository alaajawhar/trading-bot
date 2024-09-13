package com.amdose.pattern.detection.services;

import com.amdose.database.entities.BotEntity;
import com.amdose.database.entities.CandleEntity;
import com.amdose.database.entities.SignalEntity;
import com.amdose.database.enums.BotModeEnum;
import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.database.repositories.IBotRepository;
import com.amdose.database.repositories.ICandleRepository;
import com.amdose.database.repositories.ISignalRepository;
import com.amdose.pattern.detection.dtos.CandleItemDTO;
import com.amdose.pattern.detection.mappers.CandleMapper;
import com.amdose.pattern.detection.services.indicators.IIndicatorService;
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
public class IndicatorsService {

    private final List<IIndicatorService> indicatorServiceList;
    private final ICandleRepository candleRepository;
    private final ISignalRepository signalRepository;
    private final IBotRepository botRepository;

    @SneakyThrows
    public void runAllIndicators(TimeFrameEnum timeFrame) {
        List<SignalEntity> allDetectedSignals = new ArrayList<>();

        log.info("fetching all bots of timeFrame: [{}]", timeFrame);
        List<BotEntity> allEnabledBots = botRepository.findByModeNotAndTimeFrame(BotModeEnum.INACTIVE, timeFrame);

        log.info("[{}] bots have been found for timeFrame: [{}]", allEnabledBots.size(), timeFrame);
        log.debug("Available bots for timeFrame: [{}] are: [{}]", timeFrame, JsonUtils.convertToString(allEnabledBots));

        for (BotEntity bot : allEnabledBots) {
            log.info("Running indicator: [{}] on Symbol: [{}] for timeFrame: [{}]"
                    , bot.getStrategy().getName()
                    , bot.getSymbol().getName()
                    , bot.getTimeFrame()
            );

            List<CandleEntity> candleEntityList = this.waitAndGetCandles(bot, timeFrame);

            List<CandleItemDTO> candleItemDTOS = CandleMapper.INSTANCE.candleEntitiesToCandleItemDTOs(candleEntityList);
            log.debug("[{}] candles has been found", candleEntityList.size());

            log.debug("Applying Ta4j...");
            TechnicalAnalysisBaseService ta = new Taj4JImpl(candleItemDTOS);
            candleItemDTOS = ta.applyAll();
            log.debug("Ta4j has been applied");

            Optional<IIndicatorService> signalOptional = indicatorServiceList.stream()
                    .filter(signal -> signal.getName().equalsIgnoreCase(bot.getStrategy().getName()))
                    .findFirst();

            if (signalOptional.isEmpty()) {
                log.info("No signals found for indicator: [{}]", bot.getStrategy().getName());
                continue;
            }

            List<SignalEntity> indicatorDetectedSignals = signalOptional.get().apply(candleItemDTOS);

            for (SignalEntity signalEntity : indicatorDetectedSignals) {
                signalEntity.setBot(bot);
            }

            allDetectedSignals.addAll(indicatorDetectedSignals);

            log.info("Indicator: [{}] on Symbol: [{}] for timeFrame: [{}] has detected: [{}] signals"
                    , bot.getStrategy().getName()
                    , bot.getSymbol().getName()
                    , bot.getTimeFrame()
                    , indicatorDetectedSignals.size()
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

    @SneakyThrows
    private List<CandleEntity> waitAndGetCandles(BotEntity bot, TimeFrameEnum timeFrame) {

        for (int i = 0; i < 3; i++) {
            log.debug("Waiting for 1sec then fetch candles of [{}]...", timeFrame);
            TimeUnit.SECONDS.sleep(1);

            List<CandleEntity> candleEntityList = candleRepository.findLastBySymbolAndTimeFrameOrderByDateAsc(
                    bot.getSymbol()
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

            if (timeFrame.subtractTime(DateUtils.roundSecondsAndMilliseconds(DateUtils.getNow()))
                    .equals(candleEntityList.get(candleEntityList.size() - 1).getDate())) {
                log.debug("Returning candles: [{}]", candleEntityList.size());
                return candleEntityList;
            }
        }

        throw new RuntimeException("Waited for 3 seconds but cant get last candle for timeFrame: [" + timeFrame + "]");
    }
}
