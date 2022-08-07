package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Precio;
import com.mycompany.myapp.domain.Producto;
import com.mycompany.myapp.repository.PrecioRepository;
import com.mycompany.myapp.service.criteria.PrecioCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PrecioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PrecioResourceIT {

    private static final Integer DEFAULT_VALOR = 1;
    private static final Integer UPDATED_VALOR = 2;
    private static final Integer SMALLER_VALOR = 1 - 1;

    private static final String DEFAULT_DETALLE = "AAAAAAAAAA";
    private static final String UPDATED_DETALLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_REGISTRO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_REGISTRO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_REGISTRO = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/precios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrecioRepository precioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrecioMockMvc;

    private Precio precio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Precio createEntity(EntityManager em) {
        Precio precio = new Precio().valor(DEFAULT_VALOR).detalle(DEFAULT_DETALLE).fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return precio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Precio createUpdatedEntity(EntityManager em) {
        Precio precio = new Precio().valor(UPDATED_VALOR).detalle(UPDATED_DETALLE).fechaRegistro(UPDATED_FECHA_REGISTRO);
        return precio;
    }

    @BeforeEach
    public void initTest() {
        precio = createEntity(em);
    }

    @Test
    @Transactional
    void createPrecio() throws Exception {
        int databaseSizeBeforeCreate = precioRepository.findAll().size();
        // Create the Precio
        restPrecioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(precio)))
            .andExpect(status().isCreated());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeCreate + 1);
        Precio testPrecio = precioList.get(precioList.size() - 1);
        assertThat(testPrecio.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testPrecio.getDetalle()).isEqualTo(DEFAULT_DETALLE);
        assertThat(testPrecio.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createPrecioWithExistingId() throws Exception {
        // Create the Precio with an existing ID
        precio.setId(1L);

        int databaseSizeBeforeCreate = precioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrecioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(precio)))
            .andExpect(status().isBadRequest());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPrecios() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        // Get all the precioList
        restPrecioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(precio.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.[*].detalle").value(hasItem(DEFAULT_DETALLE.toString())))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @Test
    @Transactional
    void getPrecio() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        // Get the precio
        restPrecioMockMvc
            .perform(get(ENTITY_API_URL_ID, precio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(precio.getId().intValue()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR))
            .andExpect(jsonPath("$.detalle").value(DEFAULT_DETALLE.toString()))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getPreciosByIdFiltering() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        Long id = precio.getId();

        defaultPrecioShouldBeFound("id.equals=" + id);
        defaultPrecioShouldNotBeFound("id.notEquals=" + id);

        defaultPrecioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPrecioShouldNotBeFound("id.greaterThan=" + id);

        defaultPrecioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPrecioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPreciosByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        // Get all the precioList where valor equals to DEFAULT_VALOR
        defaultPrecioShouldBeFound("valor.equals=" + DEFAULT_VALOR);

        // Get all the precioList where valor equals to UPDATED_VALOR
        defaultPrecioShouldNotBeFound("valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllPreciosByValorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        // Get all the precioList where valor not equals to DEFAULT_VALOR
        defaultPrecioShouldNotBeFound("valor.notEquals=" + DEFAULT_VALOR);

        // Get all the precioList where valor not equals to UPDATED_VALOR
        defaultPrecioShouldBeFound("valor.notEquals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllPreciosByValorIsInShouldWork() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        // Get all the precioList where valor in DEFAULT_VALOR or UPDATED_VALOR
        defaultPrecioShouldBeFound("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR);

        // Get all the precioList where valor equals to UPDATED_VALOR
        defaultPrecioShouldNotBeFound("valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllPreciosByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        // Get all the precioList where valor is not null
        defaultPrecioShouldBeFound("valor.specified=true");

        // Get all the precioList where valor is null
        defaultPrecioShouldNotBeFound("valor.specified=false");
    }

    @Test
    @Transactional
    void getAllPreciosByValorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        // Get all the precioList where valor is greater than or equal to DEFAULT_VALOR
        defaultPrecioShouldBeFound("valor.greaterThanOrEqual=" + DEFAULT_VALOR);

        // Get all the precioList where valor is greater than or equal to UPDATED_VALOR
        defaultPrecioShouldNotBeFound("valor.greaterThanOrEqual=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllPreciosByValorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        // Get all the precioList where valor is less than or equal to DEFAULT_VALOR
        defaultPrecioShouldBeFound("valor.lessThanOrEqual=" + DEFAULT_VALOR);

        // Get all the precioList where valor is less than or equal to SMALLER_VALOR
        defaultPrecioShouldNotBeFound("valor.lessThanOrEqual=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllPreciosByValorIsLessThanSomething() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        // Get all the precioList where valor is less than DEFAULT_VALOR
        defaultPrecioShouldNotBeFound("valor.lessThan=" + DEFAULT_VALOR);

        // Get all the precioList where valor is less than UPDATED_VALOR
        defaultPrecioShouldBeFound("valor.lessThan=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllPreciosByValorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        // Get all the precioList where valor is greater than DEFAULT_VALOR
        defaultPrecioShouldNotBeFound("valor.greaterThan=" + DEFAULT_VALOR);

        // Get all the precioList where valor is greater than SMALLER_VALOR
        defaultPrecioShouldBeFound("valor.greaterThan=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllPreciosByFechaRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        // Get all the precioList where fechaRegistro equals to DEFAULT_FECHA_REGISTRO
        defaultPrecioShouldBeFound("fechaRegistro.equals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the precioList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultPrecioShouldNotBeFound("fechaRegistro.equals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllPreciosByFechaRegistroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        // Get all the precioList where fechaRegistro not equals to DEFAULT_FECHA_REGISTRO
        defaultPrecioShouldNotBeFound("fechaRegistro.notEquals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the precioList where fechaRegistro not equals to UPDATED_FECHA_REGISTRO
        defaultPrecioShouldBeFound("fechaRegistro.notEquals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllPreciosByFechaRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        // Get all the precioList where fechaRegistro in DEFAULT_FECHA_REGISTRO or UPDATED_FECHA_REGISTRO
        defaultPrecioShouldBeFound("fechaRegistro.in=" + DEFAULT_FECHA_REGISTRO + "," + UPDATED_FECHA_REGISTRO);

        // Get all the precioList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultPrecioShouldNotBeFound("fechaRegistro.in=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllPreciosByFechaRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        // Get all the precioList where fechaRegistro is not null
        defaultPrecioShouldBeFound("fechaRegistro.specified=true");

        // Get all the precioList where fechaRegistro is null
        defaultPrecioShouldNotBeFound("fechaRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllPreciosByFechaRegistroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        // Get all the precioList where fechaRegistro is greater than or equal to DEFAULT_FECHA_REGISTRO
        defaultPrecioShouldBeFound("fechaRegistro.greaterThanOrEqual=" + DEFAULT_FECHA_REGISTRO);

        // Get all the precioList where fechaRegistro is greater than or equal to UPDATED_FECHA_REGISTRO
        defaultPrecioShouldNotBeFound("fechaRegistro.greaterThanOrEqual=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllPreciosByFechaRegistroIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        // Get all the precioList where fechaRegistro is less than or equal to DEFAULT_FECHA_REGISTRO
        defaultPrecioShouldBeFound("fechaRegistro.lessThanOrEqual=" + DEFAULT_FECHA_REGISTRO);

        // Get all the precioList where fechaRegistro is less than or equal to SMALLER_FECHA_REGISTRO
        defaultPrecioShouldNotBeFound("fechaRegistro.lessThanOrEqual=" + SMALLER_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllPreciosByFechaRegistroIsLessThanSomething() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        // Get all the precioList where fechaRegistro is less than DEFAULT_FECHA_REGISTRO
        defaultPrecioShouldNotBeFound("fechaRegistro.lessThan=" + DEFAULT_FECHA_REGISTRO);

        // Get all the precioList where fechaRegistro is less than UPDATED_FECHA_REGISTRO
        defaultPrecioShouldBeFound("fechaRegistro.lessThan=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllPreciosByFechaRegistroIsGreaterThanSomething() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        // Get all the precioList where fechaRegistro is greater than DEFAULT_FECHA_REGISTRO
        defaultPrecioShouldNotBeFound("fechaRegistro.greaterThan=" + DEFAULT_FECHA_REGISTRO);

        // Get all the precioList where fechaRegistro is greater than SMALLER_FECHA_REGISTRO
        defaultPrecioShouldBeFound("fechaRegistro.greaterThan=" + SMALLER_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllPreciosByProductoIngresoIsEqualToSomething() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);
        Producto productoIngreso;
        if (TestUtil.findAll(em, Producto.class).isEmpty()) {
            productoIngreso = ProductoResourceIT.createEntity(em);
            em.persist(productoIngreso);
            em.flush();
        } else {
            productoIngreso = TestUtil.findAll(em, Producto.class).get(0);
        }
        em.persist(productoIngreso);
        em.flush();
        precio.addProductoIngreso(productoIngreso);
        precioRepository.saveAndFlush(precio);
        Long productoIngresoId = productoIngreso.getId();

        // Get all the precioList where productoIngreso equals to productoIngresoId
        defaultPrecioShouldBeFound("productoIngresoId.equals=" + productoIngresoId);

        // Get all the precioList where productoIngreso equals to (productoIngresoId + 1)
        defaultPrecioShouldNotBeFound("productoIngresoId.equals=" + (productoIngresoId + 1));
    }

    @Test
    @Transactional
    void getAllPreciosByProductoSalidaIsEqualToSomething() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);
        Producto productoSalida;
        if (TestUtil.findAll(em, Producto.class).isEmpty()) {
            productoSalida = ProductoResourceIT.createEntity(em);
            em.persist(productoSalida);
            em.flush();
        } else {
            productoSalida = TestUtil.findAll(em, Producto.class).get(0);
        }
        em.persist(productoSalida);
        em.flush();
        precio.addProductoSalida(productoSalida);
        precioRepository.saveAndFlush(precio);
        Long productoSalidaId = productoSalida.getId();

        // Get all the precioList where productoSalida equals to productoSalidaId
        defaultPrecioShouldBeFound("productoSalidaId.equals=" + productoSalidaId);

        // Get all the precioList where productoSalida equals to (productoSalidaId + 1)
        defaultPrecioShouldNotBeFound("productoSalidaId.equals=" + (productoSalidaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPrecioShouldBeFound(String filter) throws Exception {
        restPrecioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(precio.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.[*].detalle").value(hasItem(DEFAULT_DETALLE.toString())))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));

        // Check, that the count call also returns 1
        restPrecioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPrecioShouldNotBeFound(String filter) throws Exception {
        restPrecioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPrecioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPrecio() throws Exception {
        // Get the precio
        restPrecioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrecio() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        int databaseSizeBeforeUpdate = precioRepository.findAll().size();

        // Update the precio
        Precio updatedPrecio = precioRepository.findById(precio.getId()).get();
        // Disconnect from session so that the updates on updatedPrecio are not directly saved in db
        em.detach(updatedPrecio);
        updatedPrecio.valor(UPDATED_VALOR).detalle(UPDATED_DETALLE).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restPrecioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPrecio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPrecio))
            )
            .andExpect(status().isOk());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeUpdate);
        Precio testPrecio = precioList.get(precioList.size() - 1);
        assertThat(testPrecio.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testPrecio.getDetalle()).isEqualTo(UPDATED_DETALLE);
        assertThat(testPrecio.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingPrecio() throws Exception {
        int databaseSizeBeforeUpdate = precioRepository.findAll().size();
        precio.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrecioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, precio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(precio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrecio() throws Exception {
        int databaseSizeBeforeUpdate = precioRepository.findAll().size();
        precio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrecioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(precio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrecio() throws Exception {
        int databaseSizeBeforeUpdate = precioRepository.findAll().size();
        precio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrecioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(precio)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrecioWithPatch() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        int databaseSizeBeforeUpdate = precioRepository.findAll().size();

        // Update the precio using partial update
        Precio partialUpdatedPrecio = new Precio();
        partialUpdatedPrecio.setId(precio.getId());

        partialUpdatedPrecio.valor(UPDATED_VALOR);

        restPrecioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrecio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrecio))
            )
            .andExpect(status().isOk());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeUpdate);
        Precio testPrecio = precioList.get(precioList.size() - 1);
        assertThat(testPrecio.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testPrecio.getDetalle()).isEqualTo(DEFAULT_DETALLE);
        assertThat(testPrecio.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdatePrecioWithPatch() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        int databaseSizeBeforeUpdate = precioRepository.findAll().size();

        // Update the precio using partial update
        Precio partialUpdatedPrecio = new Precio();
        partialUpdatedPrecio.setId(precio.getId());

        partialUpdatedPrecio.valor(UPDATED_VALOR).detalle(UPDATED_DETALLE).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restPrecioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrecio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrecio))
            )
            .andExpect(status().isOk());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeUpdate);
        Precio testPrecio = precioList.get(precioList.size() - 1);
        assertThat(testPrecio.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testPrecio.getDetalle()).isEqualTo(UPDATED_DETALLE);
        assertThat(testPrecio.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingPrecio() throws Exception {
        int databaseSizeBeforeUpdate = precioRepository.findAll().size();
        precio.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrecioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, precio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(precio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrecio() throws Exception {
        int databaseSizeBeforeUpdate = precioRepository.findAll().size();
        precio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrecioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(precio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrecio() throws Exception {
        int databaseSizeBeforeUpdate = precioRepository.findAll().size();
        precio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrecioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(precio)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Precio in the database
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrecio() throws Exception {
        // Initialize the database
        precioRepository.saveAndFlush(precio);

        int databaseSizeBeforeDelete = precioRepository.findAll().size();

        // Delete the precio
        restPrecioMockMvc
            .perform(delete(ENTITY_API_URL_ID, precio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Precio> precioList = precioRepository.findAll();
        assertThat(precioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
