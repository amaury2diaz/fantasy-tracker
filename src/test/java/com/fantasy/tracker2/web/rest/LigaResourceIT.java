package com.fantasy.tracker2.web.rest;

import com.fantasy.tracker2.FantasyTrackerApp;
import com.fantasy.tracker2.domain.Liga;
import com.fantasy.tracker2.repository.LigaRepository;
import com.fantasy.tracker2.repository.search.LigaSearchRepository;
import com.fantasy.tracker2.service.LigaService;

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
 * Integration tests for the {@link LigaResource} REST controller.
 */
@SpringBootTest(classes = FantasyTrackerApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class LigaResourceIT {

    private static final String DEFAULT_LIGA = "AAAAAAAAAA";
    private static final String UPDATED_LIGA = "BBBBBBBBBB";

    @Autowired
    private LigaRepository ligaRepository;

    @Autowired
    private LigaService ligaService;

    /**
     * This repository is mocked in the com.fantasy.tracker2.repository.search test package.
     *
     * @see com.fantasy.tracker2.repository.search.LigaSearchRepositoryMockConfiguration
     */
    @Autowired
    private LigaSearchRepository mockLigaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLigaMockMvc;

    private Liga liga;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Liga createEntity(EntityManager em) {
        Liga liga = new Liga()
            .liga(DEFAULT_LIGA);
        return liga;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Liga createUpdatedEntity(EntityManager em) {
        Liga liga = new Liga()
            .liga(UPDATED_LIGA);
        return liga;
    }

    @BeforeEach
    public void initTest() {
        liga = createEntity(em);
    }

    @Test
    @Transactional
    public void createLiga() throws Exception {
        int databaseSizeBeforeCreate = ligaRepository.findAll().size();
        // Create the Liga
        restLigaMockMvc.perform(post("/api/ligas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(liga)))
            .andExpect(status().isCreated());

        // Validate the Liga in the database
        List<Liga> ligaList = ligaRepository.findAll();
        assertThat(ligaList).hasSize(databaseSizeBeforeCreate + 1);
        Liga testLiga = ligaList.get(ligaList.size() - 1);
        assertThat(testLiga.getLiga()).isEqualTo(DEFAULT_LIGA);

        // Validate the Liga in Elasticsearch
        verify(mockLigaSearchRepository, times(1)).save(testLiga);
    }

    @Test
    @Transactional
    public void createLigaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ligaRepository.findAll().size();

        // Create the Liga with an existing ID
        liga.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLigaMockMvc.perform(post("/api/ligas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(liga)))
            .andExpect(status().isBadRequest());

        // Validate the Liga in the database
        List<Liga> ligaList = ligaRepository.findAll();
        assertThat(ligaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Liga in Elasticsearch
        verify(mockLigaSearchRepository, times(0)).save(liga);
    }


    @Test
    @Transactional
    public void checkLigaIsRequired() throws Exception {
        int databaseSizeBeforeTest = ligaRepository.findAll().size();
        // set the field null
        liga.setLiga(null);

        // Create the Liga, which fails.


        restLigaMockMvc.perform(post("/api/ligas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(liga)))
            .andExpect(status().isBadRequest());

        List<Liga> ligaList = ligaRepository.findAll();
        assertThat(ligaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLigas() throws Exception {
        // Initialize the database
        ligaRepository.saveAndFlush(liga);

        // Get all the ligaList
        restLigaMockMvc.perform(get("/api/ligas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(liga.getId().intValue())))
            .andExpect(jsonPath("$.[*].liga").value(hasItem(DEFAULT_LIGA)));
    }
    
    @Test
    @Transactional
    public void getLiga() throws Exception {
        // Initialize the database
        ligaRepository.saveAndFlush(liga);

        // Get the liga
        restLigaMockMvc.perform(get("/api/ligas/{id}", liga.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(liga.getId().intValue()))
            .andExpect(jsonPath("$.liga").value(DEFAULT_LIGA));
    }
    @Test
    @Transactional
    public void getNonExistingLiga() throws Exception {
        // Get the liga
        restLigaMockMvc.perform(get("/api/ligas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLiga() throws Exception {
        // Initialize the database
        ligaService.save(liga);

        int databaseSizeBeforeUpdate = ligaRepository.findAll().size();

        // Update the liga
        Liga updatedLiga = ligaRepository.findById(liga.getId()).get();
        // Disconnect from session so that the updates on updatedLiga are not directly saved in db
        em.detach(updatedLiga);
        updatedLiga
            .liga(UPDATED_LIGA);

        restLigaMockMvc.perform(put("/api/ligas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedLiga)))
            .andExpect(status().isOk());

        // Validate the Liga in the database
        List<Liga> ligaList = ligaRepository.findAll();
        assertThat(ligaList).hasSize(databaseSizeBeforeUpdate);
        Liga testLiga = ligaList.get(ligaList.size() - 1);
        assertThat(testLiga.getLiga()).isEqualTo(UPDATED_LIGA);

        // Validate the Liga in Elasticsearch
        verify(mockLigaSearchRepository, times(2)).save(testLiga);
    }

    @Test
    @Transactional
    public void updateNonExistingLiga() throws Exception {
        int databaseSizeBeforeUpdate = ligaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLigaMockMvc.perform(put("/api/ligas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(liga)))
            .andExpect(status().isBadRequest());

        // Validate the Liga in the database
        List<Liga> ligaList = ligaRepository.findAll();
        assertThat(ligaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Liga in Elasticsearch
        verify(mockLigaSearchRepository, times(0)).save(liga);
    }

    @Test
    @Transactional
    public void deleteLiga() throws Exception {
        // Initialize the database
        ligaService.save(liga);

        int databaseSizeBeforeDelete = ligaRepository.findAll().size();

        // Delete the liga
        restLigaMockMvc.perform(delete("/api/ligas/{id}", liga.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Liga> ligaList = ligaRepository.findAll();
        assertThat(ligaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Liga in Elasticsearch
        verify(mockLigaSearchRepository, times(1)).deleteById(liga.getId());
    }

    @Test
    @Transactional
    public void searchLiga() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        ligaService.save(liga);
        when(mockLigaSearchRepository.search(queryStringQuery("id:" + liga.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(liga), PageRequest.of(0, 1), 1));

        // Search the liga
        restLigaMockMvc.perform(get("/api/_search/ligas?query=id:" + liga.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(liga.getId().intValue())))
            .andExpect(jsonPath("$.[*].liga").value(hasItem(DEFAULT_LIGA)));
    }
}
