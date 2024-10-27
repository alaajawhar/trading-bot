package com.amdose.base.services;

import com.amdose.base.models.dtos.TradeDto;
import com.amdose.base.models.enums.OutcomeResultEnum;
import com.amdose.base.payloads.charts.*;
import com.amdose.base.payloads.dashboard.*;
import com.amdose.base.utils.DisplayUtils;
import com.amdose.database.entities.CandleEntity;
import com.amdose.database.entities.SignalEntity;
import com.amdose.database.entities.StrategyEntity;
import com.amdose.database.entities.SymbolEntity;
import com.amdose.database.enums.SignalActionEnum;
import com.amdose.database.enums.TimeFrameEnum;
import com.amdose.database.repositories.ICandleRepository;
import com.amdose.database.repositories.ISignalRepository;
import com.amdose.database.repositories.IStrategyRepository;
import com.amdose.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final List<String> COLORS = List.of(
            "#012970",
            "#d51bb6",
            "#1bbfd5",
            "#1bd565",
            "#a0d51b",
            "#d57e1b",
            "#d51b1b"
    );

    private final IStrategyRepository strategyRepository;
    private final ICandleRepository candleRepository;
    private final ISignalRepository signalRepository;

    public GetDashboardOverviewResponse getDashboardOverview() {
        GetDashboardOverviewResponse response = new GetDashboardOverviewResponse();

        // Signals
        List<SignalEntity> thisMonthSignals = signalRepository.findAllByAddedDateAfterOrderByAddedDateAsc(DateUtils.getFirstDayOfCurrentMonth());
        List<SignalEntity> thisWeekSignals = thisMonthSignals.stream().filter(item -> item.getAddedDate().after(DateUtils.getFirstDayOfCurrentWeek())).toList();
        List<SignalEntity> todaysSignals = thisWeekSignals.stream().filter(item -> item.getAddedDate().after(DateUtils.getTodayAtStartOfDay())).toList();

        // Trades
        List<TradeDto> thisMonthTrades = thisMonthSignals.stream().collect(groupingBy(SignalEntity::getDetectionId)).values().stream().filter(item -> item.size() == 2).map(this::convertToTrade).filter(Optional::isPresent).map(Optional::get).toList();
        List<TradeDto> thisWeekTrades = thisWeekSignals.stream().collect(groupingBy(SignalEntity::getDetectionId)).values().stream().filter(item -> item.size() == 2).map(this::convertToTrade).filter(Optional::isPresent).map(Optional::get).toList();
        List<TradeDto> todaysTrades = todaysSignals.stream().collect(groupingBy(SignalEntity::getDetectionId)).values().stream().filter(item -> item.size() == 2).map(this::convertToTrade).filter(Optional::isPresent).map(Optional::get).toList();

        response.setDashboardSummary(this.getDashboardSummary(thisMonthTrades, thisWeekTrades, todaysTrades));
        response.setLineChartResponse(this.getDashboardLineChart(thisMonthTrades, thisWeekTrades, todaysTrades));
        response.setRadarChartResponse(this.getDashboardRadarChart(thisMonthTrades, thisWeekTrades, todaysTrades));
        response.setPieChartResponse(this.getDashboardPieChart(thisMonthTrades, thisWeekTrades, todaysTrades));
        response.setMultiBarChartResponse(this.getDashboardMultiBarChart(thisMonthTrades, thisWeekTrades, todaysTrades));

        return response;
    }

    private Optional<TradeDto> convertToTrade(List<SignalEntity> signals) {
        TradeDto response = new TradeDto();

        if (signals == null || signals.size() != 2) {
            log.warn("Invalid Signal! should should container exact 2 items(sell and buy)");
            return Optional.empty();
        }


        SymbolEntity symbol = signals.get(0).getBot().getSymbol();
        TimeFrameEnum timeframe = signals.get(0).getBot().getTimeFrame();
        Long strategyId = signals.get(0).getBot().getStrategy().getId();

        Optional<SignalEntity> sellSignalOptional = signals.stream().filter(signalEntity -> signalEntity.getAction() == SignalActionEnum.SELL).findFirst();
        Optional<SignalEntity> buySignalOptional = signals.stream().filter(signalEntity -> signalEntity.getAction() == SignalActionEnum.BUY).findFirst();

        if (sellSignalOptional.isEmpty() || buySignalOptional.isEmpty()) {
            log.warn("Signal does not have both SELL and BUY actions");
            return Optional.empty();
        }

        Optional<CandleEntity> sellCandle = candleRepository.findTopBySymbolAndTimeFrameAndDate(symbol, timeframe, sellSignalOptional.get().getScheduledAt());
        Optional<CandleEntity> buyCandle = candleRepository.findTopBySymbolAndTimeFrameAndDate(symbol, timeframe, buySignalOptional.get().getScheduledAt());

        if (sellCandle.isEmpty() || buyCandle.isEmpty()) {
            log.warn("Candle not found");
            return Optional.empty();
        }

        response.setStrategyId(strategyId);
        response.setProfit(sellCandle.get().getOpen() - buyCandle.get().getOpen());
        response.setTimeFrame(timeframe);
        response.setMetaData(signals.get(0).getMetaData());
        response.setDate(signals.get(0).getScheduledAt());

        return Optional.of(response);
    }


    private GetDashboardSummaryResponse getDashboardSummary(List<TradeDto> thisMonthTrades, List<TradeDto> thisWeekTrades, List<TradeDto> todaysTrades) {
        GetDashboardSummaryResponse summaryResponse = new GetDashboardSummaryResponse();

        // TODAY
        DashboardSummaryItem todaysSummary = new DashboardSummaryItem();
        todaysSummary.setTotalWins(todaysTrades.stream().filter(item -> item.getProfit() > 0).count());
        todaysSummary.setTotalProfit((long) todaysTrades.stream().mapToDouble(TradeDto::getProfit).sum());
        todaysSummary.setTotalLose(todaysTrades.stream().filter(item -> item.getProfit() < 0).count());


        DashboardSummaryItem thisWeekSummary = new DashboardSummaryItem();
        thisWeekSummary.setTotalWins(thisWeekTrades.stream().filter(item -> item.getProfit() > 0).count());
        thisWeekSummary.setTotalProfit((long) thisWeekTrades.stream().mapToDouble(TradeDto::getProfit).sum());
        thisWeekSummary.setTotalLose(thisWeekTrades.stream().filter(item -> item.getProfit() < 0).count());

        DashboardSummaryItem thisMonthSummary = new DashboardSummaryItem();
        thisMonthSummary.setTotalWins(thisMonthTrades.stream().filter(item -> item.getProfit() > 0).count());
        thisMonthSummary.setTotalProfit((long) thisMonthTrades.stream().mapToDouble(TradeDto::getProfit).sum());
        thisMonthSummary.setTotalLose(thisMonthTrades.stream().filter(item -> item.getProfit() < 0).count());

        summaryResponse.setTodaySummary(todaysSummary);
        summaryResponse.setThisWeekSummary(thisWeekSummary);
        summaryResponse.setThisMonthSummary(thisMonthSummary);

        return summaryResponse;
    }

    private GetDashboardLineChartResponse getDashboardLineChart(List<TradeDto> thisMonthTrades, List<TradeDto> thisWeekTrades, List<TradeDto> todaysTrades) {
        GetDashboardLineChartResponse response = new GetDashboardLineChartResponse();

        GetLineChartResponse thisWeekLineChart = new GetLineChartResponse();
        GetLineChartResponse thisMonthLineChart = new GetLineChartResponse();

        /*
         * TODAY
         */
        GetLineChartResponse todaysLineChart = new GetLineChartResponse();
        todaysLineChart.setLabels(List.of("00:00", "01:00", "02:00", "03:00", "04:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"));
        List<LineChartItem> todaysLineChartList = new ArrayList<>();

        int i = 0;
        Map<Long, List<TradeDto>> todaysTradesGroupedByStrategyId = todaysTrades.stream().collect(groupingBy(TradeDto::getStrategyId));
        for (Map.Entry<Long, List<TradeDto>> entry : todaysTradesGroupedByStrategyId.entrySet()) {
            LineChartItem lineChartItem = new LineChartItem();

            if (strategyRepository.findById(entry.getKey()).isEmpty()) {
                continue;
            }

            lineChartItem.setChartName(strategyRepository.findById(entry.getKey()).get().getDescription());
            lineChartItem.setChartColor(COLORS.get(i++));
            lineChartItem.setData(this.calculateHourlyProfit(entry.getValue()));
            todaysLineChartList.add(lineChartItem);
        }
        todaysLineChart.setList(todaysLineChartList);


        /*
         * THIS WEEK
         */
        thisWeekLineChart.setLabels(List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));
        List<LineChartItem> thisWeekLineChartList = new ArrayList<>();

        int j = 0;
        Map<Long, List<TradeDto>> thisWeekTradesGroupedByStrategyId = thisWeekTrades.stream().collect(groupingBy(TradeDto::getStrategyId));
        for (Map.Entry<Long, List<TradeDto>> entry : thisWeekTradesGroupedByStrategyId.entrySet()) {
            LineChartItem lineChartItem = new LineChartItem();

            if (strategyRepository.findById(entry.getKey()).isEmpty()) {
                continue;
            }

            lineChartItem.setChartName(strategyRepository.findById(entry.getKey()).get().getDescription());
            lineChartItem.setChartColor(COLORS.get(j++));
            lineChartItem.setData(this.calculateDailyProfit(entry.getValue()));
            thisWeekLineChartList.add(lineChartItem);
        }
        thisWeekLineChart.setList(thisWeekLineChartList);

        /*
         * THIS MONTH
         */
        thisMonthLineChart.setLabels(List.of("Week 1", "Week 2", "Week 3", "Week 4"));
        List<LineChartItem> thisMonthLineChartList = new ArrayList<>();

        int k = 0;
        Map<Long, List<TradeDto>> thisMonthTradesGroupedByStrategyId = thisMonthTrades.stream().collect(groupingBy(TradeDto::getStrategyId));
        for (Map.Entry<Long, List<TradeDto>> entry : thisMonthTradesGroupedByStrategyId.entrySet()) {
            LineChartItem lineChartItem = new LineChartItem();

            if (strategyRepository.findById(entry.getKey()).isEmpty()) {
                continue;
            }

            lineChartItem.setChartName(strategyRepository.findById(entry.getKey()).get().getDescription());
            lineChartItem.setChartColor(COLORS.get(k++));
            lineChartItem.setData(this.calculateWeeklyProfit(entry.getValue()));
            thisMonthLineChartList.add(lineChartItem);
        }
        thisMonthLineChart.setList(thisMonthLineChartList);

        response.setTodayLineChart(todaysLineChart);
        response.setThisWeekLineChart(thisWeekLineChart);
        response.setThisMonthLineChart(thisMonthLineChart);

        return response;
    }

    private GetDashboardRadarChartResponse getDashboardRadarChart(List<TradeDto> thisMonthTrades, List<TradeDto> thisWeekTrades, List<TradeDto> todaysTrades) {
        GetDashboardRadarChartResponse response = new GetDashboardRadarChartResponse();

        List<String> labels = Arrays.stream(TimeFrameEnum.values()).map(TimeFrameEnum::getLabel).toList();

        GetRadarChartResponse todaysRadarChart = new GetRadarChartResponse();
        GetRadarChartResponse thisWeekRadarChart = new GetRadarChartResponse();
        GetRadarChartResponse thisMonthRadarChart = new GetRadarChartResponse();

        /*
         * TODAY
         */
        todaysRadarChart.setLabels(labels);
        List<RadarChartItem> todaysRadarChartData = new ArrayList<>();

        int i = 0;
        Map<Long, List<TradeDto>> todayTradesGroupedByStrategyId = todaysTrades.stream().collect(groupingBy(TradeDto::getStrategyId));
        for (Map.Entry<Long, List<TradeDto>> entry : todayTradesGroupedByStrategyId.entrySet()) {
            RadarChartItem radarChartItem = new RadarChartItem();

            if (strategyRepository.findById(entry.getKey()).isEmpty()) {
                continue;
            }

            radarChartItem.setChartName(strategyRepository.findById(entry.getKey()).get().getDescription());
            radarChartItem.setChartColor(COLORS.get(i++));

            for (TimeFrameEnum timeframe : TimeFrameEnum.values()) {
                List<TradeDto> timeframedTrades = entry.getValue().stream().filter(item -> item.getTimeFrame() == timeframe).toList();
                double timeframedTotalProfit = timeframedTrades.stream().mapToDouble(TradeDto::getProfit).sum();
                radarChartItem.addData(DisplayUtils.roundAmountToDouble(timeframedTotalProfit));
            }

            todaysRadarChartData.add(radarChartItem);
        }

        todaysRadarChart.setList(todaysRadarChartData);

        /*
         * THIS WEEK
         */
        thisWeekRadarChart.setLabels(labels);
        List<RadarChartItem> thisWeekRadarChartData = new ArrayList<>();

        int j = 0;
        Map<Long, List<TradeDto>> thisWeekTradesGroupedByStrategyId = thisWeekTrades.stream().collect(groupingBy(TradeDto::getStrategyId));
        for (Map.Entry<Long, List<TradeDto>> entry : thisWeekTradesGroupedByStrategyId.entrySet()) {
            RadarChartItem radarChartItem = new RadarChartItem();

            if (strategyRepository.findById(entry.getKey()).isEmpty()) {
                continue;
            }

            radarChartItem.setChartName(strategyRepository.findById(entry.getKey()).get().getDescription());
            radarChartItem.setChartColor(COLORS.get(j++));

            for (TimeFrameEnum timeframe : TimeFrameEnum.values()) {
                List<TradeDto> timeframedTrades = entry.getValue().stream().filter(item -> item.getTimeFrame() == timeframe).toList();
                double timeframedTotalProfit = timeframedTrades.stream().mapToDouble(TradeDto::getProfit).sum();
                radarChartItem.addData(DisplayUtils.roundAmountToDouble(timeframedTotalProfit));
            }

            thisWeekRadarChartData.add(radarChartItem);
        }

        thisWeekRadarChart.setList(thisWeekRadarChartData);

        /*
         * THIS MONTH
         */
        thisMonthRadarChart.setLabels(labels);
        List<RadarChartItem> thisMonthRadarChartData = new ArrayList<>();

        int k = 0;
        Map<Long, List<TradeDto>> thisMonthTradesGroupedByStrategyId = thisMonthTrades.stream().collect(groupingBy(TradeDto::getStrategyId));
        for (Map.Entry<Long, List<TradeDto>> entry : thisMonthTradesGroupedByStrategyId.entrySet()) {
            RadarChartItem radarChartItem = new RadarChartItem();

            if (strategyRepository.findById(entry.getKey()).isEmpty()) {
                continue;
            }

            radarChartItem.setChartName(strategyRepository.findById(entry.getKey()).get().getDescription());
            radarChartItem.setChartColor(COLORS.get(k++));

            for (TimeFrameEnum timeframe : TimeFrameEnum.values()) {
                List<TradeDto> timeframedTrades = entry.getValue().stream().filter(item -> item.getTimeFrame() == timeframe).toList();
                double timeframedTotalProfit = timeframedTrades.stream().mapToDouble(TradeDto::getProfit).sum();
                radarChartItem.addData(DisplayUtils.roundAmountToDouble(timeframedTotalProfit));
            }

            thisMonthRadarChartData.add(radarChartItem);
        }

        thisMonthRadarChart.setList(thisMonthRadarChartData);


        response.setTodayRadarChart(todaysRadarChart);
        response.setThisWeekRadarChart(thisWeekRadarChart);
        response.setThisMonthRadarChart(thisMonthRadarChart);

        return response;
    }

    private GetDashboardMultiBarChartResponse getDashboardMultiBarChart(List<TradeDto> thisMonthTrades, List<TradeDto> thisWeekTrades, List<TradeDto> todaysTrades) {
        GetDashboardMultiBarChartResponse response = new GetDashboardMultiBarChartResponse();
        List<StrategyEntity> strategyList = strategyRepository.findAll();

        List<String> strategyNames = strategyList.stream().map(StrategyEntity::getDescription).toList();

        /*
         * TODAY
         */
        GetMultiBarChartResponse todayMultiBarChart = new GetMultiBarChartResponse();
        todayMultiBarChart.setLabels(strategyNames);
        MultiBarChartItem todayWinsChart = new MultiBarChartItem();
        todayWinsChart.setChartColor("#2ecb18");
        todayWinsChart.setChartName(OutcomeResultEnum.WIN.getLabel());

        MultiBarChartItem todayLosesChart = new MultiBarChartItem();
        todayLosesChart.setChartColor("#cb3818");
        todayLosesChart.setChartName(OutcomeResultEnum.LOSE.getLabel());

        for (StrategyEntity strategyEntity : strategyList) {
            List<TradeDto> strategyTrades = todaysTrades.stream().filter(trade -> trade.getStrategyId() == strategyEntity.getId()).toList();
            Double winsData = strategyTrades.stream().filter(item -> item.getProfit() > 0).mapToDouble(TradeDto::getProfit).sum();
            Double losesData = strategyTrades.stream().filter(item -> item.getProfit() < 0).mapToDouble(TradeDto::getProfit).sum();
            todayWinsChart.addDataItem(DisplayUtils.roundAmountToDouble(winsData));
            todayLosesChart.addDataItem(DisplayUtils.roundAmountToDouble(Math.abs(losesData)));
        }

        todayMultiBarChart.setList(List.of(todayWinsChart, todayLosesChart));

        /*
         * THIS WEEK
         */
        GetMultiBarChartResponse thisWeekMultiBarChart = new GetMultiBarChartResponse();
        thisWeekMultiBarChart.setLabels(strategyNames);

        MultiBarChartItem thisWeekWinsChart = new MultiBarChartItem();
        thisWeekWinsChart.setChartColor("#2ecb18");
        thisWeekWinsChart.setChartName(OutcomeResultEnum.WIN.getLabel());

        MultiBarChartItem thisWeekLosesChart = new MultiBarChartItem();
        thisWeekLosesChart.setChartColor("#cb3818");
        thisWeekLosesChart.setChartName(OutcomeResultEnum.LOSE.getLabel());

        for (StrategyEntity strategyEntity : strategyList) {
            List<TradeDto> strategyTrades = thisWeekTrades.stream().filter(trade -> trade.getStrategyId() == strategyEntity.getId()).toList();
            Double winsData = strategyTrades.stream().filter(item -> item.getProfit() > 0).mapToDouble(TradeDto::getProfit).sum();
            Double losesData = strategyTrades.stream().filter(item -> item.getProfit() < 0).mapToDouble(TradeDto::getProfit).sum();
            thisWeekWinsChart.addDataItem(DisplayUtils.roundAmountToDouble(winsData));
            thisWeekLosesChart.addDataItem(DisplayUtils.roundAmountToDouble(Math.abs(losesData)));
        }

        thisWeekMultiBarChart.setList(List.of(thisWeekWinsChart, thisWeekLosesChart));

        /*
         * THIS MONTH
         */
        GetMultiBarChartResponse thisMonthMultiBarChart = new GetMultiBarChartResponse();
        thisMonthMultiBarChart.setLabels(strategyNames);

        MultiBarChartItem thisMonthWinsChart = new MultiBarChartItem();
        thisMonthWinsChart.setChartColor("#2ecb18");
        thisMonthWinsChart.setChartName(OutcomeResultEnum.WIN.getLabel());

        MultiBarChartItem thisMonthLosesChart = new MultiBarChartItem();
        thisMonthLosesChart.setChartColor("#cb3818");
        thisMonthLosesChart.setChartName(OutcomeResultEnum.LOSE.getLabel());

        for (StrategyEntity strategyEntity : strategyList) {
            List<TradeDto> strategyTrades = thisMonthTrades.stream().filter(trade -> trade.getStrategyId() == strategyEntity.getId()).toList();
            Double winsData = strategyTrades.stream().filter(item -> item.getProfit() > 0).mapToDouble(TradeDto::getProfit).sum();
            Double losesData = strategyTrades.stream().filter(item -> item.getProfit() < 0).mapToDouble(TradeDto::getProfit).sum();
            thisMonthWinsChart.addDataItem(DisplayUtils.roundAmountToDouble(winsData));
            thisMonthLosesChart.addDataItem(DisplayUtils.roundAmountToDouble(Math.abs(losesData)));
        }

        thisMonthMultiBarChart.setList(List.of(thisMonthWinsChart, thisMonthLosesChart));


        response.setTodayMultiBarChart(todayMultiBarChart);
        response.setThisWeekMultiBarChart(thisWeekMultiBarChart);
        response.setThisMonthMultiBarChart(thisMonthMultiBarChart);

        return response;
    }

    private GetDashboardPieChartResponse getDashboardPieChart(List<TradeDto> thisMonthTrades, List<TradeDto> thisWeekTrades, List<TradeDto> todaysTrades) {
        GetDashboardPieChartResponse response = new GetDashboardPieChartResponse();

        List<StrategyEntity> strategyList = strategyRepository.findAll();

        List<String> strategyNames = strategyList.stream().map(StrategyEntity::getDescription).toList();


        /*
         * TODAY
         */
        GetPieChartResponse todayPieChart = new GetPieChartResponse();
        todayPieChart.setLabels(strategyNames);

        int i = 0;
        for (StrategyEntity strategyEntity : strategyList) {
            PieChartItem todayPieChartItem = new PieChartItem();
            todayPieChartItem.setChartColor(COLORS.get(i++));
            todayPieChartItem.setChartName(strategyEntity.getDescription());
            long count = todaysTrades.stream().filter(item -> item.getStrategyId() == strategyEntity.getId()).count();
            todayPieChartItem.setData((double) count);
            todayPieChart.addPieChart(todayPieChartItem);
        }

        /*
         * THIS WEEK
         */
        GetPieChartResponse thisWeekPieChart = new GetPieChartResponse();
        thisWeekPieChart.setLabels(strategyNames);

        int j = 0;
        for (StrategyEntity strategyEntity : strategyList) {
            PieChartItem thisWeekPieChartItem = new PieChartItem();
            thisWeekPieChartItem.setChartColor(COLORS.get(j++));
            thisWeekPieChartItem.setChartName(strategyEntity.getDescription());
            long count = thisWeekTrades.stream().filter(item -> item.getStrategyId() == strategyEntity.getId()).count();
            thisWeekPieChartItem.setData((double) count);
            thisWeekPieChart.addPieChart(thisWeekPieChartItem);
        }

        /*
         * THIS MONTH
         */
        GetPieChartResponse thisMonthPieChart = new GetPieChartResponse();
        thisMonthPieChart.setLabels(strategyNames);

        int k = 0;
        for (StrategyEntity strategyEntity : strategyList) {
            PieChartItem thisMonthPieChartItem = new PieChartItem();
            thisMonthPieChartItem.setChartColor(COLORS.get(k++));
            thisMonthPieChartItem.setChartName(strategyEntity.getDescription());
            long count = thisMonthTrades.stream().filter(item -> item.getStrategyId() == strategyEntity.getId()).count();
            thisMonthPieChartItem.setData((double) count);
            thisMonthPieChart.addPieChart(thisMonthPieChartItem);
        }

        response.setTodayPieChart(todayPieChart);
        response.setThisWeekPieChart(thisWeekPieChart);
        response.setThisMonthPieChart(thisMonthPieChart);

        return response;
    }

    private List<Double> calculateHourlyProfit(List<TradeDto> todaysTrades) {
        // Initialize a list to store profits for each hour (24 hours)
        List<Double> hourlyProfits = IntStream.range(0, 24)
                .mapToObj(i -> 0.0)
                .collect(Collectors.toList());

        // Loop through each trade and calculate its corresponding hour
        for (TradeDto trade : todaysTrades) {
            Date tradeDate = trade.getDate();

            // Convert Date to LocalDateTime to extract the hour
            LocalDateTime tradeDateTime = tradeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            int hour = tradeDateTime.getHour(); // Get the hour (0 to 23)

            // Sum the profit for the corresponding hour
            hourlyProfits.set(hour, hourlyProfits.get(hour) + trade.getProfit());
        }

        List<Double> roundedHourlyProfits = hourlyProfits.stream().map(DisplayUtils::roundAmountToDouble).toList();

        return roundedHourlyProfits;
    }

    public List<Double> calculateDailyProfit(List<TradeDto> todaysTrades) {
        // Create a map to store the profit for each day of the week
        Map<DayOfWeek, Double> dailyProfitsMap = new HashMap<>();

        // Initialize all days of the week with 0.0 profit
        for (DayOfWeek day : DayOfWeek.values()) {
            dailyProfitsMap.put(day, 0.0);
        }

        // Iterate over each trade and sum up the profits for the respective day
        for (TradeDto trade : todaysTrades) {
            Date tradeDate = trade.getDate();

            // Convert Date to LocalDateTime to extract the day of the week
            LocalDateTime tradeDateTime = tradeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            DayOfWeek dayOfWeek = tradeDateTime.getDayOfWeek();

            // Update the total profit for the corresponding day
            double currentProfit = dailyProfitsMap.get(dayOfWeek);
            dailyProfitsMap.put(dayOfWeek, currentProfit + trade.getProfit());
        }

        // Prepare the result list in the order of days from Monday to Sunday
        List<Double> dailyProfits = new ArrayList<>();
        dailyProfits.add(dailyProfitsMap.get(DayOfWeek.MONDAY));
        dailyProfits.add(dailyProfitsMap.get(DayOfWeek.TUESDAY));
        dailyProfits.add(dailyProfitsMap.get(DayOfWeek.WEDNESDAY));
        dailyProfits.add(dailyProfitsMap.get(DayOfWeek.THURSDAY));
        dailyProfits.add(dailyProfitsMap.get(DayOfWeek.FRIDAY));
        dailyProfits.add(dailyProfitsMap.get(DayOfWeek.SATURDAY));
        dailyProfits.add(dailyProfitsMap.get(DayOfWeek.SUNDAY));


        List<Double> roundedDailyProfits = dailyProfits.stream().map(DisplayUtils::roundAmountToDouble).toList();

        return roundedDailyProfits;
    }

    public List<Double> calculateWeeklyProfit(List<TradeDto> todaysTrades) {
        // Create a map to store the profit for each week (1-4 or 1-5)
        Map<Integer, Double> weeklyProfitsMap = new HashMap<>();

        // Initialize profits for all weeks to 0.0
        for (int i = 1; i <= 5; i++) {
            weeklyProfitsMap.put(i, 0.0);
        }

        // Iterate over each trade and sum up the profits for the respective week
        for (TradeDto trade : todaysTrades) {
            Date tradeDate = trade.getDate();

            // Convert Date to LocalDateTime to extract the week of the month
            LocalDateTime tradeDateTime = tradeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            int weekOfMonth = tradeDateTime.get(ChronoField.ALIGNED_WEEK_OF_MONTH);

            // Update the total profit for the corresponding week
            double currentProfit = weeklyProfitsMap.getOrDefault(weekOfMonth, 0.0);
            weeklyProfitsMap.put(weekOfMonth, currentProfit + trade.getProfit());
        }

        // Prepare the result list in the order of weeks (Week 1 to Week 4/5)
        List<Double> weeklyProfits = new ArrayList<>();
        weeklyProfits.add(weeklyProfitsMap.get(1)); // Week 1
        weeklyProfits.add(weeklyProfitsMap.get(2)); // Week 2
        weeklyProfits.add(weeklyProfitsMap.get(3)); // Week 3
        weeklyProfits.add(weeklyProfitsMap.get(4)); // Week 4
        // Some months may have a fifth week, so we check for it
        if (weeklyProfitsMap.get(5) != 0.0) {
            weeklyProfits.add(weeklyProfitsMap.get(5)); // Week 5 (if any trades fall in this week)
        }

        List<Double> roundedWeeklyProfits = weeklyProfits.stream().map(DisplayUtils::roundAmountToDouble).toList();

        return roundedWeeklyProfits;
    }
}
