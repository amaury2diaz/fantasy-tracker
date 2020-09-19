package com.fantasy.tracker2.web.rest;

import com.fantasy.tracker2.domain.Futbolista;
import com.fantasy.tracker2.service.FutbolistaService;
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
 * REST controller for managing {@link com.fantasy.tracker2.domain.Futbolista}.
 */
@RestController
@RequestMapping("/api")
public class FutbolistaResource {

    private final Logger log = LoggerFactory.getLogger(FutbolistaResource.class);

    private static final String ENTITY_NAME = "futbolista";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FutbolistaService futbolistaService;

    public FutbolistaResource(FutbolistaService futbolistaService) {
        this.futbolistaService = futbolistaService;
    }

    /**
     * {@code POST  /futbolistas} : Create a new futbolista.
     *
     * @param futbolista the futbolista to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new futbolista, or with status {@code 400 (Bad Request)} if the futbolista has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/futbolistas")
    public ResponseEntity<Futbolista> createFutbolista(@Valid @RequestBody Futbolista futbolista) throws URISyntaxException {
        log.debug("REST request to save Futbolista : {}", futbolista);
        if (futbolista.getId() != null) {
            throw new BadRequestAlertException("A new futbolista cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Futbolista result = futbolistaService.save(futbolista);
        return ResponseEntity.created(new URI("/api/futbolistas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /futbolistas} : Updates an existing futbolista.
     *
     * @param futbolista the futbolista to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated futbolista,
     * or with status {@code 400 (Bad Request)} if the futbolista is not valid,
     * or with status {@code 500 (Internal Server Error)} if the futbolista couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/futbolistas")
    public ResponseEntity<Futbolista> updateFutbolista(@Valid @RequestBody Futbolista futbolista) throws URISyntaxException {
        log.debug("REST request to update Futbolista : {}", futbolista);
        if (futbolista.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Futbolista result = futbolistaService.save(futbolista);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, futbolista.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /futbolistas} : get all the futbolistas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of futbolistas in body.
     */
    @GetMapping("/futbolistas")
    public ResponseEntity<List<Futbolista>> getAllFutbolistas(Pageable pageable) {
        log.debug("REST request to get a page of Futbolistas");
        Page<Futbolista> page = futbolistaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /futbolistas/:id} : get the "id" futbolista.
     *
     * @param id the id of the futbolista to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the futbolista, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/futbolistas/{id}")
    public ResponseEntity<Futbolista> getFutbolista(@PathVariable Long id) {
        log.debug("REST request to get Futbolista : {}", id);
        Optional<Futbolista> futbolista = futbolistaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(futbolista);
    }

    /**
     * {@code DELETE  /futbolistas/:id} : delete the "id" futbolista.
     *
     * @param id the id of the futbolista to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/futbolistas/{id}")
    public ResponseEntity<Void> deleteFutbolista(@PathVariable Long id) {
        log.debug("REST request to delete Futbolista : {}", id);
        futbolistaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/futbolistas?query=:query} : search for the futbolista corresponding
     * to the query.
     *
     * @param query the query of the futbolista search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/futbolistas")
    public ResponseEntity<List<Futbolista>> searchFutbolistas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Futbolistas for query {}", query);
        Page<Futbolista> page = futbolistaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
