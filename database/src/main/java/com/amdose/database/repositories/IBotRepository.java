package com.amdose.database.repositories;

import com.amdose.database.entities.BotEntity;
import com.amdose.database.enums.BotModeEnum;
import com.amdose.database.enums.TimeFrameEnum;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
public interface IBotRepository extends CrudRepository<BotEntity, Long> {

    List<BotEntity> findByModeNotAndTimeFrame(BotModeEnum mode, TimeFrameEnum timeFrame);

    List<BotEntity> findAll();
}
