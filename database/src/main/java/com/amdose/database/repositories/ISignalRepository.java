package com.amdose.database.repositories;

import com.amdose.database.entities.SignalEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Alaa Jawhar
 */
public interface ISignalRepository extends CrudRepository<SignalEntity, Long> {
}
