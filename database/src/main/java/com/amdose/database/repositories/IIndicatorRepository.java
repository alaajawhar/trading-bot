package com.amdose.database.repositories;

import com.amdose.database.entities.IndicatorEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
public interface IIndicatorRepository extends CrudRepository<IndicatorEntity, Long> {
    List<IndicatorEntity> findAll();
}
