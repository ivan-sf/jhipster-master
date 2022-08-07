package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Componente;
import com.mycompany.myapp.domain.Empresa;
import com.mycompany.myapp.repository.ComponenteRepository;
import com.mycompany.myapp.service.criteria.ComponenteCriteria;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ComponenteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComponenteResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ESTADO = 1;
    private static final Integer UPDATED_ESTADO = 2;
    private static final Integer SMALLER_ESTADO = 1 - 1;

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/componentes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComponenteRepository componenteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComponenteMockMvc;

    private Componente componente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Componente createEntity(EntityManager em) {
        Componente componente = new Componente()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .estado(DEFAULT_ESTADO)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return componente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Componente createUpdatedEntity(EntityManager em) {
        Componente componente = new Componente()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .estado(UPDATED_ESTADO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);
        return componente;
    }

    @BeforeEach
    public void initTest() {
        componente = createEntity(em);
    }

    @Test
    @Transactional
    void createComponente() throws Exception {
        int databaseSizeBeforeCreate = componenteRepository.findAll().size();
        // Create the Componente
        restComponenteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(componente)))
            .andExpect(status().isCreated());

        // Validate the Componente in the database
        List<Componente> componenteList = componenteRepository.findAll();
        assertThat(componenteList).hasSize(databaseSizeBeforeCreate + 1);
        Componente testComponente = componenteList.get(componenteList.size() - 1);
        assertThat(testComponente.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testComponente.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testComponente.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testComponente.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createComponenteWithExistingId() throws Exception {
        // Create the Componente with an existing ID
        componente.setId(1L);

        int databaseSizeBeforeCreate = componenteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComponenteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(componente)))
            .andExpect(status().isBadRequest());

        // Validate the Componente in the database
        List<Componente> componenteList = componenteRepository.findAll();
        assertThat(componenteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllComponentes() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        // Get all the componenteList
        restComponenteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(componente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @Test
    @Transactional
    void getComponente() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        // Get the componente
        restComponenteMockMvc
            .perform(get(ENTITY_API_URL_ID, componente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(componente.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getComponentesByIdFiltering() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        Long id = componente.getId();

        defaultComponenteShouldBeFound("id.equals=" + id);
        defaultComponenteShouldNotBeFound("id.notEquals=" + id);

        defaultComponenteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultComponenteShouldNotBeFound("id.greaterThan=" + id);

        defaultComponenteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultComponenteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllComponentesByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        // Get all the componenteList where nombre equals to DEFAULT_NOMBRE
        defaultComponenteShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the componenteList where nombre equals to UPDATED_NOMBRE
        defaultComponenteShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllComponentesByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        // Get all the componenteList where nombre not equals to DEFAULT_NOMBRE
        defaultComponenteShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the componenteList where nombre not equals to UPDATED_NOMBRE
        defaultComponenteShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllComponentesByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        // Get all the componenteList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultComponenteShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the componenteList where nombre equals to UPDATED_NOMBRE
        defaultComponenteShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllComponentesByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        // Get all the componenteList where nombre is not null
        defaultComponenteShouldBeFound("nombre.specified=true");

        // Get all the componenteList where nombre is null
        defaultComponenteShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllComponentesByNombreContainsSomething() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        // Get all the componenteList where nombre contains DEFAULT_NOMBRE
        defaultComponenteShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the componenteList where nombre contains UPDATED_NOMBRE
        defaultComponenteShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllComponentesByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        // Get all the componenteList where nombre does not contain DEFAULT_NOMBRE
        defaultComponenteShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the componenteList where nombre does not contain UPDATED_NOMBRE
        defaultComponenteShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllComponentesByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        // Get all the componenteList where estado equals to DEFAULT_ESTADO
        defaultComponenteShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the componenteList where estado equals to UPDATED_ESTADO
        defaultComponenteShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllComponentesByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        // Get all the componenteList where estado not equals to DEFAULT_ESTADO
        defaultComponenteShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the componenteList where estado not equals to UPDATED_ESTADO
        defaultComponenteShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllComponentesByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        // Get all the componenteList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultComponenteShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the componenteList where estado equals to UPDATED_ESTADO
        defaultComponenteShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllComponentesByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        // Get all the componenteList where estado is not null
        defaultComponenteShouldBeFound("estado.specified=true");

        // Get all the componenteList where estado is null
        defaultComponenteShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllComponentesByEstadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        // Get all the componenteList where estado is greater than or equal to DEFAULT_ESTADO
        defaultComponenteShouldBeFound("estado.greaterThanOrEqual=" + DEFAULT_ESTADO);

        // Get all the componenteList where estado is greater than or equal to UPDATED_ESTADO
        defaultComponenteShouldNotBeFound("estado.greaterThanOrEqual=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllComponentesByEstadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        // Get all the componenteList where estado is less than or equal to DEFAULT_ESTADO
        defaultComponenteShouldBeFound("estado.lessThanOrEqual=" + DEFAULT_ESTADO);

        // Get all the componenteList where estado is less than or equal to SMALLER_ESTADO
        defaultComponenteShouldNotBeFound("estado.lessThanOrEqual=" + SMALLER_ESTADO);
    }

    @Test
    @Transactional
    void getAllComponentesByEstadoIsLessThanSomething() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        // Get all the componenteList where estado is less than DEFAULT_ESTADO
        defaultComponenteShouldNotBeFound("estado.lessThan=" + DEFAULT_ESTADO);

        // Get all the componenteList where estado is less than UPDATED_ESTADO
        defaultComponenteShouldBeFound("estado.lessThan=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllComponentesByEstadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        // Get all the componenteList where estado is greater than DEFAULT_ESTADO
        defaultComponenteShouldNotBeFound("estado.greaterThan=" + DEFAULT_ESTADO);

        // Get all the componenteList where estado is greater than SMALLER_ESTADO
        defaultComponenteShouldBeFound("estado.greaterThan=" + SMALLER_ESTADO);
    }

    @Test
    @Transactional
    void getAllComponentesByFechaRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        // Get all the componenteList where fechaRegistro equals to DEFAULT_FECHA_REGISTRO
        defaultComponenteShouldBeFound("fechaRegistro.equals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the componenteList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultComponenteShouldNotBeFound("fechaRegistro.equals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllComponentesByFechaRegistroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        // Get all the componenteList where fechaRegistro not equals to DEFAULT_FECHA_REGISTRO
        defaultComponenteShouldNotBeFound("fechaRegistro.notEquals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the componenteList where fechaRegistro not equals to UPDATED_FECHA_REGISTRO
        defaultComponenteShouldBeFound("fechaRegistro.notEquals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllComponentesByFechaRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        // Get all the componenteList where fechaRegistro in DEFAULT_FECHA_REGISTRO or UPDATED_FECHA_REGISTRO
        defaultComponenteShouldBeFound("fechaRegistro.in=" + DEFAULT_FECHA_REGISTRO + "," + UPDATED_FECHA_REGISTRO);

        // Get all the componenteList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultComponenteShouldNotBeFound("fechaRegistro.in=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllComponentesByFechaRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        // Get all the componenteList where fechaRegistro is not null
        defaultComponenteShouldBeFound("fechaRegistro.specified=true");

        // Get all the componenteList where fechaRegistro is null
        defaultComponenteShouldNotBeFound("fechaRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllComponentesByEmpresaIsEqualToSomething() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);
        Empresa empresa;
        if (TestUtil.findAll(em, Empresa.class).isEmpty()) {
            empresa = EmpresaResourceIT.createEntity(em);
            em.persist(empresa);
            em.flush();
        } else {
            empresa = TestUtil.findAll(em, Empresa.class).get(0);
        }
        em.persist(empresa);
        em.flush();
        componente.setEmpresa(empresa);
        componenteRepository.saveAndFlush(componente);
        Long empresaId = empresa.getId();

        // Get all the componenteList where empresa equals to empresaId
        defaultComponenteShouldBeFound("empresaId.equals=" + empresaId);

        // Get all the componenteList where empresa equals to (empresaId + 1)
        defaultComponenteShouldNotBeFound("empresaId.equals=" + (empresaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultComponenteShouldBeFound(String filter) throws Exception {
        restComponenteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(componente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));

        // Check, that the count call also returns 1
        restComponenteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultComponenteShouldNotBeFound(String filter) throws Exception {
        restComponenteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restComponenteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingComponente() throws Exception {
        // Get the componente
        restComponenteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewComponente() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        int databaseSizeBeforeUpdate = componenteRepository.findAll().size();

        // Update the componente
        Componente updatedComponente = componenteRepository.findById(componente.getId()).get();
        // Disconnect from session so that the updates on updatedComponente are not directly saved in db
        em.detach(updatedComponente);
        updatedComponente
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .estado(UPDATED_ESTADO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restComponenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedComponente.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedComponente))
            )
            .andExpect(status().isOk());

        // Validate the Componente in the database
        List<Componente> componenteList = componenteRepository.findAll();
        assertThat(componenteList).hasSize(databaseSizeBeforeUpdate);
        Componente testComponente = componenteList.get(componenteList.size() - 1);
        assertThat(testComponente.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testComponente.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testComponente.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testComponente.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingComponente() throws Exception {
        int databaseSizeBeforeUpdate = componenteRepository.findAll().size();
        componente.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComponenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, componente.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(componente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Componente in the database
        List<Componente> componenteList = componenteRepository.findAll();
        assertThat(componenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComponente() throws Exception {
        int databaseSizeBeforeUpdate = componenteRepository.findAll().size();
        componente.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComponenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(componente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Componente in the database
        List<Componente> componenteList = componenteRepository.findAll();
        assertThat(componenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComponente() throws Exception {
        int databaseSizeBeforeUpdate = componenteRepository.findAll().size();
        componente.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComponenteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(componente)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Componente in the database
        List<Componente> componenteList = componenteRepository.findAll();
        assertThat(componenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComponenteWithPatch() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        int databaseSizeBeforeUpdate = componenteRepository.findAll().size();

        // Update the componente using partial update
        Componente partialUpdatedComponente = new Componente();
        partialUpdatedComponente.setId(componente.getId());

        partialUpdatedComponente.descripcion(UPDATED_DESCRIPCION).estado(UPDATED_ESTADO);

        restComponenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComponente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComponente))
            )
            .andExpect(status().isOk());

        // Validate the Componente in the database
        List<Componente> componenteList = componenteRepository.findAll();
        assertThat(componenteList).hasSize(databaseSizeBeforeUpdate);
        Componente testComponente = componenteList.get(componenteList.size() - 1);
        assertThat(testComponente.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testComponente.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testComponente.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testComponente.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdateComponenteWithPatch() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        int databaseSizeBeforeUpdate = componenteRepository.findAll().size();

        // Update the componente using partial update
        Componente partialUpdatedComponente = new Componente();
        partialUpdatedComponente.setId(componente.getId());

        partialUpdatedComponente
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .estado(UPDATED_ESTADO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restComponenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComponente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComponente))
            )
            .andExpect(status().isOk());

        // Validate the Componente in the database
        List<Componente> componenteList = componenteRepository.findAll();
        assertThat(componenteList).hasSize(databaseSizeBeforeUpdate);
        Componente testComponente = componenteList.get(componenteList.size() - 1);
        assertThat(testComponente.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testComponente.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testComponente.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testComponente.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingComponente() throws Exception {
        int databaseSizeBeforeUpdate = componenteRepository.findAll().size();
        componente.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComponenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, componente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(componente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Componente in the database
        List<Componente> componenteList = componenteRepository.findAll();
        assertThat(componenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComponente() throws Exception {
        int databaseSizeBeforeUpdate = componenteRepository.findAll().size();
        componente.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComponenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(componente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Componente in the database
        List<Componente> componenteList = componenteRepository.findAll();
        assertThat(componenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComponente() throws Exception {
        int databaseSizeBeforeUpdate = componenteRepository.findAll().size();
        componente.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComponenteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(componente))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Componente in the database
        List<Componente> componenteList = componenteRepository.findAll();
        assertThat(componenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComponente() throws Exception {
        // Initialize the database
        componenteRepository.saveAndFlush(componente);

        int databaseSizeBeforeDelete = componenteRepository.findAll().size();

        // Delete the componente
        restComponenteMockMvc
            .perform(delete(ENTITY_API_URL_ID, componente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Componente> componenteList = componenteRepository.findAll();
        assertThat(componenteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
