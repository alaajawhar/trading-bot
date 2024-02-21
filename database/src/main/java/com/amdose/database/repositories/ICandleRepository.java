package com.amdose.database.repositories;

import com.amdose.database.entities.CandleEntity;
import com.amdose.database.enums.TimeFrameEnum;
import org.springframework.data.repository.CrudRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Alaa Jawhar
 */
public interface ICandleRepository extends CrudRepository<CandleEntity, Long> {

    List<CandleEntity> findAllByOrderByDateAsc();

    List<CandleEntity> findAllByDateBeforeOrderByDateAsc(Date date);

    List<CandleEntity> findAllByTimeFrameOrderByDateAsc(TimeFrameEnum timeFrame);

    List<CandleEntity> findAllBySymbolAndTimeFrameOrderByDateAsc(String symbol, TimeFrameEnum timeFrame);

    List<CandleEntity> findTop1000BySymbolAndTimeFrameOrderByDateDesc(String symbol, TimeFrameEnum timeFrame);

    default List<CandleEntity> findLastBySymbolAndTimeFrameOrderByDateAsc(String symbol, TimeFrameEnum timeFrame) {
        List<CandleEntity> top100Desc = findTop1000BySymbolAndTimeFrameOrderByDateDesc(symbol, timeFrame);
        Collections.reverse(top100Desc);
        return top100Desc;
    }

    Optional<CandleEntity> findTopBySymbolAndTimeFrameOrderByDateDesc(String symbol, TimeFrameEnum timeFrame);

}
