package com.amdose.database.repositories;

import com.amdose.database.entities.SymbolEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
public interface ISymbolRepository extends CrudRepository<SymbolEntity, Long> {
    List<SymbolEntity> findAll();
}
