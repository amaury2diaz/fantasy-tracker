package com.fantasy.tracker2.service.impl;

import com.fantasy.tracker2.service.LigaService;
import com.fantasy.tracker2.domain.Liga;
import com.fantasy.tracker2.repository.LigaRepository;
import com.fantasy.tracker2.repository.search.LigaSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Liga}.
 */
@Service
@Transactional
public class LigaServiceImpl implements LigaService {

    private final Logger log = LoggerFactory.getLogger(LigaServiceImpl.class);

    private final LigaRepository ligaRepository;

    private final LigaSearchRepository ligaSearchRepository;

    public LigaServiceImpl(LigaRepository ligaRepository, LigaSearchRepository ligaSearchRepository) {
        this.ligaRepository = ligaRepository;
        this.ligaSearchRepository = ligaSearchRepository;
    }

    @Override
    public Liga save(Liga liga) {
        log.debug("Request to save Liga : {}", liga);
        Liga result = ligaRepository.save(liga);
        ligaSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Liga> findAll(Pageable pageable) {
        log.debug("Request to get all Ligas");
        return ligaRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Liga> findOne(Long id) {
        log.debug("Request to get Liga : {}", id);
        return ligaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Liga : {}", id);
        ligaRepository.deleteById(id);
        ligaSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Liga> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Ligas for query {}", query);
        return ligaSearchRepository.search(queryStringQuery(query), pageable);    }
}
