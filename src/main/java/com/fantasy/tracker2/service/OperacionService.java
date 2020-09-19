package com.fantasy.tracker2.service;

import com.fantasy.tracker2.domain.Operacion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Operacion}.
 */
public interface OperacionService {

    /**
     * Save a operacion.
     *
     * @param operacion the entity to save.
     * @return the persisted entity.
     */
    Operacion save(Operacion operacion);

    /**
     * Get all the operacions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Operacion> findAll(Pageable pageable);


    /**
     * Get the "id" operacion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Operacion> findOne(Long id);

    /**
     * Delete the "id" operacion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the operacion corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Operacion> search(String query, Pageable pageable);
}
