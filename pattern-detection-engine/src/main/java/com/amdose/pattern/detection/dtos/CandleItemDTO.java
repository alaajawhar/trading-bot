package com.amdose.pattern.detection.dtos;

import com.amdose.database.entities.CandleEntity;
import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class CandleItemDTO extends CandleEntity {
    private double rsiValue;
}
