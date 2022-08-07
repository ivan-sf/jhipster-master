package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Codigo;
import com.mycompany.myapp.domain.Producto;
import com.mycompany.myapp.repository.CodigoRepository;
import com.mycompany.myapp.service.criteria.CodigoCriteria;
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
 * Integration tests for the {@link CodigoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CodigoResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_DETALLE = "AAAAAAAAAA";
    private static final String UPDATED_DETALLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_REGISTRO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_REGISTRO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_REGISTRO = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/codigos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CodigoRepository codigoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCodigoMockMvc;

    private Codigo codigo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Codigo createEntity(EntityManager em) {
        Codigo codigo = new Codigo().codigo(DEFAULT_CODIGO).detalle(DEFAULT_DETALLE).fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return codigo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Codigo createUpdatedEntity(EntityManager em) {
        Codigo codigo = new Codigo().codigo(UPDATED_CODIGO).detalle(UPDATED_DETALLE).fechaRegistro(UPDATED_FECHA_REGISTRO);
        return codigo;
    }

    @BeforeEach
    public void initTest() {
        codigo = createEntity(em);
    }

    @Test
    @Transactional
    void createCodigo() throws Exception {
        int databaseSizeBeforeCreate = codigoRepository.findAll().size();
        // Create the Codigo
        restCodigoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codigo)))
            .andExpect(status().isCreated());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeCreate + 1);
        Codigo testCodigo = codigoList.get(codigoList.size() - 1);
        assertThat(testCodigo.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testCodigo.getDetalle()).isEqualTo(DEFAULT_DETALLE);
        assertThat(testCodigo.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createCodigoWithExistingId() throws Exception {
        // Create the Codigo with an existing ID
        codigo.setId(1L);

        int databaseSizeBeforeCreate = codigoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCodigoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codigo)))
            .andExpect(status().isBadRequest());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCodigos() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        // Get all the codigoList
        restCodigoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(codigo.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].detalle").value(hasItem(DEFAULT_DETALLE.toString())))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @Test
    @Transactional
    void getCodigo() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        // Get the codigo
        restCodigoMockMvc
            .perform(get(ENTITY_API_URL_ID, codigo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(codigo.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.detalle").value(DEFAULT_DETALLE.toString()))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getCodigosByIdFiltering() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        Long id = codigo.getId();

        defaultCodigoShouldBeFound("id.equals=" + id);
        defaultCodigoShouldNotBeFound("id.notEquals=" + id);

        defaultCodigoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCodigoShouldNotBeFound("id.greaterThan=" + id);

        defaultCodigoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCodigoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCodigosByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        // Get all the codigoList where codigo equals to DEFAULT_CODIGO
        defaultCodigoShouldBeFound("codigo.equals=" + DEFAULT_CODIGO);

        // Get all the codigoList where codigo equals to UPDATED_CODIGO
        defaultCodigoShouldNotBeFound("codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllCodigosByCodigoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        // Get all the codigoList where codigo not equals to DEFAULT_CODIGO
        defaultCodigoShouldNotBeFound("codigo.notEquals=" + DEFAULT_CODIGO);

        // Get all the codigoList where codigo not equals to UPDATED_CODIGO
        defaultCodigoShouldBeFound("codigo.notEquals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllCodigosByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        // Get all the codigoList where codigo in DEFAULT_CODIGO or UPDATED_CODIGO
        defaultCodigoShouldBeFound("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO);

        // Get all the codigoList where codigo equals to UPDATED_CODIGO
        defaultCodigoShouldNotBeFound("codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllCodigosByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        // Get all the codigoList where codigo is not null
        defaultCodigoShouldBeFound("codigo.specified=true");

        // Get all the codigoList where codigo is null
        defaultCodigoShouldNotBeFound("codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllCodigosByCodigoContainsSomething() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        // Get all the codigoList where codigo contains DEFAULT_CODIGO
        defaultCodigoShouldBeFound("codigo.contains=" + DEFAULT_CODIGO);

        // Get all the codigoList where codigo contains UPDATED_CODIGO
        defaultCodigoShouldNotBeFound("codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllCodigosByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        // Get all the codigoList where codigo does not contain DEFAULT_CODIGO
        defaultCodigoShouldNotBeFound("codigo.doesNotContain=" + DEFAULT_CODIGO);

        // Get all the codigoList where codigo does not contain UPDATED_CODIGO
        defaultCodigoShouldBeFound("codigo.doesNotContain=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllCodigosByFechaRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        // Get all the codigoList where fechaRegistro equals to DEFAULT_FECHA_REGISTRO
        defaultCodigoShouldBeFound("fechaRegistro.equals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the codigoList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultCodigoShouldNotBeFound("fechaRegistro.equals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllCodigosByFechaRegistroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        // Get all the codigoList where fechaRegistro not equals to DEFAULT_FECHA_REGISTRO
        defaultCodigoShouldNotBeFound("fechaRegistro.notEquals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the codigoList where fechaRegistro not equals to UPDATED_FECHA_REGISTRO
        defaultCodigoShouldBeFound("fechaRegistro.notEquals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllCodigosByFechaRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        // Get all the codigoList where fechaRegistro in DEFAULT_FECHA_REGISTRO or UPDATED_FECHA_REGISTRO
        defaultCodigoShouldBeFound("fechaRegistro.in=" + DEFAULT_FECHA_REGISTRO + "," + UPDATED_FECHA_REGISTRO);

        // Get all the codigoList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultCodigoShouldNotBeFound("fechaRegistro.in=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllCodigosByFechaRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        // Get all the codigoList where fechaRegistro is not null
        defaultCodigoShouldBeFound("fechaRegistro.specified=true");

        // Get all the codigoList where fechaRegistro is null
        defaultCodigoShouldNotBeFound("fechaRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllCodigosByFechaRegistroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        // Get all the codigoList where fechaRegistro is greater than or equal to DEFAULT_FECHA_REGISTRO
        defaultCodigoShouldBeFound("fechaRegistro.greaterThanOrEqual=" + DEFAULT_FECHA_REGISTRO);

        // Get all the codigoList where fechaRegistro is greater than or equal to UPDATED_FECHA_REGISTRO
        defaultCodigoShouldNotBeFound("fechaRegistro.greaterThanOrEqual=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllCodigosByFechaRegistroIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        // Get all the codigoList where fechaRegistro is less than or equal to DEFAULT_FECHA_REGISTRO
        defaultCodigoShouldBeFound("fechaRegistro.lessThanOrEqual=" + DEFAULT_FECHA_REGISTRO);

        // Get all the codigoList where fechaRegistro is less than or equal to SMALLER_FECHA_REGISTRO
        defaultCodigoShouldNotBeFound("fechaRegistro.lessThanOrEqual=" + SMALLER_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllCodigosByFechaRegistroIsLessThanSomething() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        // Get all the codigoList where fechaRegistro is less than DEFAULT_FECHA_REGISTRO
        defaultCodigoShouldNotBeFound("fechaRegistro.lessThan=" + DEFAULT_FECHA_REGISTRO);

        // Get all the codigoList where fechaRegistro is less than UPDATED_FECHA_REGISTRO
        defaultCodigoShouldBeFound("fechaRegistro.lessThan=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllCodigosByFechaRegistroIsGreaterThanSomething() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        // Get all the codigoList where fechaRegistro is greater than DEFAULT_FECHA_REGISTRO
        defaultCodigoShouldNotBeFound("fechaRegistro.greaterThan=" + DEFAULT_FECHA_REGISTRO);

        // Get all the codigoList where fechaRegistro is greater than SMALLER_FECHA_REGISTRO
        defaultCodigoShouldBeFound("fechaRegistro.greaterThan=" + SMALLER_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllCodigosByProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);
        Producto producto;
        if (TestUtil.findAll(em, Producto.class).isEmpty()) {
            producto = ProductoResourceIT.createEntity(em);
            em.persist(producto);
            em.flush();
        } else {
            producto = TestUtil.findAll(em, Producto.class).get(0);
        }
        em.persist(producto);
        em.flush();
        codigo.addProducto(producto);
        codigoRepository.saveAndFlush(codigo);
        Long productoId = producto.getId();

        // Get all the codigoList where producto equals to productoId
        defaultCodigoShouldBeFound("productoId.equals=" + productoId);

        // Get all the codigoList where producto equals to (productoId + 1)
        defaultCodigoShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCodigoShouldBeFound(String filter) throws Exception {
        restCodigoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(codigo.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].detalle").value(hasItem(DEFAULT_DETALLE.toString())))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));

        // Check, that the count call also returns 1
        restCodigoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCodigoShouldNotBeFound(String filter) throws Exception {
        restCodigoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCodigoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCodigo() throws Exception {
        // Get the codigo
        restCodigoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCodigo() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        int databaseSizeBeforeUpdate = codigoRepository.findAll().size();

        // Update the codigo
        Codigo updatedCodigo = codigoRepository.findById(codigo.getId()).get();
        // Disconnect from session so that the updates on updatedCodigo are not directly saved in db
        em.detach(updatedCodigo);
        updatedCodigo.codigo(UPDATED_CODIGO).detalle(UPDATED_DETALLE).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restCodigoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCodigo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCodigo))
            )
            .andExpect(status().isOk());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeUpdate);
        Codigo testCodigo = codigoList.get(codigoList.size() - 1);
        assertThat(testCodigo.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testCodigo.getDetalle()).isEqualTo(UPDATED_DETALLE);
        assertThat(testCodigo.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingCodigo() throws Exception {
        int databaseSizeBeforeUpdate = codigoRepository.findAll().size();
        codigo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodigoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, codigo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(codigo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCodigo() throws Exception {
        int databaseSizeBeforeUpdate = codigoRepository.findAll().size();
        codigo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodigoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(codigo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCodigo() throws Exception {
        int databaseSizeBeforeUpdate = codigoRepository.findAll().size();
        codigo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodigoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codigo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCodigoWithPatch() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        int databaseSizeBeforeUpdate = codigoRepository.findAll().size();

        // Update the codigo using partial update
        Codigo partialUpdatedCodigo = new Codigo();
        partialUpdatedCodigo.setId(codigo.getId());

        partialUpdatedCodigo.codigo(UPDATED_CODIGO).detalle(UPDATED_DETALLE).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restCodigoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodigo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCodigo))
            )
            .andExpect(status().isOk());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeUpdate);
        Codigo testCodigo = codigoList.get(codigoList.size() - 1);
        assertThat(testCodigo.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testCodigo.getDetalle()).isEqualTo(UPDATED_DETALLE);
        assertThat(testCodigo.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdateCodigoWithPatch() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        int databaseSizeBeforeUpdate = codigoRepository.findAll().size();

        // Update the codigo using partial update
        Codigo partialUpdatedCodigo = new Codigo();
        partialUpdatedCodigo.setId(codigo.getId());

        partialUpdatedCodigo.codigo(UPDATED_CODIGO).detalle(UPDATED_DETALLE).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restCodigoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodigo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCodigo))
            )
            .andExpect(status().isOk());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeUpdate);
        Codigo testCodigo = codigoList.get(codigoList.size() - 1);
        assertThat(testCodigo.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testCodigo.getDetalle()).isEqualTo(UPDATED_DETALLE);
        assertThat(testCodigo.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingCodigo() throws Exception {
        int databaseSizeBeforeUpdate = codigoRepository.findAll().size();
        codigo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodigoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, codigo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(codigo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCodigo() throws Exception {
        int databaseSizeBeforeUpdate = codigoRepository.findAll().size();
        codigo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodigoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(codigo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCodigo() throws Exception {
        int databaseSizeBeforeUpdate = codigoRepository.findAll().size();
        codigo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodigoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(codigo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Codigo in the database
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCodigo() throws Exception {
        // Initialize the database
        codigoRepository.saveAndFlush(codigo);

        int databaseSizeBeforeDelete = codigoRepository.findAll().size();

        // Delete the codigo
        restCodigoMockMvc
            .perform(delete(ENTITY_API_URL_ID, codigo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Codigo> codigoList = codigoRepository.findAll();
        assertThat(codigoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
