package com.amdose.pattern.detection.dtos;

import com.amdose.database.entities.CandleEntity;
import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class CandleItemDTO extends CandleEntity {
    private boolean isThreeBlackCrows;
    private boolean isThreeWhiteSoldiers;
    private boolean isBullishEngulfing;
    private boolean isBearishEngulfing;
    private double rsiValue;
    private boolean high;
    private boolean low;
}
