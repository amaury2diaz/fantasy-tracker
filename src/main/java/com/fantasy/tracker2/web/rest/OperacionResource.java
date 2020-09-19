package com.fantasy.tracker2.web.rest;

import com.fantasy.tracker2.domain.Operacion;
import com.fantasy.tracker2.service.OperacionService;
import com.fantasy.tracker2.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.fantasy.tracker2.domain.Operacion}.
 */
@RestController
@RequestMapping("/api")
public class OperacionResource {

    private final Logger log = LoggerFactory.getLogger(OperacionResource.class);

    private static final String ENTITY_NAME = "operacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OperacionService operacionService;

    public OperacionResource(OperacionService operacionService) {
        this.operacionService = operacionService;
    }

    /**
     * {@code POST  /operacions} : Create a new operacion.
     *
     * @param operacion the operacion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new operacion, or with status {@code 400 (Bad Request)} if the operacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/operacions")
    public ResponseEntity<Operacion> createOperacion(@Valid @RequestBody Operacion operacion) throws URISyntaxException {
        log.debug("REST request to save Operacion : {}", operacion);
        if (operacion.getId() != null) {
            throw new BadRequestAlertException("A new operacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Operacion result = operacionService.save(operacion);
        return ResponseEntity.created(new URI("/api/operacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /operacions} : Updates an existing operacion.
     *
     * @param operacion the operacion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operacion,
     * or with status {@code 400 (Bad Request)} if the operacion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the operacion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/operacions")
    public ResponseEntity<Operacion> updateOperacion(@Valid @RequestBody Operacion operacion) throws URISyntaxException {
        log.debug("REST request to update Operacion : {}", operacion);
        if (operacion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Operacion result = operacionService.save(operacion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, operacion.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /operacions} : get all the operacions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of operacions in body.
     */
    @GetMapping("/operacions")
    public ResponseEntity<List<Operacion>> getAllOperacions(Pageable pageable) {
        log.debug("REST request to get a page of Operacions");
        Page<Operacion> page = operacionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /operacions/:id} : get the "id" operacion.
     *
     * @param id the id of the operacion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the operacion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/operacions/{id}")
    public ResponseEntity<Operacion> getOperacion(@PathVariable Long id) {
        log.debug("REST request to get Operacion : {}", id);
        Optional<Operacion> operacion = operacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(operacion);
    }

    /**
     * {@code DELETE  /operacions/:id} : delete the "id" operacion.
     *
     * @param id the id of the operacion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/operacions/{id}")
    public ResponseEntity<Void> deleteOperacion(@PathVariable Long id) {
        log.debug("REST request to delete Operacion : {}", id);
        operacionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/operacions?query=:query} : search for the operacion corresponding
     * to the query.
     *
     * @param query the query of the operacion search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/operacions")
    public ResponseEntity<List<Operacion>> searchOperacions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Operacions for query {}", query);
        Page<Operacion> page = operacionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
