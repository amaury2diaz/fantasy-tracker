package com.fantasy.tracker2.service.impl;

import com.fantasy.tracker2.service.OperacionService;
import com.fantasy.tracker2.domain.Operacion;
import com.fantasy.tracker2.repository.OperacionRepository;
import com.fantasy.tracker2.repository.search.OperacionSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Operacion}.
 */
@Service
@Transactional
public class OperacionServiceImpl implements OperacionService {

    private final Logger log = LoggerFactory.getLogger(OperacionServiceImpl.class);

    private final OperacionRepository operacionRepository;

    private final OperacionSearchRepository operacionSearchRepository;

    public OperacionServiceImpl(OperacionRepository operacionRepository, OperacionSearchRepository operacionSearchRepository) {
        this.operacionRepository = operacionRepository;
        this.operacionSearchRepository = operacionSearchRepository;
    }

    @Override
    public Operacion save(Operacion operacion) {
        log.debug("Request to save Operacion : {}", operacion);
        operacion.setFecha(Instant.now());
        Operacion result = operacionRepository.save(operacion);
        operacionSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Operacion> findAll(Pageable pageable) {
        log.debug("Request to get all Operacions");
        return operacionRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Operacion> findOne(Long id) {
        log.debug("Request to get Operacion : {}", id);
        return operacionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Operacion : {}", id);
        operacionRepository.deleteById(id);
        operacionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Operacion> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Operacions for query {}", query);
        return operacionSearchRepository.search(queryStringQuery(query), pageable);    }
}
