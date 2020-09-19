package com.fantasy.tracker2.web.rest;

import com.fantasy.tracker2.FantasyTrackerApp;
import com.fantasy.tracker2.domain.Operacion;
import com.fantasy.tracker2.repository.OperacionRepository;
import com.fantasy.tracker2.repository.search.OperacionSearchRepository;
import com.fantasy.tracker2.service.OperacionService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fantasy.tracker2.domain.enumeration.AccionEnum;
/**
 * Integration tests for the {@link OperacionResource} REST controller.
 */
@SpringBootTest(classes = FantasyTrackerApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class OperacionResourceIT {

    private static final Long DEFAULT_PRECIO = 1L;
    private static final Long UPDATED_PRECIO = 2L;

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final AccionEnum DEFAULT_ACCION = AccionEnum.FACHO;
    private static final AccionEnum UPDATED_ACCION = AccionEnum.COMPRA;

    @Autowired
    private OperacionRepository operacionRepository;

    @Autowired
    private OperacionService operacionService;

    /**
     * This repository is mocked in the com.fantasy.tracker2.repository.search test package.
     *
     * @see com.fantasy.tracker2.repository.search.OperacionSearchRepositoryMockConfiguration
     */
    @Autowired
    private OperacionSearchRepository mockOperacionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOperacionMockMvc;

    private Operacion operacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operacion createEntity(EntityManager em) {
        Operacion operacion = new Operacion()
            .precio(DEFAULT_PRECIO)
            .fecha(DEFAULT_FECHA)
            .accion(DEFAULT_ACCION);
        return operacion;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operacion createUpdatedEntity(EntityManager em) {
        Operacion operacion = new Operacion()
            .precio(UPDATED_PRECIO)
            .fecha(UPDATED_FECHA)
            .accion(UPDATED_ACCION);
        return operacion;
    }

    @BeforeEach
    public void initTest() {
        operacion = createEntity(em);
    }

    @Test
    @Transactional
    public void createOperacion() throws Exception {
        int databaseSizeBeforeCreate = operacionRepository.findAll().size();
        // Create the Operacion
        restOperacionMockMvc.perform(post("/api/operacions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(operacion)))
            .andExpect(status().isCreated());

        // Validate the Operacion in the database
        List<Operacion> operacionList = operacionRepository.findAll();
        assertThat(operacionList).hasSize(databaseSizeBeforeCreate + 1);
        Operacion testOperacion = operacionList.get(operacionList.size() - 1);
        assertThat(testOperacion.getPrecio()).isEqualTo(DEFAULT_PRECIO);
        assertThat(testOperacion.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testOperacion.getAccion()).isEqualTo(DEFAULT_ACCION);

        // Validate the Operacion in Elasticsearch
        verify(mockOperacionSearchRepository, times(1)).save(testOperacion);
    }

    @Test
    @Transactional
    public void createOperacionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = operacionRepository.findAll().size();

        // Create the Operacion with an existing ID
        operacion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperacionMockMvc.perform(post("/api/operacions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(operacion)))
            .andExpect(status().isBadRequest());

        // Validate the Operacion in the database
        List<Operacion> operacionList = operacionRepository.findAll();
        assertThat(operacionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Operacion in Elasticsearch
        verify(mockOperacionSearchRepository, times(0)).save(operacion);
    }


    @Test
    @Transactional
    public void checkPrecioIsRequired() throws Exception {
        int databaseSizeBeforeTest = operacionRepository.findAll().size();
        // set the field null
        operacion.setPrecio(null);

        // Create the Operacion, which fails.


        restOperacionMockMvc.perform(post("/api/operacions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(operacion)))
            .andExpect(status().isBadRequest());

        List<Operacion> operacionList = operacionRepository.findAll();
        assertThat(operacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = operacionRepository.findAll().size();
        // set the field null
        operacion.setFecha(null);

        // Create the Operacion, which fails.


        restOperacionMockMvc.perform(post("/api/operacions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(operacion)))
            .andExpect(status().isBadRequest());

        List<Operacion> operacionList = operacionRepository.findAll();
        assertThat(operacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccionIsRequired() throws Exception {
        int databaseSizeBeforeTest = operacionRepository.findAll().size();
        // set the field null
        operacion.setAccion(null);

        // Create the Operacion, which fails.


        restOperacionMockMvc.perform(post("/api/operacions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(operacion)))
            .andExpect(status().isBadRequest());

        List<Operacion> operacionList = operacionRepository.findAll();
        assertThat(operacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOperacions() throws Exception {
        // Initialize the database
        operacionRepository.saveAndFlush(operacion);

        // Get all the operacionList
        restOperacionMockMvc.perform(get("/api/operacions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].accion").value(hasItem(DEFAULT_ACCION.toString())));
    }
    
    @Test
    @Transactional
    public void getOperacion() throws Exception {
        // Initialize the database
        operacionRepository.saveAndFlush(operacion);

        // Get the operacion
        restOperacionMockMvc.perform(get("/api/operacions/{id}", operacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operacion.getId().intValue()))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.accion").value(DEFAULT_ACCION.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingOperacion() throws Exception {
        // Get the operacion
        restOperacionMockMvc.perform(get("/api/operacions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOperacion() throws Exception {
        // Initialize the database
        operacionService.save(operacion);

        int databaseSizeBeforeUpdate = operacionRepository.findAll().size();

        // Update the operacion
        Operacion updatedOperacion = operacionRepository.findById(operacion.getId()).get();
        // Disconnect from session so that the updates on updatedOperacion are not directly saved in db
        em.detach(updatedOperacion);
        updatedOperacion
            .precio(UPDATED_PRECIO)
            .fecha(UPDATED_FECHA)
            .accion(UPDATED_ACCION);

        restOperacionMockMvc.perform(put("/api/operacions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedOperacion)))
            .andExpect(status().isOk());

        // Validate the Operacion in the database
        List<Operacion> operacionList = operacionRepository.findAll();
        assertThat(operacionList).hasSize(databaseSizeBeforeUpdate);
        Operacion testOperacion = operacionList.get(operacionList.size() - 1);
        assertThat(testOperacion.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testOperacion.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testOperacion.getAccion()).isEqualTo(UPDATED_ACCION);

        // Validate the Operacion in Elasticsearch
        verify(mockOperacionSearchRepository, times(2)).save(testOperacion);
    }

    @Test
    @Transactional
    public void updateNonExistingOperacion() throws Exception {
        int databaseSizeBeforeUpdate = operacionRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperacionMockMvc.perform(put("/api/operacions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(operacion)))
            .andExpect(status().isBadRequest());

        // Validate the Operacion in the database
        List<Operacion> operacionList = operacionRepository.findAll();
        assertThat(operacionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Operacion in Elasticsearch
        verify(mockOperacionSearchRepository, times(0)).save(operacion);
    }

    @Test
    @Transactional
    public void deleteOperacion() throws Exception {
        // Initialize the database
        operacionService.save(operacion);

        int databaseSizeBeforeDelete = operacionRepository.findAll().size();

        // Delete the operacion
        restOperacionMockMvc.perform(delete("/api/operacions/{id}", operacion.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Operacion> operacionList = operacionRepository.findAll();
        assertThat(operacionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Operacion in Elasticsearch
        verify(mockOperacionSearchRepository, times(1)).deleteById(operacion.getId());
    }

    @Test
    @Transactional
    public void searchOperacion() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        operacionService.save(operacion);
        when(mockOperacionSearchRepository.search(queryStringQuery("id:" + operacion.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(operacion), PageRequest.of(0, 1), 1));

        // Search the operacion
        restOperacionMockMvc.perform(get("/api/_search/operacions?query=id:" + operacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].accion").value(hasItem(DEFAULT_ACCION.toString())));
    }
}
