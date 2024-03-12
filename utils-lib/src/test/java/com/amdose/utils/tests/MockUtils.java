package com.amdose.utils.tests;

import com.amdose.utils.DateUtils;
import lombok.experimental.UtilityClass;
import org.mockito.Mockito;

import java.util.Date;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * @author Alaa Jawhar
 */
@UtilityClass
public class MockUtils {

    public void setDateTo(String dateStr) {
        Date mockDate = DateUtils.convertToDate(dateStr);
        mockStatic(DateUtils.class, Mockito.CALLS_REAL_METHODS);
        when(DateUtils.getNow()).thenReturn(mockDate);
    }
}
