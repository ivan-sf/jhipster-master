package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Componente;
import com.mycompany.myapp.domain.Empresa;
import com.mycompany.myapp.domain.InfoLegal;
import com.mycompany.myapp.domain.Rol;
import com.mycompany.myapp.domain.Sucursal;
import com.mycompany.myapp.domain.Usuario;
import com.mycompany.myapp.repository.EmpresaRepository;
import com.mycompany.myapp.service.EmpresaService;
import com.mycompany.myapp.service.criteria.EmpresaCriteria;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmpresaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmpresaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION_GPS = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION_GPS = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CELULAR = "AAAAAAAAAA";
    private static final String UPDATED_CELULAR = "BBBBBBBBBB";

    private static final String DEFAULT_INDICATIVO = "AAAAAAAAAA";
    private static final String UPDATED_INDICATIVO = "BBBBBBBBBB";

    private static final Integer DEFAULT_ESTADO = 1;
    private static final Integer UPDATED_ESTADO = 2;
    private static final Integer SMALLER_ESTADO = 1 - 1;

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/empresas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmpresaRepository empresaRepository;

    @Mock
    private EmpresaRepository empresaRepositoryMock;

    @Mock
    private EmpresaService empresaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmpresaMockMvc;

    private Empresa empresa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empresa createEntity(EntityManager em) {
        Empresa empresa = new Empresa()
            .nombre(DEFAULT_NOMBRE)
            .direccion(DEFAULT_DIRECCION)
            .direccionGPS(DEFAULT_DIRECCION_GPS)
            .email(DEFAULT_EMAIL)
            .celular(DEFAULT_CELULAR)
            .indicativo(DEFAULT_INDICATIVO)
            .estado(DEFAULT_ESTADO)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return empresa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empresa createUpdatedEntity(EntityManager em) {
        Empresa empresa = new Empresa()
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .direccionGPS(UPDATED_DIRECCION_GPS)
            .email(UPDATED_EMAIL)
            .celular(UPDATED_CELULAR)
            .indicativo(UPDATED_INDICATIVO)
            .estado(UPDATED_ESTADO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);
        return empresa;
    }

    @BeforeEach
    public void initTest() {
        empresa = createEntity(em);
    }

    @Test
    @Transactional
    void createEmpresa() throws Exception {
        int databaseSizeBeforeCreate = empresaRepository.findAll().size();
        // Create the Empresa
        restEmpresaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(empresa)))
            .andExpect(status().isCreated());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeCreate + 1);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEmpresa.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testEmpresa.getDireccionGPS()).isEqualTo(DEFAULT_DIRECCION_GPS);
        assertThat(testEmpresa.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmpresa.getCelular()).isEqualTo(DEFAULT_CELULAR);
        assertThat(testEmpresa.getIndicativo()).isEqualTo(DEFAULT_INDICATIVO);
        assertThat(testEmpresa.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testEmpresa.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createEmpresaWithExistingId() throws Exception {
        // Create the Empresa with an existing ID
        empresa.setId(1L);

        int databaseSizeBeforeCreate = empresaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmpresaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(empresa)))
            .andExpect(status().isBadRequest());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmpresas() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList
        restEmpresaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empresa.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].direccionGPS").value(hasItem(DEFAULT_DIRECCION_GPS)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR)))
            .andExpect(jsonPath("$.[*].indicativo").value(hasItem(DEFAULT_INDICATIVO)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmpresasWithEagerRelationshipsIsEnabled() throws Exception {
        when(empresaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmpresaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(empresaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmpresasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(empresaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmpresaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(empresaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getEmpresa() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get the empresa
        restEmpresaMockMvc
            .perform(get(ENTITY_API_URL_ID, empresa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(empresa.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.direccionGPS").value(DEFAULT_DIRECCION_GPS))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.celular").value(DEFAULT_CELULAR))
            .andExpect(jsonPath("$.indicativo").value(DEFAULT_INDICATIVO))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getEmpresasByIdFiltering() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        Long id = empresa.getId();

        defaultEmpresaShouldBeFound("id.equals=" + id);
        defaultEmpresaShouldNotBeFound("id.notEquals=" + id);

        defaultEmpresaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmpresaShouldNotBeFound("id.greaterThan=" + id);

        defaultEmpresaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmpresaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmpresasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where nombre equals to DEFAULT_NOMBRE
        defaultEmpresaShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the empresaList where nombre equals to UPDATED_NOMBRE
        defaultEmpresaShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEmpresasByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where nombre not equals to DEFAULT_NOMBRE
        defaultEmpresaShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the empresaList where nombre not equals to UPDATED_NOMBRE
        defaultEmpresaShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEmpresasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultEmpresaShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the empresaList where nombre equals to UPDATED_NOMBRE
        defaultEmpresaShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEmpresasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where nombre is not null
        defaultEmpresaShouldBeFound("nombre.specified=true");

        // Get all the empresaList where nombre is null
        defaultEmpresaShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpresasByNombreContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where nombre contains DEFAULT_NOMBRE
        defaultEmpresaShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the empresaList where nombre contains UPDATED_NOMBRE
        defaultEmpresaShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEmpresasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where nombre does not contain DEFAULT_NOMBRE
        defaultEmpresaShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the empresaList where nombre does not contain UPDATED_NOMBRE
        defaultEmpresaShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEmpresasByDireccionIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where direccion equals to DEFAULT_DIRECCION
        defaultEmpresaShouldBeFound("direccion.equals=" + DEFAULT_DIRECCION);

        // Get all the empresaList where direccion equals to UPDATED_DIRECCION
        defaultEmpresaShouldNotBeFound("direccion.equals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllEmpresasByDireccionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where direccion not equals to DEFAULT_DIRECCION
        defaultEmpresaShouldNotBeFound("direccion.notEquals=" + DEFAULT_DIRECCION);

        // Get all the empresaList where direccion not equals to UPDATED_DIRECCION
        defaultEmpresaShouldBeFound("direccion.notEquals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllEmpresasByDireccionIsInShouldWork() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where direccion in DEFAULT_DIRECCION or UPDATED_DIRECCION
        defaultEmpresaShouldBeFound("direccion.in=" + DEFAULT_DIRECCION + "," + UPDATED_DIRECCION);

        // Get all the empresaList where direccion equals to UPDATED_DIRECCION
        defaultEmpresaShouldNotBeFound("direccion.in=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllEmpresasByDireccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where direccion is not null
        defaultEmpresaShouldBeFound("direccion.specified=true");

        // Get all the empresaList where direccion is null
        defaultEmpresaShouldNotBeFound("direccion.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpresasByDireccionContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where direccion contains DEFAULT_DIRECCION
        defaultEmpresaShouldBeFound("direccion.contains=" + DEFAULT_DIRECCION);

        // Get all the empresaList where direccion contains UPDATED_DIRECCION
        defaultEmpresaShouldNotBeFound("direccion.contains=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllEmpresasByDireccionNotContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where direccion does not contain DEFAULT_DIRECCION
        defaultEmpresaShouldNotBeFound("direccion.doesNotContain=" + DEFAULT_DIRECCION);

        // Get all the empresaList where direccion does not contain UPDATED_DIRECCION
        defaultEmpresaShouldBeFound("direccion.doesNotContain=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllEmpresasByDireccionGPSIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where direccionGPS equals to DEFAULT_DIRECCION_GPS
        defaultEmpresaShouldBeFound("direccionGPS.equals=" + DEFAULT_DIRECCION_GPS);

        // Get all the empresaList where direccionGPS equals to UPDATED_DIRECCION_GPS
        defaultEmpresaShouldNotBeFound("direccionGPS.equals=" + UPDATED_DIRECCION_GPS);
    }

    @Test
    @Transactional
    void getAllEmpresasByDireccionGPSIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where direccionGPS not equals to DEFAULT_DIRECCION_GPS
        defaultEmpresaShouldNotBeFound("direccionGPS.notEquals=" + DEFAULT_DIRECCION_GPS);

        // Get all the empresaList where direccionGPS not equals to UPDATED_DIRECCION_GPS
        defaultEmpresaShouldBeFound("direccionGPS.notEquals=" + UPDATED_DIRECCION_GPS);
    }

    @Test
    @Transactional
    void getAllEmpresasByDireccionGPSIsInShouldWork() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where direccionGPS in DEFAULT_DIRECCION_GPS or UPDATED_DIRECCION_GPS
        defaultEmpresaShouldBeFound("direccionGPS.in=" + DEFAULT_DIRECCION_GPS + "," + UPDATED_DIRECCION_GPS);

        // Get all the empresaList where direccionGPS equals to UPDATED_DIRECCION_GPS
        defaultEmpresaShouldNotBeFound("direccionGPS.in=" + UPDATED_DIRECCION_GPS);
    }

    @Test
    @Transactional
    void getAllEmpresasByDireccionGPSIsNullOrNotNull() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where direccionGPS is not null
        defaultEmpresaShouldBeFound("direccionGPS.specified=true");

        // Get all the empresaList where direccionGPS is null
        defaultEmpresaShouldNotBeFound("direccionGPS.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpresasByDireccionGPSContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where direccionGPS contains DEFAULT_DIRECCION_GPS
        defaultEmpresaShouldBeFound("direccionGPS.contains=" + DEFAULT_DIRECCION_GPS);

        // Get all the empresaList where direccionGPS contains UPDATED_DIRECCION_GPS
        defaultEmpresaShouldNotBeFound("direccionGPS.contains=" + UPDATED_DIRECCION_GPS);
    }

    @Test
    @Transactional
    void getAllEmpresasByDireccionGPSNotContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where direccionGPS does not contain DEFAULT_DIRECCION_GPS
        defaultEmpresaShouldNotBeFound("direccionGPS.doesNotContain=" + DEFAULT_DIRECCION_GPS);

        // Get all the empresaList where direccionGPS does not contain UPDATED_DIRECCION_GPS
        defaultEmpresaShouldBeFound("direccionGPS.doesNotContain=" + UPDATED_DIRECCION_GPS);
    }

    @Test
    @Transactional
    void getAllEmpresasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where email equals to DEFAULT_EMAIL
        defaultEmpresaShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the empresaList where email equals to UPDATED_EMAIL
        defaultEmpresaShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmpresasByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where email not equals to DEFAULT_EMAIL
        defaultEmpresaShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the empresaList where email not equals to UPDATED_EMAIL
        defaultEmpresaShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmpresasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultEmpresaShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the empresaList where email equals to UPDATED_EMAIL
        defaultEmpresaShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmpresasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where email is not null
        defaultEmpresaShouldBeFound("email.specified=true");

        // Get all the empresaList where email is null
        defaultEmpresaShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpresasByEmailContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where email contains DEFAULT_EMAIL
        defaultEmpresaShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the empresaList where email contains UPDATED_EMAIL
        defaultEmpresaShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmpresasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where email does not contain DEFAULT_EMAIL
        defaultEmpresaShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the empresaList where email does not contain UPDATED_EMAIL
        defaultEmpresaShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmpresasByCelularIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where celular equals to DEFAULT_CELULAR
        defaultEmpresaShouldBeFound("celular.equals=" + DEFAULT_CELULAR);

        // Get all the empresaList where celular equals to UPDATED_CELULAR
        defaultEmpresaShouldNotBeFound("celular.equals=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    void getAllEmpresasByCelularIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where celular not equals to DEFAULT_CELULAR
        defaultEmpresaShouldNotBeFound("celular.notEquals=" + DEFAULT_CELULAR);

        // Get all the empresaList where celular not equals to UPDATED_CELULAR
        defaultEmpresaShouldBeFound("celular.notEquals=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    void getAllEmpresasByCelularIsInShouldWork() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where celular in DEFAULT_CELULAR or UPDATED_CELULAR
        defaultEmpresaShouldBeFound("celular.in=" + DEFAULT_CELULAR + "," + UPDATED_CELULAR);

        // Get all the empresaList where celular equals to UPDATED_CELULAR
        defaultEmpresaShouldNotBeFound("celular.in=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    void getAllEmpresasByCelularIsNullOrNotNull() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where celular is not null
        defaultEmpresaShouldBeFound("celular.specified=true");

        // Get all the empresaList where celular is null
        defaultEmpresaShouldNotBeFound("celular.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpresasByCelularContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where celular contains DEFAULT_CELULAR
        defaultEmpresaShouldBeFound("celular.contains=" + DEFAULT_CELULAR);

        // Get all the empresaList where celular contains UPDATED_CELULAR
        defaultEmpresaShouldNotBeFound("celular.contains=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    void getAllEmpresasByCelularNotContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where celular does not contain DEFAULT_CELULAR
        defaultEmpresaShouldNotBeFound("celular.doesNotContain=" + DEFAULT_CELULAR);

        // Get all the empresaList where celular does not contain UPDATED_CELULAR
        defaultEmpresaShouldBeFound("celular.doesNotContain=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    void getAllEmpresasByIndicativoIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where indicativo equals to DEFAULT_INDICATIVO
        defaultEmpresaShouldBeFound("indicativo.equals=" + DEFAULT_INDICATIVO);

        // Get all the empresaList where indicativo equals to UPDATED_INDICATIVO
        defaultEmpresaShouldNotBeFound("indicativo.equals=" + UPDATED_INDICATIVO);
    }

    @Test
    @Transactional
    void getAllEmpresasByIndicativoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where indicativo not equals to DEFAULT_INDICATIVO
        defaultEmpresaShouldNotBeFound("indicativo.notEquals=" + DEFAULT_INDICATIVO);

        // Get all the empresaList where indicativo not equals to UPDATED_INDICATIVO
        defaultEmpresaShouldBeFound("indicativo.notEquals=" + UPDATED_INDICATIVO);
    }

    @Test
    @Transactional
    void getAllEmpresasByIndicativoIsInShouldWork() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where indicativo in DEFAULT_INDICATIVO or UPDATED_INDICATIVO
        defaultEmpresaShouldBeFound("indicativo.in=" + DEFAULT_INDICATIVO + "," + UPDATED_INDICATIVO);

        // Get all the empresaList where indicativo equals to UPDATED_INDICATIVO
        defaultEmpresaShouldNotBeFound("indicativo.in=" + UPDATED_INDICATIVO);
    }

    @Test
    @Transactional
    void getAllEmpresasByIndicativoIsNullOrNotNull() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where indicativo is not null
        defaultEmpresaShouldBeFound("indicativo.specified=true");

        // Get all the empresaList where indicativo is null
        defaultEmpresaShouldNotBeFound("indicativo.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpresasByIndicativoContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where indicativo contains DEFAULT_INDICATIVO
        defaultEmpresaShouldBeFound("indicativo.contains=" + DEFAULT_INDICATIVO);

        // Get all the empresaList where indicativo contains UPDATED_INDICATIVO
        defaultEmpresaShouldNotBeFound("indicativo.contains=" + UPDATED_INDICATIVO);
    }

    @Test
    @Transactional
    void getAllEmpresasByIndicativoNotContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where indicativo does not contain DEFAULT_INDICATIVO
        defaultEmpresaShouldNotBeFound("indicativo.doesNotContain=" + DEFAULT_INDICATIVO);

        // Get all the empresaList where indicativo does not contain UPDATED_INDICATIVO
        defaultEmpresaShouldBeFound("indicativo.doesNotContain=" + UPDATED_INDICATIVO);
    }

    @Test
    @Transactional
    void getAllEmpresasByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where estado equals to DEFAULT_ESTADO
        defaultEmpresaShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the empresaList where estado equals to UPDATED_ESTADO
        defaultEmpresaShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllEmpresasByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where estado not equals to DEFAULT_ESTADO
        defaultEmpresaShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the empresaList where estado not equals to UPDATED_ESTADO
        defaultEmpresaShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllEmpresasByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultEmpresaShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the empresaList where estado equals to UPDATED_ESTADO
        defaultEmpresaShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllEmpresasByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where estado is not null
        defaultEmpresaShouldBeFound("estado.specified=true");

        // Get all the empresaList where estado is null
        defaultEmpresaShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpresasByEstadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where estado is greater than or equal to DEFAULT_ESTADO
        defaultEmpresaShouldBeFound("estado.greaterThanOrEqual=" + DEFAULT_ESTADO);

        // Get all the empresaList where estado is greater than or equal to UPDATED_ESTADO
        defaultEmpresaShouldNotBeFound("estado.greaterThanOrEqual=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllEmpresasByEstadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where estado is less than or equal to DEFAULT_ESTADO
        defaultEmpresaShouldBeFound("estado.lessThanOrEqual=" + DEFAULT_ESTADO);

        // Get all the empresaList where estado is less than or equal to SMALLER_ESTADO
        defaultEmpresaShouldNotBeFound("estado.lessThanOrEqual=" + SMALLER_ESTADO);
    }

    @Test
    @Transactional
    void getAllEmpresasByEstadoIsLessThanSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where estado is less than DEFAULT_ESTADO
        defaultEmpresaShouldNotBeFound("estado.lessThan=" + DEFAULT_ESTADO);

        // Get all the empresaList where estado is less than UPDATED_ESTADO
        defaultEmpresaShouldBeFound("estado.lessThan=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllEmpresasByEstadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where estado is greater than DEFAULT_ESTADO
        defaultEmpresaShouldNotBeFound("estado.greaterThan=" + DEFAULT_ESTADO);

        // Get all the empresaList where estado is greater than SMALLER_ESTADO
        defaultEmpresaShouldBeFound("estado.greaterThan=" + SMALLER_ESTADO);
    }

    @Test
    @Transactional
    void getAllEmpresasByFechaRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where fechaRegistro equals to DEFAULT_FECHA_REGISTRO
        defaultEmpresaShouldBeFound("fechaRegistro.equals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the empresaList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultEmpresaShouldNotBeFound("fechaRegistro.equals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllEmpresasByFechaRegistroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where fechaRegistro not equals to DEFAULT_FECHA_REGISTRO
        defaultEmpresaShouldNotBeFound("fechaRegistro.notEquals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the empresaList where fechaRegistro not equals to UPDATED_FECHA_REGISTRO
        defaultEmpresaShouldBeFound("fechaRegistro.notEquals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllEmpresasByFechaRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where fechaRegistro in DEFAULT_FECHA_REGISTRO or UPDATED_FECHA_REGISTRO
        defaultEmpresaShouldBeFound("fechaRegistro.in=" + DEFAULT_FECHA_REGISTRO + "," + UPDATED_FECHA_REGISTRO);

        // Get all the empresaList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultEmpresaShouldNotBeFound("fechaRegistro.in=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllEmpresasByFechaRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where fechaRegistro is not null
        defaultEmpresaShouldBeFound("fechaRegistro.specified=true");

        // Get all the empresaList where fechaRegistro is null
        defaultEmpresaShouldNotBeFound("fechaRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpresasByComponenteIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);
        Componente componente;
        if (TestUtil.findAll(em, Componente.class).isEmpty()) {
            componente = ComponenteResourceIT.createEntity(em);
            em.persist(componente);
            em.flush();
        } else {
            componente = TestUtil.findAll(em, Componente.class).get(0);
        }
        em.persist(componente);
        em.flush();
        empresa.addComponente(componente);
        empresaRepository.saveAndFlush(empresa);
        Long componenteId = componente.getId();

        // Get all the empresaList where componente equals to componenteId
        defaultEmpresaShouldBeFound("componenteId.equals=" + componenteId);

        // Get all the empresaList where componente equals to (componenteId + 1)
        defaultEmpresaShouldNotBeFound("componenteId.equals=" + (componenteId + 1));
    }

    @Test
    @Transactional
    void getAllEmpresasByRolIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);
        Rol rol;
        if (TestUtil.findAll(em, Rol.class).isEmpty()) {
            rol = RolResourceIT.createEntity(em);
            em.persist(rol);
            em.flush();
        } else {
            rol = TestUtil.findAll(em, Rol.class).get(0);
        }
        em.persist(rol);
        em.flush();
        empresa.addRol(rol);
        empresaRepository.saveAndFlush(empresa);
        Long rolId = rol.getId();

        // Get all the empresaList where rol equals to rolId
        defaultEmpresaShouldBeFound("rolId.equals=" + rolId);

        // Get all the empresaList where rol equals to (rolId + 1)
        defaultEmpresaShouldNotBeFound("rolId.equals=" + (rolId + 1));
    }

    @Test
    @Transactional
    void getAllEmpresasByUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            usuario = UsuarioResourceIT.createEntity(em);
            em.persist(usuario);
            em.flush();
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        empresa.addUsuario(usuario);
        empresaRepository.saveAndFlush(empresa);
        Long usuarioId = usuario.getId();

        // Get all the empresaList where usuario equals to usuarioId
        defaultEmpresaShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the empresaList where usuario equals to (usuarioId + 1)
        defaultEmpresaShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    @Test
    @Transactional
    void getAllEmpresasBySucursalIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);
        Sucursal sucursal;
        if (TestUtil.findAll(em, Sucursal.class).isEmpty()) {
            sucursal = SucursalResourceIT.createEntity(em);
            em.persist(sucursal);
            em.flush();
        } else {
            sucursal = TestUtil.findAll(em, Sucursal.class).get(0);
        }
        em.persist(sucursal);
        em.flush();
        empresa.addSucursal(sucursal);
        empresaRepository.saveAndFlush(empresa);
        Long sucursalId = sucursal.getId();

        // Get all the empresaList where sucursal equals to sucursalId
        defaultEmpresaShouldBeFound("sucursalId.equals=" + sucursalId);

        // Get all the empresaList where sucursal equals to (sucursalId + 1)
        defaultEmpresaShouldNotBeFound("sucursalId.equals=" + (sucursalId + 1));
    }

    @Test
    @Transactional
    void getAllEmpresasBySucursalIdIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);
        Sucursal sucursalId;
        if (TestUtil.findAll(em, Sucursal.class).isEmpty()) {
            sucursalId = SucursalResourceIT.createEntity(em);
            em.persist(sucursalId);
            em.flush();
        } else {
            sucursalId = TestUtil.findAll(em, Sucursal.class).get(0);
        }
        em.persist(sucursalId);
        em.flush();
        empresa.addSucursalId(sucursalId);
        empresaRepository.saveAndFlush(empresa);
        Long sucursalIdId = sucursalId.getId();

        // Get all the empresaList where sucursalId equals to sucursalIdId
        defaultEmpresaShouldBeFound("sucursalIdId.equals=" + sucursalIdId);

        // Get all the empresaList where sucursalId equals to (sucursalIdId + 1)
        defaultEmpresaShouldNotBeFound("sucursalIdId.equals=" + (sucursalIdId + 1));
    }

    @Test
    @Transactional
    void getAllEmpresasByInfoLegalIdIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);
        InfoLegal infoLegalId;
        if (TestUtil.findAll(em, InfoLegal.class).isEmpty()) {
            infoLegalId = InfoLegalResourceIT.createEntity(em);
            em.persist(infoLegalId);
            em.flush();
        } else {
            infoLegalId = TestUtil.findAll(em, InfoLegal.class).get(0);
        }
        em.persist(infoLegalId);
        em.flush();
        empresa.addInfoLegalId(infoLegalId);
        empresaRepository.saveAndFlush(empresa);
        Long infoLegalIdId = infoLegalId.getId();

        // Get all the empresaList where infoLegalId equals to infoLegalIdId
        defaultEmpresaShouldBeFound("infoLegalIdId.equals=" + infoLegalIdId);

        // Get all the empresaList where infoLegalId equals to (infoLegalIdId + 1)
        defaultEmpresaShouldNotBeFound("infoLegalIdId.equals=" + (infoLegalIdId + 1));
    }

    @Test
    @Transactional
    void getAllEmpresasByUsuarioIdIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);
        Usuario usuarioId;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            usuarioId = UsuarioResourceIT.createEntity(em);
            em.persist(usuarioId);
            em.flush();
        } else {
            usuarioId = TestUtil.findAll(em, Usuario.class).get(0);
        }
        em.persist(usuarioId);
        em.flush();
        empresa.addUsuarioId(usuarioId);
        empresaRepository.saveAndFlush(empresa);
        Long usuarioIdId = usuarioId.getId();

        // Get all the empresaList where usuarioId equals to usuarioIdId
        defaultEmpresaShouldBeFound("usuarioIdId.equals=" + usuarioIdId);

        // Get all the empresaList where usuarioId equals to (usuarioIdId + 1)
        defaultEmpresaShouldNotBeFound("usuarioIdId.equals=" + (usuarioIdId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmpresaShouldBeFound(String filter) throws Exception {
        restEmpresaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empresa.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].direccionGPS").value(hasItem(DEFAULT_DIRECCION_GPS)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR)))
            .andExpect(jsonPath("$.[*].indicativo").value(hasItem(DEFAULT_INDICATIVO)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));

        // Check, that the count call also returns 1
        restEmpresaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmpresaShouldNotBeFound(String filter) throws Exception {
        restEmpresaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmpresaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmpresa() throws Exception {
        // Get the empresa
        restEmpresaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmpresa() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();

        // Update the empresa
        Empresa updatedEmpresa = empresaRepository.findById(empresa.getId()).get();
        // Disconnect from session so that the updates on updatedEmpresa are not directly saved in db
        em.detach(updatedEmpresa);
        updatedEmpresa
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .direccionGPS(UPDATED_DIRECCION_GPS)
            .email(UPDATED_EMAIL)
            .celular(UPDATED_CELULAR)
            .indicativo(UPDATED_INDICATIVO)
            .estado(UPDATED_ESTADO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restEmpresaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmpresa.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmpresa))
            )
            .andExpect(status().isOk());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEmpresa.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testEmpresa.getDireccionGPS()).isEqualTo(UPDATED_DIRECCION_GPS);
        assertThat(testEmpresa.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmpresa.getCelular()).isEqualTo(UPDATED_CELULAR);
        assertThat(testEmpresa.getIndicativo()).isEqualTo(UPDATED_INDICATIVO);
        assertThat(testEmpresa.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testEmpresa.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();
        empresa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empresa.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(empresa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();
        empresa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(empresa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();
        empresa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(empresa)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmpresaWithPatch() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();

        // Update the empresa using partial update
        Empresa partialUpdatedEmpresa = new Empresa();
        partialUpdatedEmpresa.setId(empresa.getId());

        partialUpdatedEmpresa
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .email(UPDATED_EMAIL)
            .indicativo(UPDATED_INDICATIVO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpresa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmpresa))
            )
            .andExpect(status().isOk());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEmpresa.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testEmpresa.getDireccionGPS()).isEqualTo(DEFAULT_DIRECCION_GPS);
        assertThat(testEmpresa.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmpresa.getCelular()).isEqualTo(DEFAULT_CELULAR);
        assertThat(testEmpresa.getIndicativo()).isEqualTo(UPDATED_INDICATIVO);
        assertThat(testEmpresa.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testEmpresa.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdateEmpresaWithPatch() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();

        // Update the empresa using partial update
        Empresa partialUpdatedEmpresa = new Empresa();
        partialUpdatedEmpresa.setId(empresa.getId());

        partialUpdatedEmpresa
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .direccionGPS(UPDATED_DIRECCION_GPS)
            .email(UPDATED_EMAIL)
            .celular(UPDATED_CELULAR)
            .indicativo(UPDATED_INDICATIVO)
            .estado(UPDATED_ESTADO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpresa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmpresa))
            )
            .andExpect(status().isOk());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEmpresa.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testEmpresa.getDireccionGPS()).isEqualTo(UPDATED_DIRECCION_GPS);
        assertThat(testEmpresa.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmpresa.getCelular()).isEqualTo(UPDATED_CELULAR);
        assertThat(testEmpresa.getIndicativo()).isEqualTo(UPDATED_INDICATIVO);
        assertThat(testEmpresa.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testEmpresa.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();
        empresa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, empresa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(empresa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();
        empresa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(empresa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();
        empresa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(empresa)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmpresa() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        int databaseSizeBeforeDelete = empresaRepository.findAll().size();

        // Delete the empresa
        restEmpresaMockMvc
            .perform(delete(ENTITY_API_URL_ID, empresa.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
