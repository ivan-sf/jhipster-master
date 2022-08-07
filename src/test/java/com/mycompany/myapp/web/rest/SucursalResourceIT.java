package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Empresa;
import com.mycompany.myapp.domain.InfoLegal;
import com.mycompany.myapp.domain.Inventario;
import com.mycompany.myapp.domain.Oficina;
import com.mycompany.myapp.domain.Sucursal;
import com.mycompany.myapp.domain.Usuario;
import com.mycompany.myapp.repository.SucursalRepository;
import com.mycompany.myapp.service.criteria.SucursalCriteria;
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
 * Integration tests for the {@link SucursalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SucursalResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_NIT = "AAAAAAAAAA";
    private static final String UPDATED_NIT = "BBBBBBBBBB";

    private static final String DEFAULT_DETALLE = "AAAAAAAAAA";
    private static final String UPDATED_DETALLE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION_GPS = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION_GPS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_ESTADO = 1;
    private static final Integer UPDATED_ESTADO = 2;
    private static final Integer SMALLER_ESTADO = 1 - 1;

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/sucursals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSucursalMockMvc;

    private Sucursal sucursal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sucursal createEntity(EntityManager em) {
        Sucursal sucursal = new Sucursal()
            .nombre(DEFAULT_NOMBRE)
            .nit(DEFAULT_NIT)
            .detalle(DEFAULT_DETALLE)
            .direccion(DEFAULT_DIRECCION)
            .direccionGPS(DEFAULT_DIRECCION_GPS)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE)
            .estado(DEFAULT_ESTADO)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return sucursal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sucursal createUpdatedEntity(EntityManager em) {
        Sucursal sucursal = new Sucursal()
            .nombre(UPDATED_NOMBRE)
            .nit(UPDATED_NIT)
            .detalle(UPDATED_DETALLE)
            .direccion(UPDATED_DIRECCION)
            .direccionGPS(UPDATED_DIRECCION_GPS)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .estado(UPDATED_ESTADO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);
        return sucursal;
    }

    @BeforeEach
    public void initTest() {
        sucursal = createEntity(em);
    }

    @Test
    @Transactional
    void createSucursal() throws Exception {
        int databaseSizeBeforeCreate = sucursalRepository.findAll().size();
        // Create the Sucursal
        restSucursalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sucursal)))
            .andExpect(status().isCreated());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeCreate + 1);
        Sucursal testSucursal = sucursalList.get(sucursalList.size() - 1);
        assertThat(testSucursal.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testSucursal.getNit()).isEqualTo(DEFAULT_NIT);
        assertThat(testSucursal.getDetalle()).isEqualTo(DEFAULT_DETALLE);
        assertThat(testSucursal.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testSucursal.getDireccionGPS()).isEqualTo(DEFAULT_DIRECCION_GPS);
        assertThat(testSucursal.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testSucursal.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
        assertThat(testSucursal.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testSucursal.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createSucursalWithExistingId() throws Exception {
        // Create the Sucursal with an existing ID
        sucursal.setId(1L);

        int databaseSizeBeforeCreate = sucursalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSucursalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sucursal)))
            .andExpect(status().isBadRequest());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSucursals() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList
        restSucursalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sucursal.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].nit").value(hasItem(DEFAULT_NIT)))
            .andExpect(jsonPath("$.[*].detalle").value(hasItem(DEFAULT_DETALLE.toString())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].direccionGPS").value(hasItem(DEFAULT_DIRECCION_GPS)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @Test
    @Transactional
    void getSucursal() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get the sucursal
        restSucursalMockMvc
            .perform(get(ENTITY_API_URL_ID, sucursal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sucursal.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.nit").value(DEFAULT_NIT))
            .andExpect(jsonPath("$.detalle").value(DEFAULT_DETALLE.toString()))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.direccionGPS").value(DEFAULT_DIRECCION_GPS))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getSucursalsByIdFiltering() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        Long id = sucursal.getId();

        defaultSucursalShouldBeFound("id.equals=" + id);
        defaultSucursalShouldNotBeFound("id.notEquals=" + id);

        defaultSucursalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSucursalShouldNotBeFound("id.greaterThan=" + id);

        defaultSucursalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSucursalShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSucursalsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where nombre equals to DEFAULT_NOMBRE
        defaultSucursalShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the sucursalList where nombre equals to UPDATED_NOMBRE
        defaultSucursalShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllSucursalsByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where nombre not equals to DEFAULT_NOMBRE
        defaultSucursalShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the sucursalList where nombre not equals to UPDATED_NOMBRE
        defaultSucursalShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllSucursalsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultSucursalShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the sucursalList where nombre equals to UPDATED_NOMBRE
        defaultSucursalShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllSucursalsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where nombre is not null
        defaultSucursalShouldBeFound("nombre.specified=true");

        // Get all the sucursalList where nombre is null
        defaultSucursalShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllSucursalsByNombreContainsSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where nombre contains DEFAULT_NOMBRE
        defaultSucursalShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the sucursalList where nombre contains UPDATED_NOMBRE
        defaultSucursalShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllSucursalsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where nombre does not contain DEFAULT_NOMBRE
        defaultSucursalShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the sucursalList where nombre does not contain UPDATED_NOMBRE
        defaultSucursalShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllSucursalsByNitIsEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where nit equals to DEFAULT_NIT
        defaultSucursalShouldBeFound("nit.equals=" + DEFAULT_NIT);

        // Get all the sucursalList where nit equals to UPDATED_NIT
        defaultSucursalShouldNotBeFound("nit.equals=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllSucursalsByNitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where nit not equals to DEFAULT_NIT
        defaultSucursalShouldNotBeFound("nit.notEquals=" + DEFAULT_NIT);

        // Get all the sucursalList where nit not equals to UPDATED_NIT
        defaultSucursalShouldBeFound("nit.notEquals=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllSucursalsByNitIsInShouldWork() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where nit in DEFAULT_NIT or UPDATED_NIT
        defaultSucursalShouldBeFound("nit.in=" + DEFAULT_NIT + "," + UPDATED_NIT);

        // Get all the sucursalList where nit equals to UPDATED_NIT
        defaultSucursalShouldNotBeFound("nit.in=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllSucursalsByNitIsNullOrNotNull() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where nit is not null
        defaultSucursalShouldBeFound("nit.specified=true");

        // Get all the sucursalList where nit is null
        defaultSucursalShouldNotBeFound("nit.specified=false");
    }

    @Test
    @Transactional
    void getAllSucursalsByNitContainsSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where nit contains DEFAULT_NIT
        defaultSucursalShouldBeFound("nit.contains=" + DEFAULT_NIT);

        // Get all the sucursalList where nit contains UPDATED_NIT
        defaultSucursalShouldNotBeFound("nit.contains=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllSucursalsByNitNotContainsSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where nit does not contain DEFAULT_NIT
        defaultSucursalShouldNotBeFound("nit.doesNotContain=" + DEFAULT_NIT);

        // Get all the sucursalList where nit does not contain UPDATED_NIT
        defaultSucursalShouldBeFound("nit.doesNotContain=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllSucursalsByDireccionIsEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where direccion equals to DEFAULT_DIRECCION
        defaultSucursalShouldBeFound("direccion.equals=" + DEFAULT_DIRECCION);

        // Get all the sucursalList where direccion equals to UPDATED_DIRECCION
        defaultSucursalShouldNotBeFound("direccion.equals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllSucursalsByDireccionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where direccion not equals to DEFAULT_DIRECCION
        defaultSucursalShouldNotBeFound("direccion.notEquals=" + DEFAULT_DIRECCION);

        // Get all the sucursalList where direccion not equals to UPDATED_DIRECCION
        defaultSucursalShouldBeFound("direccion.notEquals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllSucursalsByDireccionIsInShouldWork() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where direccion in DEFAULT_DIRECCION or UPDATED_DIRECCION
        defaultSucursalShouldBeFound("direccion.in=" + DEFAULT_DIRECCION + "," + UPDATED_DIRECCION);

        // Get all the sucursalList where direccion equals to UPDATED_DIRECCION
        defaultSucursalShouldNotBeFound("direccion.in=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllSucursalsByDireccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where direccion is not null
        defaultSucursalShouldBeFound("direccion.specified=true");

        // Get all the sucursalList where direccion is null
        defaultSucursalShouldNotBeFound("direccion.specified=false");
    }

    @Test
    @Transactional
    void getAllSucursalsByDireccionContainsSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where direccion contains DEFAULT_DIRECCION
        defaultSucursalShouldBeFound("direccion.contains=" + DEFAULT_DIRECCION);

        // Get all the sucursalList where direccion contains UPDATED_DIRECCION
        defaultSucursalShouldNotBeFound("direccion.contains=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllSucursalsByDireccionNotContainsSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where direccion does not contain DEFAULT_DIRECCION
        defaultSucursalShouldNotBeFound("direccion.doesNotContain=" + DEFAULT_DIRECCION);

        // Get all the sucursalList where direccion does not contain UPDATED_DIRECCION
        defaultSucursalShouldBeFound("direccion.doesNotContain=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllSucursalsByDireccionGPSIsEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where direccionGPS equals to DEFAULT_DIRECCION_GPS
        defaultSucursalShouldBeFound("direccionGPS.equals=" + DEFAULT_DIRECCION_GPS);

        // Get all the sucursalList where direccionGPS equals to UPDATED_DIRECCION_GPS
        defaultSucursalShouldNotBeFound("direccionGPS.equals=" + UPDATED_DIRECCION_GPS);
    }

    @Test
    @Transactional
    void getAllSucursalsByDireccionGPSIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where direccionGPS not equals to DEFAULT_DIRECCION_GPS
        defaultSucursalShouldNotBeFound("direccionGPS.notEquals=" + DEFAULT_DIRECCION_GPS);

        // Get all the sucursalList where direccionGPS not equals to UPDATED_DIRECCION_GPS
        defaultSucursalShouldBeFound("direccionGPS.notEquals=" + UPDATED_DIRECCION_GPS);
    }

    @Test
    @Transactional
    void getAllSucursalsByDireccionGPSIsInShouldWork() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where direccionGPS in DEFAULT_DIRECCION_GPS or UPDATED_DIRECCION_GPS
        defaultSucursalShouldBeFound("direccionGPS.in=" + DEFAULT_DIRECCION_GPS + "," + UPDATED_DIRECCION_GPS);

        // Get all the sucursalList where direccionGPS equals to UPDATED_DIRECCION_GPS
        defaultSucursalShouldNotBeFound("direccionGPS.in=" + UPDATED_DIRECCION_GPS);
    }

    @Test
    @Transactional
    void getAllSucursalsByDireccionGPSIsNullOrNotNull() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where direccionGPS is not null
        defaultSucursalShouldBeFound("direccionGPS.specified=true");

        // Get all the sucursalList where direccionGPS is null
        defaultSucursalShouldNotBeFound("direccionGPS.specified=false");
    }

    @Test
    @Transactional
    void getAllSucursalsByDireccionGPSContainsSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where direccionGPS contains DEFAULT_DIRECCION_GPS
        defaultSucursalShouldBeFound("direccionGPS.contains=" + DEFAULT_DIRECCION_GPS);

        // Get all the sucursalList where direccionGPS contains UPDATED_DIRECCION_GPS
        defaultSucursalShouldNotBeFound("direccionGPS.contains=" + UPDATED_DIRECCION_GPS);
    }

    @Test
    @Transactional
    void getAllSucursalsByDireccionGPSNotContainsSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where direccionGPS does not contain DEFAULT_DIRECCION_GPS
        defaultSucursalShouldNotBeFound("direccionGPS.doesNotContain=" + DEFAULT_DIRECCION_GPS);

        // Get all the sucursalList where direccionGPS does not contain UPDATED_DIRECCION_GPS
        defaultSucursalShouldBeFound("direccionGPS.doesNotContain=" + UPDATED_DIRECCION_GPS);
    }

    @Test
    @Transactional
    void getAllSucursalsByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where estado equals to DEFAULT_ESTADO
        defaultSucursalShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the sucursalList where estado equals to UPDATED_ESTADO
        defaultSucursalShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllSucursalsByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where estado not equals to DEFAULT_ESTADO
        defaultSucursalShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the sucursalList where estado not equals to UPDATED_ESTADO
        defaultSucursalShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllSucursalsByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultSucursalShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the sucursalList where estado equals to UPDATED_ESTADO
        defaultSucursalShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllSucursalsByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where estado is not null
        defaultSucursalShouldBeFound("estado.specified=true");

        // Get all the sucursalList where estado is null
        defaultSucursalShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllSucursalsByEstadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where estado is greater than or equal to DEFAULT_ESTADO
        defaultSucursalShouldBeFound("estado.greaterThanOrEqual=" + DEFAULT_ESTADO);

        // Get all the sucursalList where estado is greater than or equal to UPDATED_ESTADO
        defaultSucursalShouldNotBeFound("estado.greaterThanOrEqual=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllSucursalsByEstadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where estado is less than or equal to DEFAULT_ESTADO
        defaultSucursalShouldBeFound("estado.lessThanOrEqual=" + DEFAULT_ESTADO);

        // Get all the sucursalList where estado is less than or equal to SMALLER_ESTADO
        defaultSucursalShouldNotBeFound("estado.lessThanOrEqual=" + SMALLER_ESTADO);
    }

    @Test
    @Transactional
    void getAllSucursalsByEstadoIsLessThanSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where estado is less than DEFAULT_ESTADO
        defaultSucursalShouldNotBeFound("estado.lessThan=" + DEFAULT_ESTADO);

        // Get all the sucursalList where estado is less than UPDATED_ESTADO
        defaultSucursalShouldBeFound("estado.lessThan=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllSucursalsByEstadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where estado is greater than DEFAULT_ESTADO
        defaultSucursalShouldNotBeFound("estado.greaterThan=" + DEFAULT_ESTADO);

        // Get all the sucursalList where estado is greater than SMALLER_ESTADO
        defaultSucursalShouldBeFound("estado.greaterThan=" + SMALLER_ESTADO);
    }

    @Test
    @Transactional
    void getAllSucursalsByFechaRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where fechaRegistro equals to DEFAULT_FECHA_REGISTRO
        defaultSucursalShouldBeFound("fechaRegistro.equals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the sucursalList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultSucursalShouldNotBeFound("fechaRegistro.equals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllSucursalsByFechaRegistroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where fechaRegistro not equals to DEFAULT_FECHA_REGISTRO
        defaultSucursalShouldNotBeFound("fechaRegistro.notEquals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the sucursalList where fechaRegistro not equals to UPDATED_FECHA_REGISTRO
        defaultSucursalShouldBeFound("fechaRegistro.notEquals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllSucursalsByFechaRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where fechaRegistro in DEFAULT_FECHA_REGISTRO or UPDATED_FECHA_REGISTRO
        defaultSucursalShouldBeFound("fechaRegistro.in=" + DEFAULT_FECHA_REGISTRO + "," + UPDATED_FECHA_REGISTRO);

        // Get all the sucursalList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultSucursalShouldNotBeFound("fechaRegistro.in=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllSucursalsByFechaRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where fechaRegistro is not null
        defaultSucursalShouldBeFound("fechaRegistro.specified=true");

        // Get all the sucursalList where fechaRegistro is null
        defaultSucursalShouldNotBeFound("fechaRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllSucursalsByOficinaIsEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);
        Oficina oficina;
        if (TestUtil.findAll(em, Oficina.class).isEmpty()) {
            oficina = OficinaResourceIT.createEntity(em);
            em.persist(oficina);
            em.flush();
        } else {
            oficina = TestUtil.findAll(em, Oficina.class).get(0);
        }
        em.persist(oficina);
        em.flush();
        sucursal.addOficina(oficina);
        sucursalRepository.saveAndFlush(sucursal);
        Long oficinaId = oficina.getId();

        // Get all the sucursalList where oficina equals to oficinaId
        defaultSucursalShouldBeFound("oficinaId.equals=" + oficinaId);

        // Get all the sucursalList where oficina equals to (oficinaId + 1)
        defaultSucursalShouldNotBeFound("oficinaId.equals=" + (oficinaId + 1));
    }

    @Test
    @Transactional
    void getAllSucursalsByInventarioIsEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);
        Inventario inventario;
        if (TestUtil.findAll(em, Inventario.class).isEmpty()) {
            inventario = InventarioResourceIT.createEntity(em);
            em.persist(inventario);
            em.flush();
        } else {
            inventario = TestUtil.findAll(em, Inventario.class).get(0);
        }
        em.persist(inventario);
        em.flush();
        sucursal.addInventario(inventario);
        sucursalRepository.saveAndFlush(sucursal);
        Long inventarioId = inventario.getId();

        // Get all the sucursalList where inventario equals to inventarioId
        defaultSucursalShouldBeFound("inventarioId.equals=" + inventarioId);

        // Get all the sucursalList where inventario equals to (inventarioId + 1)
        defaultSucursalShouldNotBeFound("inventarioId.equals=" + (inventarioId + 1));
    }

    @Test
    @Transactional
    void getAllSucursalsByEmpresaIsEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);
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
        sucursal.setEmpresa(empresa);
        sucursalRepository.saveAndFlush(sucursal);
        Long empresaId = empresa.getId();

        // Get all the sucursalList where empresa equals to empresaId
        defaultSucursalShouldBeFound("empresaId.equals=" + empresaId);

        // Get all the sucursalList where empresa equals to (empresaId + 1)
        defaultSucursalShouldNotBeFound("empresaId.equals=" + (empresaId + 1));
    }

    @Test
    @Transactional
    void getAllSucursalsByInfoLegalIsEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);
        InfoLegal infoLegal;
        if (TestUtil.findAll(em, InfoLegal.class).isEmpty()) {
            infoLegal = InfoLegalResourceIT.createEntity(em);
            em.persist(infoLegal);
            em.flush();
        } else {
            infoLegal = TestUtil.findAll(em, InfoLegal.class).get(0);
        }
        em.persist(infoLegal);
        em.flush();
        sucursal.addInfoLegal(infoLegal);
        sucursalRepository.saveAndFlush(sucursal);
        Long infoLegalId = infoLegal.getId();

        // Get all the sucursalList where infoLegal equals to infoLegalId
        defaultSucursalShouldBeFound("infoLegalId.equals=" + infoLegalId);

        // Get all the sucursalList where infoLegal equals to (infoLegalId + 1)
        defaultSucursalShouldNotBeFound("infoLegalId.equals=" + (infoLegalId + 1));
    }

    @Test
    @Transactional
    void getAllSucursalsByUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);
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
        sucursal.addUsuario(usuario);
        sucursalRepository.saveAndFlush(sucursal);
        Long usuarioId = usuario.getId();

        // Get all the sucursalList where usuario equals to usuarioId
        defaultSucursalShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the sucursalList where usuario equals to (usuarioId + 1)
        defaultSucursalShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    @Test
    @Transactional
    void getAllSucursalsByEmpresaIdIsEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);
        Empresa empresaId;
        if (TestUtil.findAll(em, Empresa.class).isEmpty()) {
            empresaId = EmpresaResourceIT.createEntity(em);
            em.persist(empresaId);
            em.flush();
        } else {
            empresaId = TestUtil.findAll(em, Empresa.class).get(0);
        }
        em.persist(empresaId);
        em.flush();
        sucursal.addEmpresaId(empresaId);
        sucursalRepository.saveAndFlush(sucursal);
        Long empresaIdId = empresaId.getId();

        // Get all the sucursalList where empresaId equals to empresaIdId
        defaultSucursalShouldBeFound("empresaIdId.equals=" + empresaIdId);

        // Get all the sucursalList where empresaId equals to (empresaIdId + 1)
        defaultSucursalShouldNotBeFound("empresaIdId.equals=" + (empresaIdId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSucursalShouldBeFound(String filter) throws Exception {
        restSucursalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sucursal.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].nit").value(hasItem(DEFAULT_NIT)))
            .andExpect(jsonPath("$.[*].detalle").value(hasItem(DEFAULT_DETALLE.toString())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].direccionGPS").value(hasItem(DEFAULT_DIRECCION_GPS)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));

        // Check, that the count call also returns 1
        restSucursalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSucursalShouldNotBeFound(String filter) throws Exception {
        restSucursalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSucursalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSucursal() throws Exception {
        // Get the sucursal
        restSucursalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSucursal() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        int databaseSizeBeforeUpdate = sucursalRepository.findAll().size();

        // Update the sucursal
        Sucursal updatedSucursal = sucursalRepository.findById(sucursal.getId()).get();
        // Disconnect from session so that the updates on updatedSucursal are not directly saved in db
        em.detach(updatedSucursal);
        updatedSucursal
            .nombre(UPDATED_NOMBRE)
            .nit(UPDATED_NIT)
            .detalle(UPDATED_DETALLE)
            .direccion(UPDATED_DIRECCION)
            .direccionGPS(UPDATED_DIRECCION_GPS)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .estado(UPDATED_ESTADO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restSucursalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSucursal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSucursal))
            )
            .andExpect(status().isOk());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeUpdate);
        Sucursal testSucursal = sucursalList.get(sucursalList.size() - 1);
        assertThat(testSucursal.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSucursal.getNit()).isEqualTo(UPDATED_NIT);
        assertThat(testSucursal.getDetalle()).isEqualTo(UPDATED_DETALLE);
        assertThat(testSucursal.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testSucursal.getDireccionGPS()).isEqualTo(UPDATED_DIRECCION_GPS);
        assertThat(testSucursal.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testSucursal.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testSucursal.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testSucursal.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingSucursal() throws Exception {
        int databaseSizeBeforeUpdate = sucursalRepository.findAll().size();
        sucursal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSucursalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sucursal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sucursal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSucursal() throws Exception {
        int databaseSizeBeforeUpdate = sucursalRepository.findAll().size();
        sucursal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSucursalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sucursal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSucursal() throws Exception {
        int databaseSizeBeforeUpdate = sucursalRepository.findAll().size();
        sucursal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSucursalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sucursal)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSucursalWithPatch() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        int databaseSizeBeforeUpdate = sucursalRepository.findAll().size();

        // Update the sucursal using partial update
        Sucursal partialUpdatedSucursal = new Sucursal();
        partialUpdatedSucursal.setId(sucursal.getId());

        partialUpdatedSucursal
            .nombre(UPDATED_NOMBRE)
            .nit(UPDATED_NIT)
            .direccion(UPDATED_DIRECCION)
            .direccionGPS(UPDATED_DIRECCION_GPS)
            .estado(UPDATED_ESTADO);

        restSucursalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSucursal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSucursal))
            )
            .andExpect(status().isOk());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeUpdate);
        Sucursal testSucursal = sucursalList.get(sucursalList.size() - 1);
        assertThat(testSucursal.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSucursal.getNit()).isEqualTo(UPDATED_NIT);
        assertThat(testSucursal.getDetalle()).isEqualTo(DEFAULT_DETALLE);
        assertThat(testSucursal.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testSucursal.getDireccionGPS()).isEqualTo(UPDATED_DIRECCION_GPS);
        assertThat(testSucursal.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testSucursal.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
        assertThat(testSucursal.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testSucursal.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdateSucursalWithPatch() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        int databaseSizeBeforeUpdate = sucursalRepository.findAll().size();

        // Update the sucursal using partial update
        Sucursal partialUpdatedSucursal = new Sucursal();
        partialUpdatedSucursal.setId(sucursal.getId());

        partialUpdatedSucursal
            .nombre(UPDATED_NOMBRE)
            .nit(UPDATED_NIT)
            .detalle(UPDATED_DETALLE)
            .direccion(UPDATED_DIRECCION)
            .direccionGPS(UPDATED_DIRECCION_GPS)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .estado(UPDATED_ESTADO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restSucursalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSucursal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSucursal))
            )
            .andExpect(status().isOk());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeUpdate);
        Sucursal testSucursal = sucursalList.get(sucursalList.size() - 1);
        assertThat(testSucursal.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSucursal.getNit()).isEqualTo(UPDATED_NIT);
        assertThat(testSucursal.getDetalle()).isEqualTo(UPDATED_DETALLE);
        assertThat(testSucursal.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testSucursal.getDireccionGPS()).isEqualTo(UPDATED_DIRECCION_GPS);
        assertThat(testSucursal.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testSucursal.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testSucursal.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testSucursal.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingSucursal() throws Exception {
        int databaseSizeBeforeUpdate = sucursalRepository.findAll().size();
        sucursal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSucursalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sucursal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sucursal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSucursal() throws Exception {
        int databaseSizeBeforeUpdate = sucursalRepository.findAll().size();
        sucursal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSucursalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sucursal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSucursal() throws Exception {
        int databaseSizeBeforeUpdate = sucursalRepository.findAll().size();
        sucursal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSucursalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sucursal)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSucursal() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        int databaseSizeBeforeDelete = sucursalRepository.findAll().size();

        // Delete the sucursal
        restSucursalMockMvc
            .perform(delete(ENTITY_API_URL_ID, sucursal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
