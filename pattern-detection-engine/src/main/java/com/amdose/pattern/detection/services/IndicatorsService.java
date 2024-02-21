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
import com.amdose.pattern.detection.services.signals.IIndicatorService;
import com.amdose.pattern.detection.services.ta.Taj4JImpl;
import com.amdose.pattern.detection.services.ta.TechnicalAnalysisBaseService;
import com.amdose.pattern.detection.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public void runAllIndicators(TimeFrameEnum timeFrame) {
        List<SignalEntity> allDetectedSignals = new ArrayList<>();

        log.info("fetching all bots of timeFrame: [{}]", timeFrame);
        List<BotEntity> allEnabledBots = botRepository.findByModeNotAndTimeFrame(BotModeEnum.INACTIVE, timeFrame);

        log.info("[{}] bots have been found of timeFrame: [{}]", allEnabledBots.size(), timeFrame);
        log.debug("Available bots for timeFrame: [{}] are: [{}]", timeFrame, JsonUtils.convertToString(allEnabledBots));

        for (BotEntity bot : allEnabledBots) {
            log.info("Running indicator: [{}] on Symbol: [{}] for timeFrame: [{}]"
                    , bot.getIndicator().getName()
                    , bot.getSymbol().getName()
                    , bot.getTimeFrame()
            );

            log.debug("Fetching candles...");
            List<CandleEntity> candleEntityList = candleRepository.findLastBySymbolAndTimeFrameOrderByDateAsc(
                    bot.getSymbol().getName()
                    , timeFrame
            );

            List<CandleItemDTO> candleItemDTOS = CandleMapper.INSTANCE.candleEntitiesToCandleItemDTOs(candleEntityList);
            log.debug("[{}] candles has been found", candleEntityList.size());

            log.debug("Applying Ta4j...");
            TechnicalAnalysisBaseService ta = new Taj4JImpl(candleItemDTOS);
            candleItemDTOS = ta.applyAll();
            log.debug("Ta4j has been applied");
            log.debug("RSI on [{}] is: [{}]. timeFrame: [{}]"
                    , candleItemDTOS.get(candleItemDTOS.size() - 1).getDate()
                    , candleItemDTOS.get(candleItemDTOS.size() - 1).getRsiValue()
                    , timeFrame
            );

            Optional<IIndicatorService> signalOptional = indicatorServiceList.stream()
                    .filter(signal -> signal.getName().equalsIgnoreCase(bot.getIndicator().getName()))
                    .findFirst();

            if (signalOptional.isEmpty()) {
                continue;
            }

            List<SignalEntity> indicatorDetectedSignals = signalOptional.get().apply(candleItemDTOS);

            for (SignalEntity signalEntity : indicatorDetectedSignals) {
                signalEntity.setIndicator(bot.getIndicator());
            }

            allDetectedSignals.addAll(indicatorDetectedSignals);

            log.info("Indicator: [{}] on Symbol: [{}] for timeFrame: [{}] has detected: [{}] signals"
                    , bot.getIndicator().getName()
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
}
