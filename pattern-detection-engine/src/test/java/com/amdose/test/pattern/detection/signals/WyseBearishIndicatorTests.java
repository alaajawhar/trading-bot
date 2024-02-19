package com.amdose.test.pattern.detection.signals;

import com.amdose.database.entities.CandleEntity;
import com.amdose.database.entities.SignalEntity;
import com.amdose.database.repositories.ICandleRepository;
import com.amdose.pattern.detection.PatternDetectionModule;
import com.amdose.pattern.detection.dtos.CandleItemDTO;
import com.amdose.pattern.detection.mappers.CandleMapper;
import com.amdose.pattern.detection.services.signals.WyseBearishIndicatorService;
import com.amdose.pattern.detection.services.ta.Taj4JImpl;
import com.amdose.pattern.detection.services.ta.TechnicalAnalysisBaseService;
import com.amdose.pattern.detection.utils.DateUtils;
import com.amdose.pattern.detection.utils.JsonUtils;
import com.amdose.test.constants.TestConstants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@SpringBootTest(classes = {PatternDetectionModule.class})
public class WyseBearishIndicatorTests {

    @Autowired
    private ICandleRepository candleRepository;

    @Test
    @DisplayName("It should detect and confirm bearish market at the index 1")
    @Sql(scripts = {TestConstants.BEARISH_CONFIRMED_DATA_AT_INDEX_1}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void firstIndexBearishDetection() {
        String settingDate = "19-02-2024 09:35:01";

        log.debug("Setting date to [{}]", settingDate);
        this.setDateTo(settingDate);

        log.debug("fetching data...");
        List<CandleItemDTO> candleItemDTOS = findAllCandlesBeforeNow();

        WyseBearishIndicatorService wyseBearishIndicatorService = new WyseBearishIndicatorService();
        List<SignalEntity> resultSignals = wyseBearishIndicatorService.apply(candleItemDTOS);
        log.debug("Result: [" + JsonUtils.convertToString(resultSignals) + "]");
    }

    @Test
    @DisplayName("It should detect and confirm bearish market at the index 2")
    @Sql(scripts = {TestConstants.BEARISH_CONFIRMED_DATA_AT_INDEX_2}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void secondIndexBearishDetection() {
        String settingDate = "17-02-2024 12:36:00";

        log.debug("Setting date to [{}]", settingDate);
        this.setDateTo(settingDate);

        log.debug("fetching data...");
        List<CandleItemDTO> candleItemDTOS = findAllCandlesBeforeNow();

        WyseBearishIndicatorService wyseBearishIndicatorService = new WyseBearishIndicatorService();
        List<SignalEntity> apply = wyseBearishIndicatorService.apply(candleItemDTOS);
        log.debug("Result: [" + JsonUtils.convertToString(apply) + "]");
    }

    @Test
    @DisplayName("It should detect and confirm bearish market at the index 3")
    @Sql(scripts = {TestConstants.BEARISH_CONFIRMED_DATA_AT_INDEX_3}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void thirdIndexBearishDetection() {
        log.debug("sd");
    }

    @Test
    @DisplayName("It should detect and confirm bearish market at the index 4")
    @Sql(scripts = {TestConstants.BEARISH_CONFIRMED_DATA_AT_INDEX_4}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void forthIndexBearishDetection() {
        String settingDate = "19-02-2024 12:48:17";

        log.debug("Setting date to [{}]", settingDate);
        this.setDateTo(settingDate);

        log.debug("fetching data...");
        List<CandleItemDTO> candleItemDTOS = findAllCandlesBeforeNow();

        WyseBearishIndicatorService wyseBearishIndicatorService = new WyseBearishIndicatorService();
        List<SignalEntity> apply = wyseBearishIndicatorService.apply(candleItemDTOS);
        log.debug("Result: [" + JsonUtils.convertToString(apply) + "]");
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
