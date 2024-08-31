package com.amdose.database.repositories;

import com.amdose.database.entities.ActionEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Alaa Jawhar
 */
public interface IActionRepository extends CrudRepository<ActionEntity, Long> {
}
