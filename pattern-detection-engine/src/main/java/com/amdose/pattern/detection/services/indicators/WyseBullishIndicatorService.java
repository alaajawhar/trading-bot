//package com.amdose.pattern.detection.services.indicators;
//
//import com.amdose.database.entities.CandleEntity;
//import com.amdose.database.entities.StrategyEntity;
//import com.amdose.database.entities.SignalEntity;
//import com.amdose.database.enums.SignalActionEnum;
//import com.amdose.database.enums.TimeFrameEnum;
//import com.amdose.database.repositories.IStrategyIndicator;
//import com.amdose.pattern.detection.dtos.CandleItemDTO;
//import com.amdose.pattern.detection.helpers.HighDetectionHelper;
//import com.amdose.pattern.detection.helpers.LowDetectionHelper;
//import com.amdose.pattern.detection.helpers.TrendHelper;
//import com.amdose.utils.DateUtils;
//import com.amdose.utils.JsonUtils;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
///**
// * @author Alaa Jawhar
// */
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class WyseBullishIndicatorService implements IIndicatorService {
//    private static final String NAME = "WyseBullishSignal";
//    private static final int LOOK_BACK_PERIOD = 20;
//    private StrategyEntity stategyEntity;
//
//    private final IStrategyIndicator indicatorRepository;
//
//    @Override
//    public String getName() {
//        return NAME;
//    }
//
//    @Override
//    public List<SignalEntity> apply(List<CandleItemDTO> candleItemDTOS) {
//        List<SignalEntity> result = new ArrayList<>();
//        TimeFrameEnum interval = candleItemDTOS.get(0).getTimeFrame();
//
//        List<Double> rsiValues = candleItemDTOS.stream().map(CandleItemDTO::getRsiValue).toList();
//        List<Double> closeValues = candleItemDTOS.stream().map(CandleEntity::getClose).toList();
//        List<Double> openValues = candleItemDTOS.stream().map(CandleEntity::getOpen).toList();
//
//        LowDetectionHelper priceLowDetection = new LowDetectionHelper(closeValues, LOOK_BACK_PERIOD);
//        HighDetectionHelper rsiHighDetection = new HighDetectionHelper(rsiValues, LOOK_BACK_PERIOD);
//
//        for (int i = 1; i < candleItemDTOS.size(); i++) {
//
//            if (rsiHighDetection.isHigherLowAt(i) == Boolean.FALSE
//                    || priceLowDetection.isLowerLowAt(i) == Boolean.FALSE) {
//                continue;
//            }
//
//            log.debug("Bullish trend has been detected. HighLow RSI & LowerLow Price on: [{}]"
//                    , candleItemDTOS.get(i).getDate()
//            );
//
//            Map<String, Object> metaData = new HashMap<>();
//            metaData.put("interval", interval.name());
//            metaData.put("detectionDate", DateUtils.convertToString(candleItemDTOS.get(i).getDate()));
//            metaData.put("priceCloseValueOnDetection", closeValues.get(i));
//            metaData.put("rsiValueOnDetection", rsiValues.get(i));
//            metaData.put("isConfirmed", Boolean.FALSE);
//
//
//            Optional<Integer> lastLowerLowIndexOptional = priceLowDetection.getLastLowerLow(i);
//
//            if (lastLowerLowIndexOptional.isEmpty()) {
//                log.debug("Skipped due to no last lower low found in [{}]", LOOK_BACK_PERIOD);
//                continue;
//            }
//
//            metaData.put("lastLowerLowDate", DateUtils.convertToString(candleItemDTOS.get(lastLowerLowIndexOptional.get()).getDate()));
//            metaData.put("lastLowerLowRsiValue", rsiValues.get(lastLowerLowIndexOptional.get()));
//
//            TrendHelper trendEquation = new TrendHelper(openValues, lastLowerLowIndexOptional.get(), i);
//
//            if (i + 1 < candleItemDTOS.size() && candleItemDTOS.get(i + 1).getClose() > trendEquation.getValueAt(i + 1)) {
//                Date signalScheduledAt = this.addUniteToDate(candleItemDTOS.get(i + 1).getDate(), interval, 1);
//                if (DateUtils.isFuture(signalScheduledAt) == Boolean.TRUE) {
//                    log.debug("Bearish Trend Confirmed on [{}]", candleItemDTOS.get(i + 1).getDate());
//                    metaData.put("trendConfirmationDate", candleItemDTOS.get(i + 1).getDate());
//                    metaData.put("isConfirmed", Boolean.TRUE);
//                    return this.takeAction(signalScheduledAt, interval, metaData);
//                }
//            }
//
//            if (i + 2 < candleItemDTOS.size()
//                    && candleItemDTOS.get(i + 2).getClose() > trendEquation.getValueAt(i + 2)
//                    && candleItemDTOS.get(i + 1).getClose() < trendEquation.getValueAt(i + 1) // not confirmed before
//            ) {
//                Date signalScheduledAt = this.addUniteToDate(candleItemDTOS.get(i + 2).getDate(), interval, 1);
//                if (DateUtils.isFuture(signalScheduledAt) == Boolean.TRUE) {
//                    log.debug("Bearish Trend Confirmed on [{}]", candleItemDTOS.get(i + 2).getDate());
//                    metaData.put("trendConfirmationDate", candleItemDTOS.get(i + 2).getDate());
//                    metaData.put("isConfirmed", Boolean.TRUE);
//                    return this.takeAction(signalScheduledAt, interval, metaData);
//                }
//            }
//
//            if (i + 3 < candleItemDTOS.size()
//                    && candleItemDTOS.get(i + 3).getClose() > trendEquation.getValueAt(i + 3)
//                    && candleItemDTOS.get(i + 2).getClose() < trendEquation.getValueAt(i + 2) // not confirmed before
//                    && candleItemDTOS.get(i + 1).getClose() < trendEquation.getValueAt(i + 1) // not confirmed before
//            ) {
//                Date signalScheduledAt = this.addUniteToDate(candleItemDTOS.get(i + 3).getDate(), interval, 1);
//                if (DateUtils.isFuture(signalScheduledAt) == Boolean.TRUE) {
//                    log.debug("Bearish Trend Confirmed on [{}]", candleItemDTOS.get(i + 3).getDate());
//                    metaData.put("trendConfirmationDate", candleItemDTOS.get(i + 3).getDate());
//                    metaData.put("isConfirmed", Boolean.TRUE);
//                    return this.takeAction(signalScheduledAt, interval, metaData);
//                }
//            }
//
//            log.debug("The bullish trend has not been confirmed or it occurred in the past.");
//        }
//        return result;
//    }
//
//    private Date addTime(Date inDate, TimeFrameEnum candleInterval) {
//        switch (candleInterval) {
//            case ONE_MINUTE:
//                return DateUtils.addMinutes(inDate, 2);
//            case ONE_HOUR:
//                return DateUtils.addHours(inDate, 2);
//            case ONE_DAY:
//                return DateUtils.addDays(inDate, 2);
//            default:
//                throw new RuntimeException("Undefined Candle Interval");
//        }
//    }
//
//    public List<SignalEntity> takeAction(Date firstSignalActionDate, TimeFrameEnum interval, Map<String, Object> metaData) {
//        String detectionId = UUID.randomUUID().toString();
//        SignalEntity buySignal = new SignalEntity();
//        buySignal.setIndicator(stategyEntity);
//        buySignal.setDetectionId(detectionId);
//        buySignal.setMetaData(JsonUtils.convertToString(metaData));
//        buySignal.setScheduledAt(firstSignalActionDate);
//        buySignal.setAction(SignalActionEnum.BUY);
//        buySignal.setRisk(0d);
//        buySignal.setAddedBy(this.getClass().getName());
//        buySignal.setAddedDate(DateUtils.getNow());
//
//        SignalEntity sellSignal = new SignalEntity();
//        sellSignal.setIndicator(stategyEntity);
//        sellSignal.setMetaData(JsonUtils.convertToString(metaData));
//        sellSignal.setDetectionId(detectionId);
//        sellSignal.setScheduledAt(this.addUniteToDate(firstSignalActionDate, interval, 2));
//        sellSignal.setAction(SignalActionEnum.SELL);
//        sellSignal.setRisk(0d);
//        sellSignal.setAddedBy(this.getClass().getName());
//        sellSignal.setAddedDate(DateUtils.getNow());
//
//        return List.of(buySignal, sellSignal);
//    }
//
//    private Date addUniteToDate(Date inDate, TimeFrameEnum candleInterval, int unit) {
//        return switch (candleInterval) {
//            case ONE_MINUTE -> DateUtils.addMinutes(inDate, unit);
//            case ONE_HOUR -> DateUtils.addHours(inDate, unit);
//            case ONE_DAY -> DateUtils.addDays(inDate, unit);
//            default -> throw new RuntimeException("Undefined Candle Interval");
//        };
//    }
//}
