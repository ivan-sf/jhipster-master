package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Empresa;
import com.mycompany.myapp.domain.InfoLegal;
import com.mycompany.myapp.domain.Sucursal;
import com.mycompany.myapp.domain.Usuario;
import com.mycompany.myapp.repository.InfoLegalRepository;
import com.mycompany.myapp.service.InfoLegalService;
import com.mycompany.myapp.service.criteria.InfoLegalCriteria;
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
 * Integration tests for the {@link InfoLegalResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InfoLegalResourceIT {

    private static final String DEFAULT_NIT = "AAAAAAAAAA";
    private static final String UPDATED_NIT = "BBBBBBBBBB";

    private static final Integer DEFAULT_REGIMEN = 1;
    private static final Integer UPDATED_REGIMEN = 2;
    private static final Integer SMALLER_REGIMEN = 1 - 1;

    private static final String DEFAULT_RESOLUCION_POS = "AAAAAAAAAA";
    private static final String UPDATED_RESOLUCION_POS = "BBBBBBBBBB";

    private static final Integer DEFAULT_PREFIJO_POS_INICIAL = 1;
    private static final Integer UPDATED_PREFIJO_POS_INICIAL = 2;
    private static final Integer SMALLER_PREFIJO_POS_INICIAL = 1 - 1;

    private static final Integer DEFAULT_PREFIJO_POS_FINAL = 1;
    private static final Integer UPDATED_PREFIJO_POS_FINAL = 2;
    private static final Integer SMALLER_PREFIJO_POS_FINAL = 1 - 1;

    private static final String DEFAULT_RESOLUCION_FAC_ELEC = "AAAAAAAAAA";
    private static final String UPDATED_RESOLUCION_FAC_ELEC = "BBBBBBBBBB";

    private static final Integer DEFAULT_PREFIJO_FAC_ELEC_FINAL = 1;
    private static final Integer UPDATED_PREFIJO_FAC_ELEC_FINAL = 2;
    private static final Integer SMALLER_PREFIJO_FAC_ELEC_FINAL = 1 - 1;

    private static final String DEFAULT_RESOLUCION_NOM_ELEC = "AAAAAAAAAA";
    private static final String UPDATED_RESOLUCION_NOM_ELEC = "BBBBBBBBBB";

    private static final Integer DEFAULT_ESTADO = 1;
    private static final Integer UPDATED_ESTADO = 2;
    private static final Integer SMALLER_ESTADO = 1 - 1;

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/info-legals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InfoLegalRepository infoLegalRepository;

    @Mock
    private InfoLegalRepository infoLegalRepositoryMock;

    @Mock
    private InfoLegalService infoLegalServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInfoLegalMockMvc;

    private InfoLegal infoLegal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfoLegal createEntity(EntityManager em) {
        InfoLegal infoLegal = new InfoLegal()
            .nit(DEFAULT_NIT)
            .regimen(DEFAULT_REGIMEN)
            .resolucionPos(DEFAULT_RESOLUCION_POS)
            .prefijoPosInicial(DEFAULT_PREFIJO_POS_INICIAL)
            .prefijoPosFinal(DEFAULT_PREFIJO_POS_FINAL)
            .resolucionFacElec(DEFAULT_RESOLUCION_FAC_ELEC)
            .prefijoFacElecFinal(DEFAULT_PREFIJO_FAC_ELEC_FINAL)
            .resolucionNomElec(DEFAULT_RESOLUCION_NOM_ELEC)
            .estado(DEFAULT_ESTADO)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return infoLegal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfoLegal createUpdatedEntity(EntityManager em) {
        InfoLegal infoLegal = new InfoLegal()
            .nit(UPDATED_NIT)
            .regimen(UPDATED_REGIMEN)
            .resolucionPos(UPDATED_RESOLUCION_POS)
            .prefijoPosInicial(UPDATED_PREFIJO_POS_INICIAL)
            .prefijoPosFinal(UPDATED_PREFIJO_POS_FINAL)
            .resolucionFacElec(UPDATED_RESOLUCION_FAC_ELEC)
            .prefijoFacElecFinal(UPDATED_PREFIJO_FAC_ELEC_FINAL)
            .resolucionNomElec(UPDATED_RESOLUCION_NOM_ELEC)
            .estado(UPDATED_ESTADO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);
        return infoLegal;
    }

    @BeforeEach
    public void initTest() {
        infoLegal = createEntity(em);
    }

    @Test
    @Transactional
    void createInfoLegal() throws Exception {
        int databaseSizeBeforeCreate = infoLegalRepository.findAll().size();
        // Create the InfoLegal
        restInfoLegalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infoLegal)))
            .andExpect(status().isCreated());

        // Validate the InfoLegal in the database
        List<InfoLegal> infoLegalList = infoLegalRepository.findAll();
        assertThat(infoLegalList).hasSize(databaseSizeBeforeCreate + 1);
        InfoLegal testInfoLegal = infoLegalList.get(infoLegalList.size() - 1);
        assertThat(testInfoLegal.getNit()).isEqualTo(DEFAULT_NIT);
        assertThat(testInfoLegal.getRegimen()).isEqualTo(DEFAULT_REGIMEN);
        assertThat(testInfoLegal.getResolucionPos()).isEqualTo(DEFAULT_RESOLUCION_POS);
        assertThat(testInfoLegal.getPrefijoPosInicial()).isEqualTo(DEFAULT_PREFIJO_POS_INICIAL);
        assertThat(testInfoLegal.getPrefijoPosFinal()).isEqualTo(DEFAULT_PREFIJO_POS_FINAL);
        assertThat(testInfoLegal.getResolucionFacElec()).isEqualTo(DEFAULT_RESOLUCION_FAC_ELEC);
        assertThat(testInfoLegal.getPrefijoFacElecFinal()).isEqualTo(DEFAULT_PREFIJO_FAC_ELEC_FINAL);
        assertThat(testInfoLegal.getResolucionNomElec()).isEqualTo(DEFAULT_RESOLUCION_NOM_ELEC);
        assertThat(testInfoLegal.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testInfoLegal.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createInfoLegalWithExistingId() throws Exception {
        // Create the InfoLegal with an existing ID
        infoLegal.setId(1L);

        int databaseSizeBeforeCreate = infoLegalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInfoLegalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infoLegal)))
            .andExpect(status().isBadRequest());

        // Validate the InfoLegal in the database
        List<InfoLegal> infoLegalList = infoLegalRepository.findAll();
        assertThat(infoLegalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInfoLegals() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList
        restInfoLegalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(infoLegal.getId().intValue())))
            .andExpect(jsonPath("$.[*].nit").value(hasItem(DEFAULT_NIT)))
            .andExpect(jsonPath("$.[*].regimen").value(hasItem(DEFAULT_REGIMEN)))
            .andExpect(jsonPath("$.[*].resolucionPos").value(hasItem(DEFAULT_RESOLUCION_POS)))
            .andExpect(jsonPath("$.[*].prefijoPosInicial").value(hasItem(DEFAULT_PREFIJO_POS_INICIAL)))
            .andExpect(jsonPath("$.[*].prefijoPosFinal").value(hasItem(DEFAULT_PREFIJO_POS_FINAL)))
            .andExpect(jsonPath("$.[*].resolucionFacElec").value(hasItem(DEFAULT_RESOLUCION_FAC_ELEC)))
            .andExpect(jsonPath("$.[*].prefijoFacElecFinal").value(hasItem(DEFAULT_PREFIJO_FAC_ELEC_FINAL)))
            .andExpect(jsonPath("$.[*].resolucionNomElec").value(hasItem(DEFAULT_RESOLUCION_NOM_ELEC)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInfoLegalsWithEagerRelationshipsIsEnabled() throws Exception {
        when(infoLegalServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInfoLegalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(infoLegalServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInfoLegalsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(infoLegalServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInfoLegalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(infoLegalServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getInfoLegal() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get the infoLegal
        restInfoLegalMockMvc
            .perform(get(ENTITY_API_URL_ID, infoLegal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(infoLegal.getId().intValue()))
            .andExpect(jsonPath("$.nit").value(DEFAULT_NIT))
            .andExpect(jsonPath("$.regimen").value(DEFAULT_REGIMEN))
            .andExpect(jsonPath("$.resolucionPos").value(DEFAULT_RESOLUCION_POS))
            .andExpect(jsonPath("$.prefijoPosInicial").value(DEFAULT_PREFIJO_POS_INICIAL))
            .andExpect(jsonPath("$.prefijoPosFinal").value(DEFAULT_PREFIJO_POS_FINAL))
            .andExpect(jsonPath("$.resolucionFacElec").value(DEFAULT_RESOLUCION_FAC_ELEC))
            .andExpect(jsonPath("$.prefijoFacElecFinal").value(DEFAULT_PREFIJO_FAC_ELEC_FINAL))
            .andExpect(jsonPath("$.resolucionNomElec").value(DEFAULT_RESOLUCION_NOM_ELEC))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getInfoLegalsByIdFiltering() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        Long id = infoLegal.getId();

        defaultInfoLegalShouldBeFound("id.equals=" + id);
        defaultInfoLegalShouldNotBeFound("id.notEquals=" + id);

        defaultInfoLegalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInfoLegalShouldNotBeFound("id.greaterThan=" + id);

        defaultInfoLegalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInfoLegalShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByNitIsEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where nit equals to DEFAULT_NIT
        defaultInfoLegalShouldBeFound("nit.equals=" + DEFAULT_NIT);

        // Get all the infoLegalList where nit equals to UPDATED_NIT
        defaultInfoLegalShouldNotBeFound("nit.equals=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByNitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where nit not equals to DEFAULT_NIT
        defaultInfoLegalShouldNotBeFound("nit.notEquals=" + DEFAULT_NIT);

        // Get all the infoLegalList where nit not equals to UPDATED_NIT
        defaultInfoLegalShouldBeFound("nit.notEquals=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByNitIsInShouldWork() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where nit in DEFAULT_NIT or UPDATED_NIT
        defaultInfoLegalShouldBeFound("nit.in=" + DEFAULT_NIT + "," + UPDATED_NIT);

        // Get all the infoLegalList where nit equals to UPDATED_NIT
        defaultInfoLegalShouldNotBeFound("nit.in=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByNitIsNullOrNotNull() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where nit is not null
        defaultInfoLegalShouldBeFound("nit.specified=true");

        // Get all the infoLegalList where nit is null
        defaultInfoLegalShouldNotBeFound("nit.specified=false");
    }

    @Test
    @Transactional
    void getAllInfoLegalsByNitContainsSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where nit contains DEFAULT_NIT
        defaultInfoLegalShouldBeFound("nit.contains=" + DEFAULT_NIT);

        // Get all the infoLegalList where nit contains UPDATED_NIT
        defaultInfoLegalShouldNotBeFound("nit.contains=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByNitNotContainsSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where nit does not contain DEFAULT_NIT
        defaultInfoLegalShouldNotBeFound("nit.doesNotContain=" + DEFAULT_NIT);

        // Get all the infoLegalList where nit does not contain UPDATED_NIT
        defaultInfoLegalShouldBeFound("nit.doesNotContain=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByRegimenIsEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where regimen equals to DEFAULT_REGIMEN
        defaultInfoLegalShouldBeFound("regimen.equals=" + DEFAULT_REGIMEN);

        // Get all the infoLegalList where regimen equals to UPDATED_REGIMEN
        defaultInfoLegalShouldNotBeFound("regimen.equals=" + UPDATED_REGIMEN);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByRegimenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where regimen not equals to DEFAULT_REGIMEN
        defaultInfoLegalShouldNotBeFound("regimen.notEquals=" + DEFAULT_REGIMEN);

        // Get all the infoLegalList where regimen not equals to UPDATED_REGIMEN
        defaultInfoLegalShouldBeFound("regimen.notEquals=" + UPDATED_REGIMEN);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByRegimenIsInShouldWork() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where regimen in DEFAULT_REGIMEN or UPDATED_REGIMEN
        defaultInfoLegalShouldBeFound("regimen.in=" + DEFAULT_REGIMEN + "," + UPDATED_REGIMEN);

        // Get all the infoLegalList where regimen equals to UPDATED_REGIMEN
        defaultInfoLegalShouldNotBeFound("regimen.in=" + UPDATED_REGIMEN);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByRegimenIsNullOrNotNull() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where regimen is not null
        defaultInfoLegalShouldBeFound("regimen.specified=true");

        // Get all the infoLegalList where regimen is null
        defaultInfoLegalShouldNotBeFound("regimen.specified=false");
    }

    @Test
    @Transactional
    void getAllInfoLegalsByRegimenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where regimen is greater than or equal to DEFAULT_REGIMEN
        defaultInfoLegalShouldBeFound("regimen.greaterThanOrEqual=" + DEFAULT_REGIMEN);

        // Get all the infoLegalList where regimen is greater than or equal to UPDATED_REGIMEN
        defaultInfoLegalShouldNotBeFound("regimen.greaterThanOrEqual=" + UPDATED_REGIMEN);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByRegimenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where regimen is less than or equal to DEFAULT_REGIMEN
        defaultInfoLegalShouldBeFound("regimen.lessThanOrEqual=" + DEFAULT_REGIMEN);

        // Get all the infoLegalList where regimen is less than or equal to SMALLER_REGIMEN
        defaultInfoLegalShouldNotBeFound("regimen.lessThanOrEqual=" + SMALLER_REGIMEN);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByRegimenIsLessThanSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where regimen is less than DEFAULT_REGIMEN
        defaultInfoLegalShouldNotBeFound("regimen.lessThan=" + DEFAULT_REGIMEN);

        // Get all the infoLegalList where regimen is less than UPDATED_REGIMEN
        defaultInfoLegalShouldBeFound("regimen.lessThan=" + UPDATED_REGIMEN);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByRegimenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where regimen is greater than DEFAULT_REGIMEN
        defaultInfoLegalShouldNotBeFound("regimen.greaterThan=" + DEFAULT_REGIMEN);

        // Get all the infoLegalList where regimen is greater than SMALLER_REGIMEN
        defaultInfoLegalShouldBeFound("regimen.greaterThan=" + SMALLER_REGIMEN);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByResolucionPosIsEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where resolucionPos equals to DEFAULT_RESOLUCION_POS
        defaultInfoLegalShouldBeFound("resolucionPos.equals=" + DEFAULT_RESOLUCION_POS);

        // Get all the infoLegalList where resolucionPos equals to UPDATED_RESOLUCION_POS
        defaultInfoLegalShouldNotBeFound("resolucionPos.equals=" + UPDATED_RESOLUCION_POS);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByResolucionPosIsNotEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where resolucionPos not equals to DEFAULT_RESOLUCION_POS
        defaultInfoLegalShouldNotBeFound("resolucionPos.notEquals=" + DEFAULT_RESOLUCION_POS);

        // Get all the infoLegalList where resolucionPos not equals to UPDATED_RESOLUCION_POS
        defaultInfoLegalShouldBeFound("resolucionPos.notEquals=" + UPDATED_RESOLUCION_POS);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByResolucionPosIsInShouldWork() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where resolucionPos in DEFAULT_RESOLUCION_POS or UPDATED_RESOLUCION_POS
        defaultInfoLegalShouldBeFound("resolucionPos.in=" + DEFAULT_RESOLUCION_POS + "," + UPDATED_RESOLUCION_POS);

        // Get all the infoLegalList where resolucionPos equals to UPDATED_RESOLUCION_POS
        defaultInfoLegalShouldNotBeFound("resolucionPos.in=" + UPDATED_RESOLUCION_POS);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByResolucionPosIsNullOrNotNull() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where resolucionPos is not null
        defaultInfoLegalShouldBeFound("resolucionPos.specified=true");

        // Get all the infoLegalList where resolucionPos is null
        defaultInfoLegalShouldNotBeFound("resolucionPos.specified=false");
    }

    @Test
    @Transactional
    void getAllInfoLegalsByResolucionPosContainsSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where resolucionPos contains DEFAULT_RESOLUCION_POS
        defaultInfoLegalShouldBeFound("resolucionPos.contains=" + DEFAULT_RESOLUCION_POS);

        // Get all the infoLegalList where resolucionPos contains UPDATED_RESOLUCION_POS
        defaultInfoLegalShouldNotBeFound("resolucionPos.contains=" + UPDATED_RESOLUCION_POS);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByResolucionPosNotContainsSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where resolucionPos does not contain DEFAULT_RESOLUCION_POS
        defaultInfoLegalShouldNotBeFound("resolucionPos.doesNotContain=" + DEFAULT_RESOLUCION_POS);

        // Get all the infoLegalList where resolucionPos does not contain UPDATED_RESOLUCION_POS
        defaultInfoLegalShouldBeFound("resolucionPos.doesNotContain=" + UPDATED_RESOLUCION_POS);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoPosInicialIsEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoPosInicial equals to DEFAULT_PREFIJO_POS_INICIAL
        defaultInfoLegalShouldBeFound("prefijoPosInicial.equals=" + DEFAULT_PREFIJO_POS_INICIAL);

        // Get all the infoLegalList where prefijoPosInicial equals to UPDATED_PREFIJO_POS_INICIAL
        defaultInfoLegalShouldNotBeFound("prefijoPosInicial.equals=" + UPDATED_PREFIJO_POS_INICIAL);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoPosInicialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoPosInicial not equals to DEFAULT_PREFIJO_POS_INICIAL
        defaultInfoLegalShouldNotBeFound("prefijoPosInicial.notEquals=" + DEFAULT_PREFIJO_POS_INICIAL);

        // Get all the infoLegalList where prefijoPosInicial not equals to UPDATED_PREFIJO_POS_INICIAL
        defaultInfoLegalShouldBeFound("prefijoPosInicial.notEquals=" + UPDATED_PREFIJO_POS_INICIAL);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoPosInicialIsInShouldWork() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoPosInicial in DEFAULT_PREFIJO_POS_INICIAL or UPDATED_PREFIJO_POS_INICIAL
        defaultInfoLegalShouldBeFound("prefijoPosInicial.in=" + DEFAULT_PREFIJO_POS_INICIAL + "," + UPDATED_PREFIJO_POS_INICIAL);

        // Get all the infoLegalList where prefijoPosInicial equals to UPDATED_PREFIJO_POS_INICIAL
        defaultInfoLegalShouldNotBeFound("prefijoPosInicial.in=" + UPDATED_PREFIJO_POS_INICIAL);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoPosInicialIsNullOrNotNull() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoPosInicial is not null
        defaultInfoLegalShouldBeFound("prefijoPosInicial.specified=true");

        // Get all the infoLegalList where prefijoPosInicial is null
        defaultInfoLegalShouldNotBeFound("prefijoPosInicial.specified=false");
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoPosInicialIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoPosInicial is greater than or equal to DEFAULT_PREFIJO_POS_INICIAL
        defaultInfoLegalShouldBeFound("prefijoPosInicial.greaterThanOrEqual=" + DEFAULT_PREFIJO_POS_INICIAL);

        // Get all the infoLegalList where prefijoPosInicial is greater than or equal to UPDATED_PREFIJO_POS_INICIAL
        defaultInfoLegalShouldNotBeFound("prefijoPosInicial.greaterThanOrEqual=" + UPDATED_PREFIJO_POS_INICIAL);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoPosInicialIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoPosInicial is less than or equal to DEFAULT_PREFIJO_POS_INICIAL
        defaultInfoLegalShouldBeFound("prefijoPosInicial.lessThanOrEqual=" + DEFAULT_PREFIJO_POS_INICIAL);

        // Get all the infoLegalList where prefijoPosInicial is less than or equal to SMALLER_PREFIJO_POS_INICIAL
        defaultInfoLegalShouldNotBeFound("prefijoPosInicial.lessThanOrEqual=" + SMALLER_PREFIJO_POS_INICIAL);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoPosInicialIsLessThanSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoPosInicial is less than DEFAULT_PREFIJO_POS_INICIAL
        defaultInfoLegalShouldNotBeFound("prefijoPosInicial.lessThan=" + DEFAULT_PREFIJO_POS_INICIAL);

        // Get all the infoLegalList where prefijoPosInicial is less than UPDATED_PREFIJO_POS_INICIAL
        defaultInfoLegalShouldBeFound("prefijoPosInicial.lessThan=" + UPDATED_PREFIJO_POS_INICIAL);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoPosInicialIsGreaterThanSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoPosInicial is greater than DEFAULT_PREFIJO_POS_INICIAL
        defaultInfoLegalShouldNotBeFound("prefijoPosInicial.greaterThan=" + DEFAULT_PREFIJO_POS_INICIAL);

        // Get all the infoLegalList where prefijoPosInicial is greater than SMALLER_PREFIJO_POS_INICIAL
        defaultInfoLegalShouldBeFound("prefijoPosInicial.greaterThan=" + SMALLER_PREFIJO_POS_INICIAL);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoPosFinalIsEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoPosFinal equals to DEFAULT_PREFIJO_POS_FINAL
        defaultInfoLegalShouldBeFound("prefijoPosFinal.equals=" + DEFAULT_PREFIJO_POS_FINAL);

        // Get all the infoLegalList where prefijoPosFinal equals to UPDATED_PREFIJO_POS_FINAL
        defaultInfoLegalShouldNotBeFound("prefijoPosFinal.equals=" + UPDATED_PREFIJO_POS_FINAL);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoPosFinalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoPosFinal not equals to DEFAULT_PREFIJO_POS_FINAL
        defaultInfoLegalShouldNotBeFound("prefijoPosFinal.notEquals=" + DEFAULT_PREFIJO_POS_FINAL);

        // Get all the infoLegalList where prefijoPosFinal not equals to UPDATED_PREFIJO_POS_FINAL
        defaultInfoLegalShouldBeFound("prefijoPosFinal.notEquals=" + UPDATED_PREFIJO_POS_FINAL);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoPosFinalIsInShouldWork() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoPosFinal in DEFAULT_PREFIJO_POS_FINAL or UPDATED_PREFIJO_POS_FINAL
        defaultInfoLegalShouldBeFound("prefijoPosFinal.in=" + DEFAULT_PREFIJO_POS_FINAL + "," + UPDATED_PREFIJO_POS_FINAL);

        // Get all the infoLegalList where prefijoPosFinal equals to UPDATED_PREFIJO_POS_FINAL
        defaultInfoLegalShouldNotBeFound("prefijoPosFinal.in=" + UPDATED_PREFIJO_POS_FINAL);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoPosFinalIsNullOrNotNull() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoPosFinal is not null
        defaultInfoLegalShouldBeFound("prefijoPosFinal.specified=true");

        // Get all the infoLegalList where prefijoPosFinal is null
        defaultInfoLegalShouldNotBeFound("prefijoPosFinal.specified=false");
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoPosFinalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoPosFinal is greater than or equal to DEFAULT_PREFIJO_POS_FINAL
        defaultInfoLegalShouldBeFound("prefijoPosFinal.greaterThanOrEqual=" + DEFAULT_PREFIJO_POS_FINAL);

        // Get all the infoLegalList where prefijoPosFinal is greater than or equal to UPDATED_PREFIJO_POS_FINAL
        defaultInfoLegalShouldNotBeFound("prefijoPosFinal.greaterThanOrEqual=" + UPDATED_PREFIJO_POS_FINAL);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoPosFinalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoPosFinal is less than or equal to DEFAULT_PREFIJO_POS_FINAL
        defaultInfoLegalShouldBeFound("prefijoPosFinal.lessThanOrEqual=" + DEFAULT_PREFIJO_POS_FINAL);

        // Get all the infoLegalList where prefijoPosFinal is less than or equal to SMALLER_PREFIJO_POS_FINAL
        defaultInfoLegalShouldNotBeFound("prefijoPosFinal.lessThanOrEqual=" + SMALLER_PREFIJO_POS_FINAL);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoPosFinalIsLessThanSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoPosFinal is less than DEFAULT_PREFIJO_POS_FINAL
        defaultInfoLegalShouldNotBeFound("prefijoPosFinal.lessThan=" + DEFAULT_PREFIJO_POS_FINAL);

        // Get all the infoLegalList where prefijoPosFinal is less than UPDATED_PREFIJO_POS_FINAL
        defaultInfoLegalShouldBeFound("prefijoPosFinal.lessThan=" + UPDATED_PREFIJO_POS_FINAL);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoPosFinalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoPosFinal is greater than DEFAULT_PREFIJO_POS_FINAL
        defaultInfoLegalShouldNotBeFound("prefijoPosFinal.greaterThan=" + DEFAULT_PREFIJO_POS_FINAL);

        // Get all the infoLegalList where prefijoPosFinal is greater than SMALLER_PREFIJO_POS_FINAL
        defaultInfoLegalShouldBeFound("prefijoPosFinal.greaterThan=" + SMALLER_PREFIJO_POS_FINAL);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByResolucionFacElecIsEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where resolucionFacElec equals to DEFAULT_RESOLUCION_FAC_ELEC
        defaultInfoLegalShouldBeFound("resolucionFacElec.equals=" + DEFAULT_RESOLUCION_FAC_ELEC);

        // Get all the infoLegalList where resolucionFacElec equals to UPDATED_RESOLUCION_FAC_ELEC
        defaultInfoLegalShouldNotBeFound("resolucionFacElec.equals=" + UPDATED_RESOLUCION_FAC_ELEC);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByResolucionFacElecIsNotEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where resolucionFacElec not equals to DEFAULT_RESOLUCION_FAC_ELEC
        defaultInfoLegalShouldNotBeFound("resolucionFacElec.notEquals=" + DEFAULT_RESOLUCION_FAC_ELEC);

        // Get all the infoLegalList where resolucionFacElec not equals to UPDATED_RESOLUCION_FAC_ELEC
        defaultInfoLegalShouldBeFound("resolucionFacElec.notEquals=" + UPDATED_RESOLUCION_FAC_ELEC);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByResolucionFacElecIsInShouldWork() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where resolucionFacElec in DEFAULT_RESOLUCION_FAC_ELEC or UPDATED_RESOLUCION_FAC_ELEC
        defaultInfoLegalShouldBeFound("resolucionFacElec.in=" + DEFAULT_RESOLUCION_FAC_ELEC + "," + UPDATED_RESOLUCION_FAC_ELEC);

        // Get all the infoLegalList where resolucionFacElec equals to UPDATED_RESOLUCION_FAC_ELEC
        defaultInfoLegalShouldNotBeFound("resolucionFacElec.in=" + UPDATED_RESOLUCION_FAC_ELEC);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByResolucionFacElecIsNullOrNotNull() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where resolucionFacElec is not null
        defaultInfoLegalShouldBeFound("resolucionFacElec.specified=true");

        // Get all the infoLegalList where resolucionFacElec is null
        defaultInfoLegalShouldNotBeFound("resolucionFacElec.specified=false");
    }

    @Test
    @Transactional
    void getAllInfoLegalsByResolucionFacElecContainsSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where resolucionFacElec contains DEFAULT_RESOLUCION_FAC_ELEC
        defaultInfoLegalShouldBeFound("resolucionFacElec.contains=" + DEFAULT_RESOLUCION_FAC_ELEC);

        // Get all the infoLegalList where resolucionFacElec contains UPDATED_RESOLUCION_FAC_ELEC
        defaultInfoLegalShouldNotBeFound("resolucionFacElec.contains=" + UPDATED_RESOLUCION_FAC_ELEC);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByResolucionFacElecNotContainsSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where resolucionFacElec does not contain DEFAULT_RESOLUCION_FAC_ELEC
        defaultInfoLegalShouldNotBeFound("resolucionFacElec.doesNotContain=" + DEFAULT_RESOLUCION_FAC_ELEC);

        // Get all the infoLegalList where resolucionFacElec does not contain UPDATED_RESOLUCION_FAC_ELEC
        defaultInfoLegalShouldBeFound("resolucionFacElec.doesNotContain=" + UPDATED_RESOLUCION_FAC_ELEC);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoFacElecFinalIsEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoFacElecFinal equals to DEFAULT_PREFIJO_FAC_ELEC_FINAL
        defaultInfoLegalShouldBeFound("prefijoFacElecFinal.equals=" + DEFAULT_PREFIJO_FAC_ELEC_FINAL);

        // Get all the infoLegalList where prefijoFacElecFinal equals to UPDATED_PREFIJO_FAC_ELEC_FINAL
        defaultInfoLegalShouldNotBeFound("prefijoFacElecFinal.equals=" + UPDATED_PREFIJO_FAC_ELEC_FINAL);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoFacElecFinalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoFacElecFinal not equals to DEFAULT_PREFIJO_FAC_ELEC_FINAL
        defaultInfoLegalShouldNotBeFound("prefijoFacElecFinal.notEquals=" + DEFAULT_PREFIJO_FAC_ELEC_FINAL);

        // Get all the infoLegalList where prefijoFacElecFinal not equals to UPDATED_PREFIJO_FAC_ELEC_FINAL
        defaultInfoLegalShouldBeFound("prefijoFacElecFinal.notEquals=" + UPDATED_PREFIJO_FAC_ELEC_FINAL);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoFacElecFinalIsInShouldWork() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoFacElecFinal in DEFAULT_PREFIJO_FAC_ELEC_FINAL or UPDATED_PREFIJO_FAC_ELEC_FINAL
        defaultInfoLegalShouldBeFound("prefijoFacElecFinal.in=" + DEFAULT_PREFIJO_FAC_ELEC_FINAL + "," + UPDATED_PREFIJO_FAC_ELEC_FINAL);

        // Get all the infoLegalList where prefijoFacElecFinal equals to UPDATED_PREFIJO_FAC_ELEC_FINAL
        defaultInfoLegalShouldNotBeFound("prefijoFacElecFinal.in=" + UPDATED_PREFIJO_FAC_ELEC_FINAL);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoFacElecFinalIsNullOrNotNull() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoFacElecFinal is not null
        defaultInfoLegalShouldBeFound("prefijoFacElecFinal.specified=true");

        // Get all the infoLegalList where prefijoFacElecFinal is null
        defaultInfoLegalShouldNotBeFound("prefijoFacElecFinal.specified=false");
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoFacElecFinalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoFacElecFinal is greater than or equal to DEFAULT_PREFIJO_FAC_ELEC_FINAL
        defaultInfoLegalShouldBeFound("prefijoFacElecFinal.greaterThanOrEqual=" + DEFAULT_PREFIJO_FAC_ELEC_FINAL);

        // Get all the infoLegalList where prefijoFacElecFinal is greater than or equal to UPDATED_PREFIJO_FAC_ELEC_FINAL
        defaultInfoLegalShouldNotBeFound("prefijoFacElecFinal.greaterThanOrEqual=" + UPDATED_PREFIJO_FAC_ELEC_FINAL);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoFacElecFinalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoFacElecFinal is less than or equal to DEFAULT_PREFIJO_FAC_ELEC_FINAL
        defaultInfoLegalShouldBeFound("prefijoFacElecFinal.lessThanOrEqual=" + DEFAULT_PREFIJO_FAC_ELEC_FINAL);

        // Get all the infoLegalList where prefijoFacElecFinal is less than or equal to SMALLER_PREFIJO_FAC_ELEC_FINAL
        defaultInfoLegalShouldNotBeFound("prefijoFacElecFinal.lessThanOrEqual=" + SMALLER_PREFIJO_FAC_ELEC_FINAL);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoFacElecFinalIsLessThanSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoFacElecFinal is less than DEFAULT_PREFIJO_FAC_ELEC_FINAL
        defaultInfoLegalShouldNotBeFound("prefijoFacElecFinal.lessThan=" + DEFAULT_PREFIJO_FAC_ELEC_FINAL);

        // Get all the infoLegalList where prefijoFacElecFinal is less than UPDATED_PREFIJO_FAC_ELEC_FINAL
        defaultInfoLegalShouldBeFound("prefijoFacElecFinal.lessThan=" + UPDATED_PREFIJO_FAC_ELEC_FINAL);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByPrefijoFacElecFinalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where prefijoFacElecFinal is greater than DEFAULT_PREFIJO_FAC_ELEC_FINAL
        defaultInfoLegalShouldNotBeFound("prefijoFacElecFinal.greaterThan=" + DEFAULT_PREFIJO_FAC_ELEC_FINAL);

        // Get all the infoLegalList where prefijoFacElecFinal is greater than SMALLER_PREFIJO_FAC_ELEC_FINAL
        defaultInfoLegalShouldBeFound("prefijoFacElecFinal.greaterThan=" + SMALLER_PREFIJO_FAC_ELEC_FINAL);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByResolucionNomElecIsEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where resolucionNomElec equals to DEFAULT_RESOLUCION_NOM_ELEC
        defaultInfoLegalShouldBeFound("resolucionNomElec.equals=" + DEFAULT_RESOLUCION_NOM_ELEC);

        // Get all the infoLegalList where resolucionNomElec equals to UPDATED_RESOLUCION_NOM_ELEC
        defaultInfoLegalShouldNotBeFound("resolucionNomElec.equals=" + UPDATED_RESOLUCION_NOM_ELEC);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByResolucionNomElecIsNotEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where resolucionNomElec not equals to DEFAULT_RESOLUCION_NOM_ELEC
        defaultInfoLegalShouldNotBeFound("resolucionNomElec.notEquals=" + DEFAULT_RESOLUCION_NOM_ELEC);

        // Get all the infoLegalList where resolucionNomElec not equals to UPDATED_RESOLUCION_NOM_ELEC
        defaultInfoLegalShouldBeFound("resolucionNomElec.notEquals=" + UPDATED_RESOLUCION_NOM_ELEC);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByResolucionNomElecIsInShouldWork() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where resolucionNomElec in DEFAULT_RESOLUCION_NOM_ELEC or UPDATED_RESOLUCION_NOM_ELEC
        defaultInfoLegalShouldBeFound("resolucionNomElec.in=" + DEFAULT_RESOLUCION_NOM_ELEC + "," + UPDATED_RESOLUCION_NOM_ELEC);

        // Get all the infoLegalList where resolucionNomElec equals to UPDATED_RESOLUCION_NOM_ELEC
        defaultInfoLegalShouldNotBeFound("resolucionNomElec.in=" + UPDATED_RESOLUCION_NOM_ELEC);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByResolucionNomElecIsNullOrNotNull() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where resolucionNomElec is not null
        defaultInfoLegalShouldBeFound("resolucionNomElec.specified=true");

        // Get all the infoLegalList where resolucionNomElec is null
        defaultInfoLegalShouldNotBeFound("resolucionNomElec.specified=false");
    }

    @Test
    @Transactional
    void getAllInfoLegalsByResolucionNomElecContainsSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where resolucionNomElec contains DEFAULT_RESOLUCION_NOM_ELEC
        defaultInfoLegalShouldBeFound("resolucionNomElec.contains=" + DEFAULT_RESOLUCION_NOM_ELEC);

        // Get all the infoLegalList where resolucionNomElec contains UPDATED_RESOLUCION_NOM_ELEC
        defaultInfoLegalShouldNotBeFound("resolucionNomElec.contains=" + UPDATED_RESOLUCION_NOM_ELEC);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByResolucionNomElecNotContainsSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where resolucionNomElec does not contain DEFAULT_RESOLUCION_NOM_ELEC
        defaultInfoLegalShouldNotBeFound("resolucionNomElec.doesNotContain=" + DEFAULT_RESOLUCION_NOM_ELEC);

        // Get all the infoLegalList where resolucionNomElec does not contain UPDATED_RESOLUCION_NOM_ELEC
        defaultInfoLegalShouldBeFound("resolucionNomElec.doesNotContain=" + UPDATED_RESOLUCION_NOM_ELEC);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where estado equals to DEFAULT_ESTADO
        defaultInfoLegalShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the infoLegalList where estado equals to UPDATED_ESTADO
        defaultInfoLegalShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where estado not equals to DEFAULT_ESTADO
        defaultInfoLegalShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the infoLegalList where estado not equals to UPDATED_ESTADO
        defaultInfoLegalShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultInfoLegalShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the infoLegalList where estado equals to UPDATED_ESTADO
        defaultInfoLegalShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where estado is not null
        defaultInfoLegalShouldBeFound("estado.specified=true");

        // Get all the infoLegalList where estado is null
        defaultInfoLegalShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllInfoLegalsByEstadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where estado is greater than or equal to DEFAULT_ESTADO
        defaultInfoLegalShouldBeFound("estado.greaterThanOrEqual=" + DEFAULT_ESTADO);

        // Get all the infoLegalList where estado is greater than or equal to UPDATED_ESTADO
        defaultInfoLegalShouldNotBeFound("estado.greaterThanOrEqual=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByEstadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where estado is less than or equal to DEFAULT_ESTADO
        defaultInfoLegalShouldBeFound("estado.lessThanOrEqual=" + DEFAULT_ESTADO);

        // Get all the infoLegalList where estado is less than or equal to SMALLER_ESTADO
        defaultInfoLegalShouldNotBeFound("estado.lessThanOrEqual=" + SMALLER_ESTADO);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByEstadoIsLessThanSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where estado is less than DEFAULT_ESTADO
        defaultInfoLegalShouldNotBeFound("estado.lessThan=" + DEFAULT_ESTADO);

        // Get all the infoLegalList where estado is less than UPDATED_ESTADO
        defaultInfoLegalShouldBeFound("estado.lessThan=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByEstadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where estado is greater than DEFAULT_ESTADO
        defaultInfoLegalShouldNotBeFound("estado.greaterThan=" + DEFAULT_ESTADO);

        // Get all the infoLegalList where estado is greater than SMALLER_ESTADO
        defaultInfoLegalShouldBeFound("estado.greaterThan=" + SMALLER_ESTADO);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByFechaRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where fechaRegistro equals to DEFAULT_FECHA_REGISTRO
        defaultInfoLegalShouldBeFound("fechaRegistro.equals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the infoLegalList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultInfoLegalShouldNotBeFound("fechaRegistro.equals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByFechaRegistroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where fechaRegistro not equals to DEFAULT_FECHA_REGISTRO
        defaultInfoLegalShouldNotBeFound("fechaRegistro.notEquals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the infoLegalList where fechaRegistro not equals to UPDATED_FECHA_REGISTRO
        defaultInfoLegalShouldBeFound("fechaRegistro.notEquals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByFechaRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where fechaRegistro in DEFAULT_FECHA_REGISTRO or UPDATED_FECHA_REGISTRO
        defaultInfoLegalShouldBeFound("fechaRegistro.in=" + DEFAULT_FECHA_REGISTRO + "," + UPDATED_FECHA_REGISTRO);

        // Get all the infoLegalList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultInfoLegalShouldNotBeFound("fechaRegistro.in=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllInfoLegalsByFechaRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        // Get all the infoLegalList where fechaRegistro is not null
        defaultInfoLegalShouldBeFound("fechaRegistro.specified=true");

        // Get all the infoLegalList where fechaRegistro is null
        defaultInfoLegalShouldNotBeFound("fechaRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllInfoLegalsByEmpresaIdIsEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);
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
        infoLegal.addEmpresaId(empresaId);
        infoLegalRepository.saveAndFlush(infoLegal);
        Long empresaIdId = empresaId.getId();

        // Get all the infoLegalList where empresaId equals to empresaIdId
        defaultInfoLegalShouldBeFound("empresaIdId.equals=" + empresaIdId);

        // Get all the infoLegalList where empresaId equals to (empresaIdId + 1)
        defaultInfoLegalShouldNotBeFound("empresaIdId.equals=" + (empresaIdId + 1));
    }

    @Test
    @Transactional
    void getAllInfoLegalsBySucursalIsEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);
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
        infoLegal.addSucursal(sucursal);
        infoLegalRepository.saveAndFlush(infoLegal);
        Long sucursalId = sucursal.getId();

        // Get all the infoLegalList where sucursal equals to sucursalId
        defaultInfoLegalShouldBeFound("sucursalId.equals=" + sucursalId);

        // Get all the infoLegalList where sucursal equals to (sucursalId + 1)
        defaultInfoLegalShouldNotBeFound("sucursalId.equals=" + (sucursalId + 1));
    }

    @Test
    @Transactional
    void getAllInfoLegalsByUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);
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
        infoLegal.setUsuario(usuario);
        infoLegalRepository.saveAndFlush(infoLegal);
        Long usuarioId = usuario.getId();

        // Get all the infoLegalList where usuario equals to usuarioId
        defaultInfoLegalShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the infoLegalList where usuario equals to (usuarioId + 1)
        defaultInfoLegalShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInfoLegalShouldBeFound(String filter) throws Exception {
        restInfoLegalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(infoLegal.getId().intValue())))
            .andExpect(jsonPath("$.[*].nit").value(hasItem(DEFAULT_NIT)))
            .andExpect(jsonPath("$.[*].regimen").value(hasItem(DEFAULT_REGIMEN)))
            .andExpect(jsonPath("$.[*].resolucionPos").value(hasItem(DEFAULT_RESOLUCION_POS)))
            .andExpect(jsonPath("$.[*].prefijoPosInicial").value(hasItem(DEFAULT_PREFIJO_POS_INICIAL)))
            .andExpect(jsonPath("$.[*].prefijoPosFinal").value(hasItem(DEFAULT_PREFIJO_POS_FINAL)))
            .andExpect(jsonPath("$.[*].resolucionFacElec").value(hasItem(DEFAULT_RESOLUCION_FAC_ELEC)))
            .andExpect(jsonPath("$.[*].prefijoFacElecFinal").value(hasItem(DEFAULT_PREFIJO_FAC_ELEC_FINAL)))
            .andExpect(jsonPath("$.[*].resolucionNomElec").value(hasItem(DEFAULT_RESOLUCION_NOM_ELEC)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));

        // Check, that the count call also returns 1
        restInfoLegalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInfoLegalShouldNotBeFound(String filter) throws Exception {
        restInfoLegalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInfoLegalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInfoLegal() throws Exception {
        // Get the infoLegal
        restInfoLegalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInfoLegal() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        int databaseSizeBeforeUpdate = infoLegalRepository.findAll().size();

        // Update the infoLegal
        InfoLegal updatedInfoLegal = infoLegalRepository.findById(infoLegal.getId()).get();
        // Disconnect from session so that the updates on updatedInfoLegal are not directly saved in db
        em.detach(updatedInfoLegal);
        updatedInfoLegal
            .nit(UPDATED_NIT)
            .regimen(UPDATED_REGIMEN)
            .resolucionPos(UPDATED_RESOLUCION_POS)
            .prefijoPosInicial(UPDATED_PREFIJO_POS_INICIAL)
            .prefijoPosFinal(UPDATED_PREFIJO_POS_FINAL)
            .resolucionFacElec(UPDATED_RESOLUCION_FAC_ELEC)
            .prefijoFacElecFinal(UPDATED_PREFIJO_FAC_ELEC_FINAL)
            .resolucionNomElec(UPDATED_RESOLUCION_NOM_ELEC)
            .estado(UPDATED_ESTADO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restInfoLegalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInfoLegal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInfoLegal))
            )
            .andExpect(status().isOk());

        // Validate the InfoLegal in the database
        List<InfoLegal> infoLegalList = infoLegalRepository.findAll();
        assertThat(infoLegalList).hasSize(databaseSizeBeforeUpdate);
        InfoLegal testInfoLegal = infoLegalList.get(infoLegalList.size() - 1);
        assertThat(testInfoLegal.getNit()).isEqualTo(UPDATED_NIT);
        assertThat(testInfoLegal.getRegimen()).isEqualTo(UPDATED_REGIMEN);
        assertThat(testInfoLegal.getResolucionPos()).isEqualTo(UPDATED_RESOLUCION_POS);
        assertThat(testInfoLegal.getPrefijoPosInicial()).isEqualTo(UPDATED_PREFIJO_POS_INICIAL);
        assertThat(testInfoLegal.getPrefijoPosFinal()).isEqualTo(UPDATED_PREFIJO_POS_FINAL);
        assertThat(testInfoLegal.getResolucionFacElec()).isEqualTo(UPDATED_RESOLUCION_FAC_ELEC);
        assertThat(testInfoLegal.getPrefijoFacElecFinal()).isEqualTo(UPDATED_PREFIJO_FAC_ELEC_FINAL);
        assertThat(testInfoLegal.getResolucionNomElec()).isEqualTo(UPDATED_RESOLUCION_NOM_ELEC);
        assertThat(testInfoLegal.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testInfoLegal.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingInfoLegal() throws Exception {
        int databaseSizeBeforeUpdate = infoLegalRepository.findAll().size();
        infoLegal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInfoLegalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, infoLegal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(infoLegal))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfoLegal in the database
        List<InfoLegal> infoLegalList = infoLegalRepository.findAll();
        assertThat(infoLegalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInfoLegal() throws Exception {
        int databaseSizeBeforeUpdate = infoLegalRepository.findAll().size();
        infoLegal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfoLegalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(infoLegal))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfoLegal in the database
        List<InfoLegal> infoLegalList = infoLegalRepository.findAll();
        assertThat(infoLegalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInfoLegal() throws Exception {
        int databaseSizeBeforeUpdate = infoLegalRepository.findAll().size();
        infoLegal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfoLegalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infoLegal)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InfoLegal in the database
        List<InfoLegal> infoLegalList = infoLegalRepository.findAll();
        assertThat(infoLegalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInfoLegalWithPatch() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        int databaseSizeBeforeUpdate = infoLegalRepository.findAll().size();

        // Update the infoLegal using partial update
        InfoLegal partialUpdatedInfoLegal = new InfoLegal();
        partialUpdatedInfoLegal.setId(infoLegal.getId());

        partialUpdatedInfoLegal.nit(UPDATED_NIT).regimen(UPDATED_REGIMEN).prefijoPosFinal(UPDATED_PREFIJO_POS_FINAL).estado(UPDATED_ESTADO);

        restInfoLegalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInfoLegal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInfoLegal))
            )
            .andExpect(status().isOk());

        // Validate the InfoLegal in the database
        List<InfoLegal> infoLegalList = infoLegalRepository.findAll();
        assertThat(infoLegalList).hasSize(databaseSizeBeforeUpdate);
        InfoLegal testInfoLegal = infoLegalList.get(infoLegalList.size() - 1);
        assertThat(testInfoLegal.getNit()).isEqualTo(UPDATED_NIT);
        assertThat(testInfoLegal.getRegimen()).isEqualTo(UPDATED_REGIMEN);
        assertThat(testInfoLegal.getResolucionPos()).isEqualTo(DEFAULT_RESOLUCION_POS);
        assertThat(testInfoLegal.getPrefijoPosInicial()).isEqualTo(DEFAULT_PREFIJO_POS_INICIAL);
        assertThat(testInfoLegal.getPrefijoPosFinal()).isEqualTo(UPDATED_PREFIJO_POS_FINAL);
        assertThat(testInfoLegal.getResolucionFacElec()).isEqualTo(DEFAULT_RESOLUCION_FAC_ELEC);
        assertThat(testInfoLegal.getPrefijoFacElecFinal()).isEqualTo(DEFAULT_PREFIJO_FAC_ELEC_FINAL);
        assertThat(testInfoLegal.getResolucionNomElec()).isEqualTo(DEFAULT_RESOLUCION_NOM_ELEC);
        assertThat(testInfoLegal.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testInfoLegal.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdateInfoLegalWithPatch() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        int databaseSizeBeforeUpdate = infoLegalRepository.findAll().size();

        // Update the infoLegal using partial update
        InfoLegal partialUpdatedInfoLegal = new InfoLegal();
        partialUpdatedInfoLegal.setId(infoLegal.getId());

        partialUpdatedInfoLegal
            .nit(UPDATED_NIT)
            .regimen(UPDATED_REGIMEN)
            .resolucionPos(UPDATED_RESOLUCION_POS)
            .prefijoPosInicial(UPDATED_PREFIJO_POS_INICIAL)
            .prefijoPosFinal(UPDATED_PREFIJO_POS_FINAL)
            .resolucionFacElec(UPDATED_RESOLUCION_FAC_ELEC)
            .prefijoFacElecFinal(UPDATED_PREFIJO_FAC_ELEC_FINAL)
            .resolucionNomElec(UPDATED_RESOLUCION_NOM_ELEC)
            .estado(UPDATED_ESTADO)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restInfoLegalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInfoLegal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInfoLegal))
            )
            .andExpect(status().isOk());

        // Validate the InfoLegal in the database
        List<InfoLegal> infoLegalList = infoLegalRepository.findAll();
        assertThat(infoLegalList).hasSize(databaseSizeBeforeUpdate);
        InfoLegal testInfoLegal = infoLegalList.get(infoLegalList.size() - 1);
        assertThat(testInfoLegal.getNit()).isEqualTo(UPDATED_NIT);
        assertThat(testInfoLegal.getRegimen()).isEqualTo(UPDATED_REGIMEN);
        assertThat(testInfoLegal.getResolucionPos()).isEqualTo(UPDATED_RESOLUCION_POS);
        assertThat(testInfoLegal.getPrefijoPosInicial()).isEqualTo(UPDATED_PREFIJO_POS_INICIAL);
        assertThat(testInfoLegal.getPrefijoPosFinal()).isEqualTo(UPDATED_PREFIJO_POS_FINAL);
        assertThat(testInfoLegal.getResolucionFacElec()).isEqualTo(UPDATED_RESOLUCION_FAC_ELEC);
        assertThat(testInfoLegal.getPrefijoFacElecFinal()).isEqualTo(UPDATED_PREFIJO_FAC_ELEC_FINAL);
        assertThat(testInfoLegal.getResolucionNomElec()).isEqualTo(UPDATED_RESOLUCION_NOM_ELEC);
        assertThat(testInfoLegal.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testInfoLegal.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingInfoLegal() throws Exception {
        int databaseSizeBeforeUpdate = infoLegalRepository.findAll().size();
        infoLegal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInfoLegalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, infoLegal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(infoLegal))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfoLegal in the database
        List<InfoLegal> infoLegalList = infoLegalRepository.findAll();
        assertThat(infoLegalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInfoLegal() throws Exception {
        int databaseSizeBeforeUpdate = infoLegalRepository.findAll().size();
        infoLegal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfoLegalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(infoLegal))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfoLegal in the database
        List<InfoLegal> infoLegalList = infoLegalRepository.findAll();
        assertThat(infoLegalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInfoLegal() throws Exception {
        int databaseSizeBeforeUpdate = infoLegalRepository.findAll().size();
        infoLegal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfoLegalMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(infoLegal))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InfoLegal in the database
        List<InfoLegal> infoLegalList = infoLegalRepository.findAll();
        assertThat(infoLegalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInfoLegal() throws Exception {
        // Initialize the database
        infoLegalRepository.saveAndFlush(infoLegal);

        int databaseSizeBeforeDelete = infoLegalRepository.findAll().size();

        // Delete the infoLegal
        restInfoLegalMockMvc
            .perform(delete(ENTITY_API_URL_ID, infoLegal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InfoLegal> infoLegalList = infoLegalRepository.findAll();
        assertThat(infoLegalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
