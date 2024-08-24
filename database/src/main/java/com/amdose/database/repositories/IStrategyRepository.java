package com.amdose.database.repositories;

import com.amdose.database.entities.StrategyEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
public interface IStrategyRepository extends CrudRepository<StrategyEntity, Long> {
    List<StrategyEntity> findAll();
}
