package com.amdose.database.repositories;

import com.amdose.database.entities.SignalEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * @author Alaa Jawhar
 */
public interface ISignalRepository extends CrudRepository<SignalEntity, Long> {
    List<SignalEntity> findAll();

    @Query(value = "SELECT ts.* FROM trading_signal ts LEFT JOIN trading_action ta ON ts.id = ta.trading_signal_id WHERE ta.trading_signal_id IS NULL AND ts.SCHEDULED_AT = :scheduledAtDate", nativeQuery = true)
    List<SignalEntity> findAllPendingActionsOfDate(Date scheduledAtDate);

    List<SignalEntity> findByDetectionId(String detectionId);

    List<SignalEntity> findAllByAddedDateAfterOrderByAddedDateAsc(Date addedDate);
}
