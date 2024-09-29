package com.amdose.base.services.test;

import com.amdose.base.exceptions.InvalidRequestException;
import com.amdose.base.payloads.dashboard.ChartItem;
import com.amdose.base.payloads.dashboard.GetDashboardSummaryResponse;
import com.amdose.base.payloads.dashboard.GetLineChartResponse;
import com.amdose.base.payloads.dashboard.GetStrategiesPerformanceBaseOnTimeframesResponse;
import com.amdose.base.payloads.test.strategy.GetStrategyTestRequest;
import com.amdose.base.payloads.test.strategy.GetStrategyTestResponse;
import com.amdose.database.entities.CandleEntity;
import com.amdose.database.entities.SignalEntity;
import com.amdose.database.entities.StrategyEntity;
import com.amdose.database.entities.SymbolEntity;
import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.database.repositories.ICandleRepository;
import com.amdose.database.repositories.IStrategyRepository;
import com.amdose.database.repositories.ISymbolRepository;
import com.amdose.pattern.detection.services.StrategyExecutorService;
import com.amdose.utils.DateUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StrategyTestService {
    public static final List<String> COLORS = List.of(
            "#012970",
            "#d51bb6",
            "#1bbfd5",
            "#1bd565",
            "#a0d51b",
            "#d57e1b",
            "#d51b1b"
    );


    private final StrategyExecutorService strategyExecutorService;
    private final IStrategyRepository strategyRepository;
    private final ISymbolRepository symbolRepository;
    private final ICandleRepository candleRepository;

    public GetStrategyTestResponse getStrategyTest(GetStrategyTestRequest request) {
        GetStrategyTestResponse response = new GetStrategyTestResponse();

        Optional<StrategyEntity> optionalStrategy = strategyRepository.findById(request.getStrategyId());

        if (optionalStrategy.isEmpty() == Boolean.TRUE) {
            throw new InvalidRequestException("Invalid Strategy Id");
        }

        Optional<SymbolEntity> optionalSymbol = symbolRepository.findById(request.getSymbolId());

        if (optionalSymbol.isEmpty() == Boolean.TRUE) {
            throw new InvalidRequestException("Invalid Symbol Id");
        }


        List<ProfitTestCandle> allDetectedSignals = this.getProfitTestCandles(optionalStrategy.get(), optionalSymbol.get());

        // SUMMARY RESPONSE
        response.setSummaryResponse(this.generateSummary(allDetectedSignals));

        // LINE CHART RESPONSE
        response.setPerformanceOverTimeResponse(this.generateLineChartResponse(allDetectedSignals));

        // RADAR RESPONSE
        response.setPerformanceBaseOnTimeframesResponse(this.generateRadarChartResponse(optionalStrategy.get().getName(), allDetectedSignals));

        return response;
    }

    private List<ProfitTestCandle> getProfitTestCandles(StrategyEntity strategy, SymbolEntity symbol) {
        List<ProfitTestCandle> response = new ArrayList<>();
        for (TimeFrameEnum timeframe : TimeFrameEnum.values()) {
            List<CandleEntity> candleEntityList = candleRepository.findLastBySymbolAndTimeFrameOrderByDateAsc(symbol, timeframe);
            List<SignalEntity> signalEntityList = strategyExecutorService.executeStrategy(strategy, candleEntityList);


            Map<Date, CandleEntity> dateCandleMap = candleEntityList.stream()
                    .collect(Collectors.toMap(item -> DateUtils.roundSecondsAndMilliseconds(item.getDate()), Function.identity()));

            Map<String, List<SignalEntity>> detectionIdSignalsMap = signalEntityList.stream()
                    .collect(groupingBy(SignalEntity::getDetectionId));


            for (CandleEntity candleItem : dateCandleMap.values()) {
                ProfitTestCandle profitTestCandle = new ProfitTestCandle();
                profitTestCandle.setTimeFrame(timeframe);
                profitTestCandle.setDate(candleItem.getDate());
                profitTestCandle.setProfit(0.0d);

                for (List<SignalEntity> groupedByDetectionIdSignals : detectionIdSignalsMap.values()) {

                    if (DateUtils.areDatesEqualInHourMinuteSecond(candleItem.getDate(), groupedByDetectionIdSignals.get(0).getScheduledAt())) {
                        Double profit = dateCandleMap.get(groupedByDetectionIdSignals.get(0).getScheduledAt()).getOpen()
                                - dateCandleMap.get(groupedByDetectionIdSignals.get(1).getScheduledAt()).getOpen();
                        profitTestCandle.setProfit(profit);
                    }

                }
                response.add(profitTestCandle);
            }

        }

        return response;
    }

    private GetDashboardSummaryResponse generateSummary(List<ProfitTestCandle> detectedSignals) {
        Long totalWins = detectedSignals.stream().filter(item -> item.getProfit() > 0).count();
        Long totalLoses = detectedSignals.stream().filter(item -> item.getProfit() < 0).count();
        double totalProfit = detectedSignals.stream().mapToDouble(ProfitTestCandle::getProfit).sum();

        GetDashboardSummaryResponse summaryResponse = new GetDashboardSummaryResponse();
        summaryResponse.setTotalWins(totalWins);
        summaryResponse.setTotalProfit((long) totalProfit);
        summaryResponse.setTotalLose(totalLoses);
        return summaryResponse;
    }

    private GetLineChartResponse generateLineChartResponse(List<ProfitTestCandle> detectedSignals) {
        GetLineChartResponse response = new GetLineChartResponse();
        response.setLabels(List.of("0", "100", "200", "300", "400", "500", "600", "700", "800", "900", "1000"));

        List<ChartItem> list = new ArrayList<>();
        int i = 0;
        for (TimeFrameEnum timeFrame : TimeFrameEnum.values()) {
            ChartItem chartItem = new ChartItem();
            chartItem.setChartName(timeFrame.getLabel());
            chartItem.setChartColor(COLORS.get(i++));

            double sum0_100 = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).limit(100).mapToDouble(ProfitTestCandle::getProfit).sum();
            double sum100_200 = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).skip(100).limit(100).mapToDouble(ProfitTestCandle::getProfit).sum();
            double sum200_300 = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).skip(200).limit(100).mapToDouble(ProfitTestCandle::getProfit).sum();
            double sum300_400 = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).skip(300).limit(100).mapToDouble(ProfitTestCandle::getProfit).sum();
            double sum400_500 = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).skip(400).limit(100).mapToDouble(ProfitTestCandle::getProfit).sum();
            double sum500_600 = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).skip(500).limit(100).mapToDouble(ProfitTestCandle::getProfit).sum();
            double sum600_700 = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).skip(600).limit(100).mapToDouble(ProfitTestCandle::getProfit).sum();
            double sum700_800 = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).skip(700).limit(100).mapToDouble(ProfitTestCandle::getProfit).sum();
            double sum800_900 = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).skip(800).limit(100).mapToDouble(ProfitTestCandle::getProfit).sum();
            double sum900_1000 = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).skip(900).limit(100).mapToDouble(ProfitTestCandle::getProfit).sum();

            chartItem.setData(List.of(
                    0.0d,
                    sum0_100,
                    sum100_200,
                    sum200_300,
                    sum300_400,
                    sum400_500,
                    sum500_600,
                    sum600_700,
                    sum700_800,
                    sum800_900,
                    sum900_1000
            ));

            list.add(chartItem);
        }

        response.setList(list);
        return response;
    }

    private GetStrategiesPerformanceBaseOnTimeframesResponse generateRadarChartResponse(String strategyName, List<ProfitTestCandle> detectedSignals) {
        GetStrategiesPerformanceBaseOnTimeframesResponse response = new GetStrategiesPerformanceBaseOnTimeframesResponse();
        response.setLabels(Arrays.stream(TimeFrameEnum.values()).map(TimeFrameEnum::getLabel).collect(Collectors.toList()));
        List<ChartItem> list = new ArrayList<>();

        ChartItem chartItem = new ChartItem();
        chartItem.setChartName(strategyName);
        chartItem.setChartColor(COLORS.get(0));

        for (TimeFrameEnum timeFrame : TimeFrameEnum.values()) {
            double timeframeSum = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).mapToDouble(ProfitTestCandle::getProfit).sum();
            chartItem.addData(timeframeSum);
            list.add(chartItem);
        }


        response.setList(list);
        return response;
    }

    @Data
    private class ProfitTestCandle {
        private Double profit;
        private TimeFrameEnum timeFrame;
        private Date date;
    }

}
