package com.fantasy.tracker2.service;

import com.fantasy.tracker2.domain.Futbolista;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Futbolista}.
 */
public interface FutbolistaService {

    /**
     * Save a futbolista.
     *
     * @param futbolista the entity to save.
     * @return the persisted entity.
     */
    Futbolista save(Futbolista futbolista);

    /**
     * Get all the futbolistas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Futbolista> findAll(Pageable pageable);


    /**
     * Get the "id" futbolista.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Futbolista> findOne(Long id);

    /**
     * Delete the "id" futbolista.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the futbolista corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Futbolista> search(String query, Pageable pageable);
}
