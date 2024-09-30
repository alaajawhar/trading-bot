package com.amdose.base.services.test;

import com.amdose.base.exceptions.InvalidRequestException;
import com.amdose.base.models.enums.OutcomeResultEnum;
import com.amdose.base.payloads.dashboard.*;
import com.amdose.base.payloads.test.strategy.GetStrategyTestRequest;
import com.amdose.base.payloads.test.strategy.GetStrategyTestResponse;
import com.amdose.base.payloads.test.strategy.GetTestSignalsResponse;
import com.amdose.base.payloads.test.strategy.TestSignalItem;
import com.amdose.base.utils.DisplayUtils;
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
import com.amdose.utils.JsonUtils;
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


        List<TestTradeDTO> allDetectedSignals = this.getProfitTestCandles(optionalStrategy.get(), optionalSymbol.get());

        // SUMMARY RESPONSE
        response.setSummaryResponse(this.generateSummary(allDetectedSignals));

        // LINE CHART RESPONSE
        response.setPerformanceOverTimeResponse(this.generateLineChartResponse(allDetectedSignals));

        // RADAR RESPONSE
        response.setPerformanceBaseOnTimeframesResponse(this.generateRadarChartResponse(optionalStrategy.get().getName(), allDetectedSignals));

        // PIE RESPONSE
        response.setPieChartResponse(this.generatePieChartResponse(allDetectedSignals));

        // MULTI-BAR RESPONSE
        response.setMultiBarChartResponse(this.generateMultiBarChart(allDetectedSignals));

        // SIGNALS RESPONSE
        response.setSignals(this.generateTestSignalsResponse(allDetectedSignals));

        return response;
    }

    private List<TestTradeDTO> getProfitTestCandles(StrategyEntity strategy, SymbolEntity symbol) {
        List<TestTradeDTO> response = new ArrayList<>();
        for (TimeFrameEnum timeframe : TimeFrameEnum.values()) {
//            Date today = Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
//            List<CandleEntity> candleEntityList = candleRepository.findAllByDateAfterAndSymbolAndTimeFrameOrderByDateAsc(today, symbol, timeframe);
            List<CandleEntity> candleEntityList = candleRepository.findLastBySymbolAndTimeFrameOrderByDateAsc(symbol, timeframe);
            List<SignalEntity> signalEntityList = strategyExecutorService.executeStrategy(strategy, candleEntityList);


            Map<Date, CandleEntity> dateCandleMap = candleEntityList.stream()
                    .collect(Collectors.toMap(item -> DateUtils.roundSecondsAndMilliseconds(item.getDate()), Function.identity()));

            Map<String, List<SignalEntity>> detectionIdSignalsMap = signalEntityList.stream()
                    .collect(groupingBy(SignalEntity::getDetectionId));


            for (CandleEntity candleItem : dateCandleMap.values()) {
                TestTradeDTO testTradeDTO = new TestTradeDTO();
                testTradeDTO.setTimeFrame(timeframe);
                testTradeDTO.setDate(candleItem.getDate());
                testTradeDTO.setProfit(0.0d);

                for (List<SignalEntity> groupedByDetectionIdSignals : detectionIdSignalsMap.values()) {

                    // Signal in the future, just detected
                    if (DateUtils.isFutureInHourMinuteSecond(groupedByDetectionIdSignals.get(1).getScheduledAt())) {
                        continue;
                    }

                    if (DateUtils.areDatesEqualInHourMinuteSecond(candleItem.getDate(), groupedByDetectionIdSignals.get(0).getScheduledAt())) {
                        Double profit = dateCandleMap.get(groupedByDetectionIdSignals.get(0).getScheduledAt()).getOpen()
                                - dateCandleMap.get(groupedByDetectionIdSignals.get(1).getScheduledAt()).getOpen();
                        testTradeDTO.setProfit(profit);
                        testTradeDTO.setMetaData(groupedByDetectionIdSignals.get(0).getMetaData());
                    }

                }
                response.add(testTradeDTO);
            }

        }

        return response;
    }

    private GetDashboardSummaryResponse generateSummary(List<TestTradeDTO> detectedSignals) {
        Long totalWins = detectedSignals.stream().filter(item -> item.getProfit() > 0).count();
        Long totalLoses = detectedSignals.stream().filter(item -> item.getProfit() < 0).count();
        double totalProfit = detectedSignals.stream().mapToDouble(TestTradeDTO::getProfit).sum();

        GetDashboardSummaryResponse summaryResponse = new GetDashboardSummaryResponse();
        summaryResponse.setTotalWins(totalWins);
        summaryResponse.setTotalProfit((long) totalProfit);
        summaryResponse.setTotalLose(totalLoses);
        return summaryResponse;
    }

    private GetLineChartResponse generateLineChartResponse(List<TestTradeDTO> detectedSignals) {
        GetLineChartResponse response = new GetLineChartResponse();
        response.setLabels(List.of("0", "100", "200", "300", "400", "500", "600", "700", "800", "900", "1000"));

        List<ChartItem> list = new ArrayList<>();
        int i = 0;
        for (TimeFrameEnum timeFrame : TimeFrameEnum.values()) {
            ChartItem chartItem = new ChartItem();
            chartItem.setChartName(timeFrame.getLabel());
            chartItem.setChartColor(COLORS.get(i++));

            double sum0_100 = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).limit(100).mapToDouble(TestTradeDTO::getProfit).sum();
            double sum100_200 = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).skip(100).limit(100).mapToDouble(TestTradeDTO::getProfit).sum();
            double sum200_300 = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).skip(200).limit(100).mapToDouble(TestTradeDTO::getProfit).sum();
            double sum300_400 = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).skip(300).limit(100).mapToDouble(TestTradeDTO::getProfit).sum();
            double sum400_500 = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).skip(400).limit(100).mapToDouble(TestTradeDTO::getProfit).sum();
            double sum500_600 = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).skip(500).limit(100).mapToDouble(TestTradeDTO::getProfit).sum();
            double sum600_700 = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).skip(600).limit(100).mapToDouble(TestTradeDTO::getProfit).sum();
            double sum700_800 = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).skip(700).limit(100).mapToDouble(TestTradeDTO::getProfit).sum();
            double sum800_900 = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).skip(800).limit(100).mapToDouble(TestTradeDTO::getProfit).sum();
            double sum900_1000 = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).skip(900).limit(100).mapToDouble(TestTradeDTO::getProfit).sum();

            chartItem.setData(List.of(
                    0.0d,
                    DisplayUtils.roundAmountToDouble(sum0_100),
                    DisplayUtils.roundAmountToDouble(sum100_200),
                    DisplayUtils.roundAmountToDouble(sum200_300),
                    DisplayUtils.roundAmountToDouble(sum300_400),
                    DisplayUtils.roundAmountToDouble(sum400_500),
                    DisplayUtils.roundAmountToDouble(sum500_600),
                    DisplayUtils.roundAmountToDouble(sum600_700),
                    DisplayUtils.roundAmountToDouble(sum700_800),
                    DisplayUtils.roundAmountToDouble(sum800_900),
                    DisplayUtils.roundAmountToDouble(sum900_1000)
            ));

            list.add(chartItem);
        }

        response.setList(list);
        return response;
    }

    private GetStrategiesPerformanceBaseOnTimeframesResponse generateRadarChartResponse(String strategyName, List<TestTradeDTO> detectedSignals) {
        GetStrategiesPerformanceBaseOnTimeframesResponse response = new GetStrategiesPerformanceBaseOnTimeframesResponse();
        response.setLabels(Arrays.stream(TimeFrameEnum.values()).map(TimeFrameEnum::getLabel).collect(Collectors.toList()));
        List<ChartItem> list = new ArrayList<>();

        ChartItem chartItem = new ChartItem();
        chartItem.setChartName(strategyName);
        chartItem.setChartColor(COLORS.get(0));

        for (TimeFrameEnum timeFrame : TimeFrameEnum.values()) {
            double timeframeSum = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).mapToDouble(TestTradeDTO::getProfit).sum();
            chartItem.addData(DisplayUtils.roundAmountToDouble(timeframeSum));
            list.add(chartItem);
        }


        response.setList(list);
        return response;
    }

    private GetPieChartResponse generatePieChartResponse(List<TestTradeDTO> detectedSignals) {
        GetPieChartResponse response = new GetPieChartResponse();
        response.setLabels(Arrays.stream(TimeFrameEnum.values()).map(TimeFrameEnum::getLabel).collect(Collectors.toList()));

        List<PieChartItem> list = new ArrayList<>();

        List<TestTradeDTO> signals = detectedSignals.stream()
                .filter(signal -> signal.getProfit() != 0).toList();

        int i = 0;
        for (TimeFrameEnum timeFrame : TimeFrameEnum.values()) {
            PieChartItem chartItem = new PieChartItem();
            chartItem.setChartColor(COLORS.get(i++));
            chartItem.setChartName(timeFrame.getLabel());

            double timeframeCount = signals.stream()
                    .filter(signal -> signal.getTimeFrame() == timeFrame)
                    .count();

            chartItem.setData(timeframeCount);
            list.add(chartItem);
        }


        response.setList(list);
        return response;
    }

    private GetMultiBarChartResponse generateMultiBarChart(List<TestTradeDTO> detectedSignals) {
        GetMultiBarChartResponse response = new GetMultiBarChartResponse();
        response.setLabels(Arrays.stream(TimeFrameEnum.values()).map(TimeFrameEnum::getLabel).collect(Collectors.toList()));

        List<MultiBarChartItem> list = new ArrayList<>();
        List<Double> winsData = new ArrayList<>();
        List<Double> losesData = new ArrayList<>();

        MultiBarChartItem winsChart = new MultiBarChartItem();
        winsChart.setChartColor("#2ecb18");
        winsChart.setChartName("Wins");
        winsChart.setData(winsData);

        MultiBarChartItem losesChart = new MultiBarChartItem();
        losesChart.setChartColor("#cb3818");
        losesChart.setChartName("Loses");
        losesChart.setData(losesData);


        for (TimeFrameEnum timeFrame : TimeFrameEnum.values()) {
            double winsSum = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).mapToDouble(TestTradeDTO::getProfit).filter(profit -> profit > 0).sum();
            double losesSum = detectedSignals.stream().filter(signal -> signal.getTimeFrame() == timeFrame).mapToDouble(TestTradeDTO::getProfit).filter(profit -> profit < 0).sum();

            winsData.add(DisplayUtils.roundAmountToDouble(winsSum));
            losesData.add(DisplayUtils.roundAmountToDouble(Math.abs(losesSum)));
        }

        list.add(winsChart);
        list.add(losesChart);

        response.setList(list);
        return response;
    }

    private GetTestSignalsResponse generateTestSignalsResponse(List<TestTradeDTO> allDetectedSignals) {
        GetTestSignalsResponse response = new GetTestSignalsResponse();
        List<TestSignalItem> list = new ArrayList<>();

        // Sort descending
        allDetectedSignals.sort((c1, c2) -> c2.getDate().compareTo(c1.getDate()));

        for (TestTradeDTO detectedSignal : allDetectedSignals) {

            if (detectedSignal.getProfit() == 0) {
                continue;
            }

            TestSignalItem testSignalItem = new TestSignalItem();
            testSignalItem.setTimeframe(detectedSignal.getTimeFrame());
            testSignalItem.setOutcomeResult(detectedSignal.getProfit() > 0 ? OutcomeResultEnum.WIN : OutcomeResultEnum.LOSE);
            testSignalItem.setProfit(DisplayUtils.roundAmountToString(detectedSignal.getProfit()));
            testSignalItem.setMetaData(JsonUtils.convertToObject(detectedSignal.getMetaData(), Object.class));
            testSignalItem.setDate(detectedSignal.getDate());
            list.add(testSignalItem);
        }

        response.setList(list);
        return response;
    }


    @Data
    private class TestTradeDTO {
        private Double profit;
        private TimeFrameEnum timeFrame;
        private String metaData;
        private Date date;
    }

}
