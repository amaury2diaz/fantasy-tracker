package com.fantasy.tracker2.service.impl;

import com.fantasy.tracker2.service.FutbolistaService;
import com.fantasy.tracker2.domain.Futbolista;
import com.fantasy.tracker2.repository.FutbolistaRepository;
import com.fantasy.tracker2.repository.search.FutbolistaSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Futbolista}.
 */
@Service
@Transactional
public class FutbolistaServiceImpl implements FutbolistaService {

    private final Logger log = LoggerFactory.getLogger(FutbolistaServiceImpl.class);

    private final FutbolistaRepository futbolistaRepository;

    private final FutbolistaSearchRepository futbolistaSearchRepository;

    public FutbolistaServiceImpl(FutbolistaRepository futbolistaRepository, FutbolistaSearchRepository futbolistaSearchRepository) {
        this.futbolistaRepository = futbolistaRepository;
        this.futbolistaSearchRepository = futbolistaSearchRepository;
    }

    @Override
    public Futbolista save(Futbolista futbolista) {
        log.debug("Request to save Futbolista : {}", futbolista);
        Futbolista result = futbolistaRepository.save(futbolista);
        futbolistaSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Futbolista> findAll(Pageable pageable) {
        log.debug("Request to get all Futbolistas");
        return futbolistaRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Futbolista> findOne(Long id) {
        log.debug("Request to get Futbolista : {}", id);
        return futbolistaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Futbolista : {}", id);
        futbolistaRepository.deleteById(id);
        futbolistaSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Futbolista> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Futbolistas for query {}", query);
        return futbolistaSearchRepository.search(queryStringQuery(query), pageable);    }
}
