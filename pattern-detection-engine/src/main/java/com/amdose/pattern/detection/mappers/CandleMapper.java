package com.amdose.pattern.detection.mappers;

import com.amdose.pattern.detection.dtos.CandleItemDTO;
import com.amdose.database.entities.CandleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Mapper
public interface CandleMapper {

    CandleMapper INSTANCE = Mappers.getMapper(CandleMapper.class);

    CandleItemDTO candleEntityToCandleItemDTO(CandleEntity candleEntity);

    List<CandleItemDTO> candleEntitiesToCandleItemDTOs(List<CandleEntity> candleEntities);
}
