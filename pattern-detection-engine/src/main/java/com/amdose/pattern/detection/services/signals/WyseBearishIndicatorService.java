package com.amdose.pattern.detection.services.signals;

import com.amdose.database.entities.CandleEntity;
import com.amdose.database.entities.SignalEntity;
import com.amdose.database.enums.SignalActionEnum;
import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.pattern.detection.dtos.CandleItemDTO;
import com.amdose.pattern.detection.helpers.HighDetectionHelper;
import com.amdose.pattern.detection.helpers.LowDetectionHelper;
import com.amdose.pattern.detection.helpers.TrendHelper;
import com.amdose.pattern.detection.utils.DateUtils;
import com.amdose.pattern.detection.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WyseBearishIndicatorService implements IIndicatorService {
    private static final String INDICATOR_NAME = "WyseBearishSignal";
    private static final int LOOK_BACK_PERIOD = 20;

    @Override
    public String getName() {
        return INDICATOR_NAME;
    }

    @Override
    public List<SignalEntity> apply(List<CandleItemDTO> candleItemDTOS) {
        List<SignalEntity> result = new ArrayList<>();
        TimeFrameEnum interval = candleItemDTOS.get(0).getTimeFrame();

        List<Double> rsiValues = candleItemDTOS.stream().map(CandleItemDTO::getRsiValue).toList();
        List<Double> closeValues = candleItemDTOS.stream().map(CandleEntity::getClose).toList();
        List<Double> openValues = candleItemDTOS.stream().map(CandleEntity::getOpen).toList();

        HighDetectionHelper priceHighDetection = new HighDetectionHelper(closeValues, LOOK_BACK_PERIOD);
        LowDetectionHelper rsiLowDetection = new LowDetectionHelper(rsiValues, LOOK_BACK_PERIOD);

        for (int i = 0; i < candleItemDTOS.size(); i++) {

            if (rsiLowDetection.isLowerHighAt(i) == Boolean.FALSE
                    || priceHighDetection.isHigherHighAt(i) == Boolean.FALSE) {
                continue;
            }

            log.debug("Bearish trend has been detected: LowerHigh RSI & HigherHigh Price on: [{}] with RSI: [{}]"
                    , candleItemDTOS.get(i).getDate()
                    , candleItemDTOS.get(i).getRsiValue()
            );

            Map<String, Object> metaData = new HashMap<>();
            metaData.put("interval", interval.name());
            metaData.put("detectionDate", DateUtils.convertToString(candleItemDTOS.get(i).getDate()));
            metaData.put("rsiValueOnDetection", rsiValues.get(i));
            metaData.put("priceCloseValueOnDetection", closeValues.get(i));
            metaData.put("isConfirmed", Boolean.FALSE);

            Optional<Integer> lastHigherHighIndex = priceHighDetection.getLastHigherHigh(i);

            if (lastHigherHighIndex.isEmpty()) {
                log.debug("Skipped due to no last higher high found in [{}]", LOOK_BACK_PERIOD);
                continue;
            }

            metaData.put("lastPriceHigherHighDate", DateUtils.convertToString(candleItemDTOS.get(lastHigherHighIndex.get()).getDate()));
            metaData.put("lastRsiHigherHighValue", rsiValues.get(lastHigherHighIndex.get()));

            TrendHelper trendEquation = new TrendHelper(openValues, lastHigherHighIndex.get(), i);

            if (i + 1 < candleItemDTOS.size() && candleItemDTOS.get(i + 1).getClose() < trendEquation.getValueAt(i + 1)) {
                Date signalScheduledAt = this.addUniteToDate(candleItemDTOS.get(i + 1).getDate(), interval, 1);
                metaData.put("trendConfirmationDate", DateUtils.convertToString(candleItemDTOS.get(i + 1).getDate()));
                metaData.put("isConfirmed", Boolean.TRUE);
                metaData.put("signalScheduledAt", DateUtils.convertToString(signalScheduledAt));
                metaData.put("index", 1);
                log.debug("The bearish trend has been confirmed: [{}]", JsonUtils.convertToString(metaData));
                if (DateUtils.roundNowAndCheckIfFuture(signalScheduledAt) == Boolean.TRUE) {
                    log.debug("Return signals");
                    return this.takeAction(signalScheduledAt, interval, metaData);
                }
                log.debug("Bearish trend occurred in the past. Keep looking...");
            }

            if (i + 2 < candleItemDTOS.size()
                    && candleItemDTOS.get(i + 1).getClose() > trendEquation.getValueAt(i + 1) // not confirmed before
                    && candleItemDTOS.get(i + 2).getClose() < trendEquation.getValueAt(i + 2) // confirmed
            ) {
                Date signalScheduledAt = this.addUniteToDate(candleItemDTOS.get(i + 2).getDate(), interval, 1);
                metaData.put("isConfirmed", Boolean.TRUE);
                metaData.put("trendConfirmationDate", DateUtils.convertToString(candleItemDTOS.get(i + 2).getDate()));
                metaData.put("signalScheduledAt", DateUtils.convertToString(signalScheduledAt));
                metaData.put("index", 2);
                log.debug("The bearish trend has been confirmed: [{}]", JsonUtils.convertToString(metaData));
                if (DateUtils.roundNowAndCheckIfFuture(signalScheduledAt) == Boolean.TRUE) {
                    log.debug("Return signals");
                    return this.takeAction(signalScheduledAt, interval, metaData);
                }
                log.debug("Bearish trend occurred in the past. Keep looking...");
            }

            if (i + 3 < candleItemDTOS.size()
                    && candleItemDTOS.get(i + 1).getClose() > trendEquation.getValueAt(i + 1) // not confirmed before
                    && candleItemDTOS.get(i + 2).getClose() > trendEquation.getValueAt(i + 2) // not confirmed before
                    && candleItemDTOS.get(i + 3).getClose() < trendEquation.getValueAt(i + 3) // confirmed
            ) {
                Date signalScheduledAt = this.addUniteToDate(candleItemDTOS.get(i + 3).getDate(), interval, 1);
                metaData.put("isConfirmed", Boolean.TRUE);
                metaData.put("trendConfirmationDate", DateUtils.convertToString(candleItemDTOS.get(i + 3).getDate()));
                metaData.put("signalScheduledAt", DateUtils.convertToString(signalScheduledAt));
                metaData.put("index", 3);
                log.debug("The bearish trend has been confirmed: [{}]", JsonUtils.convertToString(metaData));
                if (DateUtils.roundNowAndCheckIfFuture(signalScheduledAt) == Boolean.TRUE) {
                    log.debug("Return signals");
                    return this.takeAction(signalScheduledAt, interval, metaData);
                }
                log.debug("Bearish trend occurred in the past. Keep looking...");
            }

            if (i + 4 < candleItemDTOS.size()
                    && candleItemDTOS.get(i + 1).getClose() > trendEquation.getValueAt(i + 1) // not confirmed before
                    && candleItemDTOS.get(i + 2).getClose() > trendEquation.getValueAt(i + 2) // not confirmed before
                    && candleItemDTOS.get(i + 3).getClose() > trendEquation.getValueAt(i + 3) // not confirmed before
                    && candleItemDTOS.get(i + 4).getClose() < trendEquation.getValueAt(i + 4) // confirmed
            ) {
                Date signalScheduledAt = this.addUniteToDate(candleItemDTOS.get(i + 4).getDate(), interval, 1);
                metaData.put("isConfirmed", Boolean.TRUE);
                metaData.put("trendConfirmationDate", DateUtils.convertToString(candleItemDTOS.get(i + 4).getDate()));
                metaData.put("signalScheduledAt", DateUtils.convertToString(signalScheduledAt));
                metaData.put("index", 4);
                log.debug("The bearish trend has been confirmed: [{}]", JsonUtils.convertToString(metaData));
                if (DateUtils.roundNowAndCheckIfFuture(signalScheduledAt) == Boolean.TRUE) {
                    return this.takeAction(signalScheduledAt, interval, metaData);
                }
                log.debug("Bearish trend occurred in the past. Keep looking...");
            }

            log.debug("The bearish trend has not been confirmed: [{}]", JsonUtils.convertToString(metaData));
        }

        return result;
    }

    public List<SignalEntity> takeAction(Date firstSignalActionDate, TimeFrameEnum interval, Map<String, Object> metaData) {
        String detectionId = UUID.randomUUID().toString();
        SignalEntity sellSignal = new SignalEntity();
        sellSignal.setDetectionId(detectionId);
        sellSignal.setScheduledAt(firstSignalActionDate);
        sellSignal.setMetaData(JsonUtils.convertToString(metaData));
        sellSignal.setAction(SignalActionEnum.SELL);
        sellSignal.setRisk(0d);
        sellSignal.setAddedBy(this.getClass().getName());
        sellSignal.setAddedDate(DateUtils.getNow());

        SignalEntity buySignal = new SignalEntity();
        buySignal.setDetectionId(detectionId);
        buySignal.setMetaData(JsonUtils.convertToString(metaData));
        buySignal.setScheduledAt(this.addUniteToDate(firstSignalActionDate, interval, 2));
        buySignal.setAction(SignalActionEnum.BUY);
        buySignal.setRisk(0d);
        buySignal.setAddedBy(this.getClass().getName());
        buySignal.setAddedDate(DateUtils.getNow());
        return List.of(sellSignal, buySignal);
    }


    private Date addUniteToDate(Date inDate, TimeFrameEnum candleInterval, int unit) {
        return switch (candleInterval) {
            case ONE_MINUTE -> DateUtils.addMinutes(inDate, unit);
            case ONE_HOUR -> DateUtils.addHours(inDate, unit);
            case ONE_DAY -> DateUtils.addDays(inDate, unit);
            default -> throw new RuntimeException("Undefined Candle Interval");
        };
    }
}
