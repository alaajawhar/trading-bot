package com.amdose.database.repositories;

import com.amdose.database.entities.CandleEntity;
import com.amdose.database.enums.TimeFrameEnum;
import org.springframework.data.repository.CrudRepository;

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

    default List<CandleEntity> findLastBySymbolAndTimeFrameOrderByDateAsc(String symbol, TimeFrameEnum timeFrame) {
        List<CandleEntity> result = this.findAllBySymbolAndTimeFrameOrderByDateAsc(symbol, timeFrame);

        if (result.size() <= 1000) {
            return result;
        }

        return result.subList(result.size() - 1000, result.size());
    }

    Optional<CandleEntity> findTopBySymbolAndTimeFrameOrderByDateDesc(String symbol, TimeFrameEnum timeFrame);

}
