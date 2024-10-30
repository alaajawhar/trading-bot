package com.amdose.pattern.detection.dtos.detectors;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
@AllArgsConstructor
public class MWResult {
    private Integer highPeakIndex1;
    private Integer highPeakIndex2;
    private Integer lowPeakIndex1;
    private Integer lowPeakIndex2;
    private Integer lowPeakIndex3;
}