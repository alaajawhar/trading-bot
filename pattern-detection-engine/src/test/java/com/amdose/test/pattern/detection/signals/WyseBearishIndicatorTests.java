package com.amdose.test.pattern.detection.signals;

import com.amdose.database.entities.CandleEntity;
import com.amdose.database.entities.SignalEntity;
import com.amdose.database.enums.SignalActionEnum;
import com.amdose.database.repositories.ICandleRepository;
import com.amdose.pattern.detection.dtos.CandleItemDTO;
import com.amdose.pattern.detection.mappers.CandleMapper;
import com.amdose.pattern.detection.services.indicators.WyseBearishIndicatorService;
import com.amdose.pattern.detection.services.ta.Taj4JImpl;
import com.amdose.pattern.detection.services.ta.TechnicalAnalysisBaseService;
import com.amdose.utils.DateUtils;
import com.amdose.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * @author Alaa Jawhar
 */
@Slf4j
//@SpringBootTest(classes = {PatternDetectionModule.class})
public class WyseBearishIndicatorTests {

    @Autowired
    private ICandleRepository candleRepository;

    //    @Test
//    @DisplayName("It should detect and confirm bearish market at the index 1")
//    @Sql(scripts = {TestConstants.BEARISH_CONFIRMED_DATA_AT_INDEX_1}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void firstIndexBearishDetection() {
        String settingDate = "19-02-2024 09:35:01";

        log.debug("Setting date to [{}]", settingDate);
        this.setDateTo(settingDate);

        log.debug("fetching data...");
        List<CandleItemDTO> candleItemDTOS = findAllCandlesBeforeNow();

        WyseBearishIndicatorService wyseBearishIndicatorService = new WyseBearishIndicatorService();
        List<SignalEntity> resultSignals = wyseBearishIndicatorService.apply(candleItemDTOS);
        log.debug("Result: [" + JsonUtils.convertToString(resultSignals.get(0).getMetaData()) + "]");

        SignalEntity sellSignal = resultSignals.get(0);
        SignalEntity buySignal = resultSignals.get(1);

        Map<String, String> sellSignalMetaData = JsonUtils.convertToObject(sellSignal.getMetaData(), Map.class);
        Map<String, String> buySignalMetaData = JsonUtils.convertToObject(buySignal.getMetaData(), Map.class);

        assertEquals(2, resultSignals.size());
        assertEquals(SignalActionEnum.SELL, resultSignals.get(0).getAction());
        assertEquals(SignalActionEnum.BUY, resultSignals.get(1).getAction());
        assertEquals("19-02-2024 09:35:00", sellSignalMetaData.get("signalScheduledAt"));
        assertEquals(1, sellSignalMetaData.get("index"));
        assertEquals("19-02-2024 09:33:00", sellSignalMetaData.get("detectionDate"));
        assertEquals(66.84246302006297, sellSignalMetaData.get("rsiValueOnDetection"));
        assertEquals(70.68221478392115, sellSignalMetaData.get("lastRsiHigherHighValue"));
    }

    //    @Test
//    @DisplayName("It should detect and confirm bearish market at the index 2")
//    @Sql(scripts = {TestConstants.BEARISH_CONFIRMED_DATA_AT_INDEX_2}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void secondIndexBearishDetection() {
        String settingDate = "17-02-2024 12:36:00";

        log.debug("Setting date to [{}]", settingDate);
        this.setDateTo(settingDate);

        log.debug("fetching data...");
        List<CandleItemDTO> candleItemDTOS = findAllCandlesBeforeNow();

        WyseBearishIndicatorService wyseBearishIndicatorService = new WyseBearishIndicatorService();
        List<SignalEntity> resultSignals = wyseBearishIndicatorService.apply(candleItemDTOS);
        log.debug("Result: [" + JsonUtils.convertToString(resultSignals.get(0).getMetaData()) + "]");

        SignalEntity sellSignal = resultSignals.get(0);
        SignalEntity buySignal = resultSignals.get(1);

        Map<String, String> sellSignalMetaData = JsonUtils.convertToObject(sellSignal.getMetaData(), Map.class);
        Map<String, String> buySignalMetaData = JsonUtils.convertToObject(buySignal.getMetaData(), Map.class);

        assertEquals(2, resultSignals.size());
        assertEquals(SignalActionEnum.SELL, resultSignals.get(0).getAction());
        assertEquals(SignalActionEnum.BUY, resultSignals.get(1).getAction());
        assertEquals("17-02-2024 12:36:00", sellSignalMetaData.get("signalScheduledAt"));
        assertEquals(2, sellSignalMetaData.get("index"));
        assertEquals("17-02-2024 12:33:00", sellSignalMetaData.get("detectionDate"));
        assertEquals(63.14629201232742, sellSignalMetaData.get("rsiValueOnDetection"));
        assertEquals(61.47273835324635, sellSignalMetaData.get("lastRsiHigherHighValue"));

    }

    //    @Test
//    @DisplayName("It should detect and confirm bearish market at the index 3")
//    @Sql(scripts = {TestConstants.BEARISH_CONFIRMED_DATA_AT_INDEX_3}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void thirdIndexBearishDetection() {
        log.debug("sd");
    }

    //    @Test
//    @DisplayName("It should detect and confirm bearish market at the index 4")
//    @Sql(scripts = {TestConstants.BEARISH_CONFIRMED_DATA_AT_INDEX_4}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void forthIndexBearishDetection() {
        String settingDate = "19-02-2024 12:48:17";

        log.debug("Setting date to [{}]", settingDate);
        this.setDateTo(settingDate);

        log.debug("fetching data...");
        List<CandleItemDTO> candleItemDTOS = findAllCandlesBeforeNow();

        WyseBearishIndicatorService wyseBearishIndicatorService = new WyseBearishIndicatorService();
        List<SignalEntity> resultSignals = wyseBearishIndicatorService.apply(candleItemDTOS);
        log.debug("Result: [" + JsonUtils.convertToString(resultSignals.get(0).getMetaData()) + "]");

        SignalEntity sellSignal = resultSignals.get(0);
        SignalEntity buySignal = resultSignals.get(1);

        Map<String, String> sellSignalMetaData = JsonUtils.convertToObject(sellSignal.getMetaData(), Map.class);
        Map<String, String> buySignalMetaData = JsonUtils.convertToObject(buySignal.getMetaData(), Map.class);

        assertEquals(2, resultSignals.size());
        assertEquals(SignalActionEnum.SELL, resultSignals.get(0).getAction());
        assertEquals(SignalActionEnum.BUY, resultSignals.get(1).getAction());
        assertEquals(4, sellSignalMetaData.get("index"));
        assertEquals(67.54515158334439, sellSignalMetaData.get("rsiValueOnDetection"));
        assertEquals("19-02-2024 12:43:00", sellSignalMetaData.get("detectionDate"));
        assertEquals("19-02-2024 12:48:00", sellSignalMetaData.get("signalScheduledAt"));
        assertEquals(73.6264398873005, sellSignalMetaData.get("lastRsiHigherHighValue"));
    }

    private List<CandleItemDTO> findAllCandlesBeforeNow() {
        log.debug("fetching data...");
        List<CandleEntity> candleEntityList = candleRepository.findAllByDateBeforeOrderByDateAsc(DateUtils.getNow());
        List<CandleItemDTO> candleItemDTOS = CandleMapper.INSTANCE.candleEntitiesToCandleItemDTOs(candleEntityList);
        TechnicalAnalysisBaseService ta = new Taj4JImpl(candleItemDTOS);
        return ta.applyAll();
    }

    private void setDateTo(String dateStr) {
        Date mockDate = DateUtils.convertToDate(dateStr);
        mockStatic(DateUtils.class, Mockito.CALLS_REAL_METHODS);
        when(DateUtils.getNow()).thenReturn(mockDate);
    }
}
