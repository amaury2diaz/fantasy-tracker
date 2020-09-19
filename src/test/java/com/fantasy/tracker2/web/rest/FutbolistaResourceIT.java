package com.fantasy.tracker2.web.rest;

import com.fantasy.tracker2.FantasyTrackerApp;
import com.fantasy.tracker2.domain.Futbolista;
import com.fantasy.tracker2.repository.FutbolistaRepository;
import com.fantasy.tracker2.repository.search.FutbolistaSearchRepository;
import com.fantasy.tracker2.service.FutbolistaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FutbolistaResource} REST controller.
 */
@SpringBootTest(classes = FantasyTrackerApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class FutbolistaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private FutbolistaRepository futbolistaRepository;

    @Autowired
    private FutbolistaService futbolistaService;

    /**
     * This repository is mocked in the com.fantasy.tracker2.repository.search test package.
     *
     * @see com.fantasy.tracker2.repository.search.FutbolistaSearchRepositoryMockConfiguration
     */
    @Autowired
    private FutbolistaSearchRepository mockFutbolistaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFutbolistaMockMvc;

    private Futbolista futbolista;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Futbolista createEntity(EntityManager em) {
        Futbolista futbolista = new Futbolista()
            .nombre(DEFAULT_NOMBRE);
        return futbolista;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Futbolista createUpdatedEntity(EntityManager em) {
        Futbolista futbolista = new Futbolista()
            .nombre(UPDATED_NOMBRE);
        return futbolista;
    }

    @BeforeEach
    public void initTest() {
        futbolista = createEntity(em);
    }

    @Test
    @Transactional
    public void createFutbolista() throws Exception {
        int databaseSizeBeforeCreate = futbolistaRepository.findAll().size();
        // Create the Futbolista
        restFutbolistaMockMvc.perform(post("/api/futbolistas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(futbolista)))
            .andExpect(status().isCreated());

        // Validate the Futbolista in the database
        List<Futbolista> futbolistaList = futbolistaRepository.findAll();
        assertThat(futbolistaList).hasSize(databaseSizeBeforeCreate + 1);
        Futbolista testFutbolista = futbolistaList.get(futbolistaList.size() - 1);
        assertThat(testFutbolista.getNombre()).isEqualTo(DEFAULT_NOMBRE);

        // Validate the Futbolista in Elasticsearch
        verify(mockFutbolistaSearchRepository, times(1)).save(testFutbolista);
    }

    @Test
    @Transactional
    public void createFutbolistaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = futbolistaRepository.findAll().size();

        // Create the Futbolista with an existing ID
        futbolista.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFutbolistaMockMvc.perform(post("/api/futbolistas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(futbolista)))
            .andExpect(status().isBadRequest());

        // Validate the Futbolista in the database
        List<Futbolista> futbolistaList = futbolistaRepository.findAll();
        assertThat(futbolistaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Futbolista in Elasticsearch
        verify(mockFutbolistaSearchRepository, times(0)).save(futbolista);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = futbolistaRepository.findAll().size();
        // set the field null
        futbolista.setNombre(null);

        // Create the Futbolista, which fails.


        restFutbolistaMockMvc.perform(post("/api/futbolistas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(futbolista)))
            .andExpect(status().isBadRequest());

        List<Futbolista> futbolistaList = futbolistaRepository.findAll();
        assertThat(futbolistaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFutbolistas() throws Exception {
        // Initialize the database
        futbolistaRepository.saveAndFlush(futbolista);

        // Get all the futbolistaList
        restFutbolistaMockMvc.perform(get("/api/futbolistas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(futbolista.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }
    
    @Test
    @Transactional
    public void getFutbolista() throws Exception {
        // Initialize the database
        futbolistaRepository.saveAndFlush(futbolista);

        // Get the futbolista
        restFutbolistaMockMvc.perform(get("/api/futbolistas/{id}", futbolista.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(futbolista.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }
    @Test
    @Transactional
    public void getNonExistingFutbolista() throws Exception {
        // Get the futbolista
        restFutbolistaMockMvc.perform(get("/api/futbolistas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFutbolista() throws Exception {
        // Initialize the database
        futbolistaService.save(futbolista);

        int databaseSizeBeforeUpdate = futbolistaRepository.findAll().size();

        // Update the futbolista
        Futbolista updatedFutbolista = futbolistaRepository.findById(futbolista.getId()).get();
        // Disconnect from session so that the updates on updatedFutbolista are not directly saved in db
        em.detach(updatedFutbolista);
        updatedFutbolista
            .nombre(UPDATED_NOMBRE);

        restFutbolistaMockMvc.perform(put("/api/futbolistas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFutbolista)))
            .andExpect(status().isOk());

        // Validate the Futbolista in the database
        List<Futbolista> futbolistaList = futbolistaRepository.findAll();
        assertThat(futbolistaList).hasSize(databaseSizeBeforeUpdate);
        Futbolista testFutbolista = futbolistaList.get(futbolistaList.size() - 1);
        assertThat(testFutbolista.getNombre()).isEqualTo(UPDATED_NOMBRE);

        // Validate the Futbolista in Elasticsearch
        verify(mockFutbolistaSearchRepository, times(2)).save(testFutbolista);
    }

    @Test
    @Transactional
    public void updateNonExistingFutbolista() throws Exception {
        int databaseSizeBeforeUpdate = futbolistaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFutbolistaMockMvc.perform(put("/api/futbolistas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(futbolista)))
            .andExpect(status().isBadRequest());

        // Validate the Futbolista in the database
        List<Futbolista> futbolistaList = futbolistaRepository.findAll();
        assertThat(futbolistaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Futbolista in Elasticsearch
        verify(mockFutbolistaSearchRepository, times(0)).save(futbolista);
    }

    @Test
    @Transactional
    public void deleteFutbolista() throws Exception {
        // Initialize the database
        futbolistaService.save(futbolista);

        int databaseSizeBeforeDelete = futbolistaRepository.findAll().size();

        // Delete the futbolista
        restFutbolistaMockMvc.perform(delete("/api/futbolistas/{id}", futbolista.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Futbolista> futbolistaList = futbolistaRepository.findAll();
        assertThat(futbolistaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Futbolista in Elasticsearch
        verify(mockFutbolistaSearchRepository, times(1)).deleteById(futbolista.getId());
    }

    @Test
    @Transactional
    public void searchFutbolista() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        futbolistaService.save(futbolista);
        when(mockFutbolistaSearchRepository.search(queryStringQuery("id:" + futbolista.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(futbolista), PageRequest.of(0, 1), 1));

        // Search the futbolista
        restFutbolistaMockMvc.perform(get("/api/_search/futbolistas?query=id:" + futbolista.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(futbolista.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }
}
