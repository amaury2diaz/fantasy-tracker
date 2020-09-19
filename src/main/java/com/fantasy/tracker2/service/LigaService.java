package com.fantasy.tracker2.service;

import com.fantasy.tracker2.domain.Liga;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Liga}.
 */
public interface LigaService {

    /**
     * Save a liga.
     *
     * @param liga the entity to save.
     * @return the persisted entity.
     */
    Liga save(Liga liga);

    /**
     * Get all the ligas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Liga> findAll(Pageable pageable);


    /**
     * Get the "id" liga.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Liga> findOne(Long id);

    /**
     * Delete the "id" liga.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the liga corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Liga> search(String query, Pageable pageable);
}
