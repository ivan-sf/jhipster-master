package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TipoComprobanteContable;
import com.mycompany.myapp.repository.TipoComprobanteContableRepository;
import com.mycompany.myapp.service.criteria.TipoComprobanteContableCriteria;
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

/**
 * Integration tests for the {@link TipoComprobanteContableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoComprobanteContableResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-comprobante-contables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoComprobanteContableRepository tipoComprobanteContableRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoComprobanteContableMockMvc;

    private TipoComprobanteContable tipoComprobanteContable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoComprobanteContable createEntity(EntityManager em) {
        TipoComprobanteContable tipoComprobanteContable = new TipoComprobanteContable().name(DEFAULT_NAME);
        return tipoComprobanteContable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoComprobanteContable createUpdatedEntity(EntityManager em) {
        TipoComprobanteContable tipoComprobanteContable = new TipoComprobanteContable().name(UPDATED_NAME);
        return tipoComprobanteContable;
    }

    @BeforeEach
    public void initTest() {
        tipoComprobanteContable = createEntity(em);
    }

    @Test
    @Transactional
    void createTipoComprobanteContable() throws Exception {
        int databaseSizeBeforeCreate = tipoComprobanteContableRepository.findAll().size();
        // Create the TipoComprobanteContable
        restTipoComprobanteContableMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoComprobanteContable))
            )
            .andExpect(status().isCreated());

        // Validate the TipoComprobanteContable in the database
        List<TipoComprobanteContable> tipoComprobanteContableList = tipoComprobanteContableRepository.findAll();
        assertThat(tipoComprobanteContableList).hasSize(databaseSizeBeforeCreate + 1);
        TipoComprobanteContable testTipoComprobanteContable = tipoComprobanteContableList.get(tipoComprobanteContableList.size() - 1);
        assertThat(testTipoComprobanteContable.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createTipoComprobanteContableWithExistingId() throws Exception {
        // Create the TipoComprobanteContable with an existing ID
        tipoComprobanteContable.setId(1L);

        int databaseSizeBeforeCreate = tipoComprobanteContableRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoComprobanteContableMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoComprobanteContable))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoComprobanteContable in the database
        List<TipoComprobanteContable> tipoComprobanteContableList = tipoComprobanteContableRepository.findAll();
        assertThat(tipoComprobanteContableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTipoComprobanteContables() throws Exception {
        // Initialize the database
        tipoComprobanteContableRepository.saveAndFlush(tipoComprobanteContable);

        // Get all the tipoComprobanteContableList
        restTipoComprobanteContableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoComprobanteContable.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTipoComprobanteContable() throws Exception {
        // Initialize the database
        tipoComprobanteContableRepository.saveAndFlush(tipoComprobanteContable);

        // Get the tipoComprobanteContable
        restTipoComprobanteContableMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoComprobanteContable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoComprobanteContable.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getTipoComprobanteContablesByIdFiltering() throws Exception {
        // Initialize the database
        tipoComprobanteContableRepository.saveAndFlush(tipoComprobanteContable);

        Long id = tipoComprobanteContable.getId();

        defaultTipoComprobanteContableShouldBeFound("id.equals=" + id);
        defaultTipoComprobanteContableShouldNotBeFound("id.notEquals=" + id);

        defaultTipoComprobanteContableShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTipoComprobanteContableShouldNotBeFound("id.greaterThan=" + id);

        defaultTipoComprobanteContableShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTipoComprobanteContableShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTipoComprobanteContablesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoComprobanteContableRepository.saveAndFlush(tipoComprobanteContable);

        // Get all the tipoComprobanteContableList where name equals to DEFAULT_NAME
        defaultTipoComprobanteContableShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the tipoComprobanteContableList where name equals to UPDATED_NAME
        defaultTipoComprobanteContableShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTipoComprobanteContablesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tipoComprobanteContableRepository.saveAndFlush(tipoComprobanteContable);

        // Get all the tipoComprobanteContableList where name not equals to DEFAULT_NAME
        defaultTipoComprobanteContableShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the tipoComprobanteContableList where name not equals to UPDATED_NAME
        defaultTipoComprobanteContableShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTipoComprobanteContablesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        tipoComprobanteContableRepository.saveAndFlush(tipoComprobanteContable);

        // Get all the tipoComprobanteContableList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTipoComprobanteContableShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the tipoComprobanteContableList where name equals to UPDATED_NAME
        defaultTipoComprobanteContableShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTipoComprobanteContablesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tipoComprobanteContableRepository.saveAndFlush(tipoComprobanteContable);

        // Get all the tipoComprobanteContableList where name is not null
        defaultTipoComprobanteContableShouldBeFound("name.specified=true");

        // Get all the tipoComprobanteContableList where name is null
        defaultTipoComprobanteContableShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoComprobanteContablesByNameContainsSomething() throws Exception {
        // Initialize the database
        tipoComprobanteContableRepository.saveAndFlush(tipoComprobanteContable);

        // Get all the tipoComprobanteContableList where name contains DEFAULT_NAME
        defaultTipoComprobanteContableShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the tipoComprobanteContableList where name contains UPDATED_NAME
        defaultTipoComprobanteContableShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTipoComprobanteContablesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        tipoComprobanteContableRepository.saveAndFlush(tipoComprobanteContable);

        // Get all the tipoComprobanteContableList where name does not contain DEFAULT_NAME
        defaultTipoComprobanteContableShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the tipoComprobanteContableList where name does not contain UPDATED_NAME
        defaultTipoComprobanteContableShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTipoComprobanteContableShouldBeFound(String filter) throws Exception {
        restTipoComprobanteContableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoComprobanteContable.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restTipoComprobanteContableMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTipoComprobanteContableShouldNotBeFound(String filter) throws Exception {
        restTipoComprobanteContableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTipoComprobanteContableMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTipoComprobanteContable() throws Exception {
        // Get the tipoComprobanteContable
        restTipoComprobanteContableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTipoComprobanteContable() throws Exception {
        // Initialize the database
        tipoComprobanteContableRepository.saveAndFlush(tipoComprobanteContable);

        int databaseSizeBeforeUpdate = tipoComprobanteContableRepository.findAll().size();

        // Update the tipoComprobanteContable
        TipoComprobanteContable updatedTipoComprobanteContable = tipoComprobanteContableRepository
            .findById(tipoComprobanteContable.getId())
            .get();
        // Disconnect from session so that the updates on updatedTipoComprobanteContable are not directly saved in db
        em.detach(updatedTipoComprobanteContable);
        updatedTipoComprobanteContable.name(UPDATED_NAME);

        restTipoComprobanteContableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTipoComprobanteContable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTipoComprobanteContable))
            )
            .andExpect(status().isOk());

        // Validate the TipoComprobanteContable in the database
        List<TipoComprobanteContable> tipoComprobanteContableList = tipoComprobanteContableRepository.findAll();
        assertThat(tipoComprobanteContableList).hasSize(databaseSizeBeforeUpdate);
        TipoComprobanteContable testTipoComprobanteContable = tipoComprobanteContableList.get(tipoComprobanteContableList.size() - 1);
        assertThat(testTipoComprobanteContable.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTipoComprobanteContable() throws Exception {
        int databaseSizeBeforeUpdate = tipoComprobanteContableRepository.findAll().size();
        tipoComprobanteContable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoComprobanteContableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoComprobanteContable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoComprobanteContable))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoComprobanteContable in the database
        List<TipoComprobanteContable> tipoComprobanteContableList = tipoComprobanteContableRepository.findAll();
        assertThat(tipoComprobanteContableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoComprobanteContable() throws Exception {
        int databaseSizeBeforeUpdate = tipoComprobanteContableRepository.findAll().size();
        tipoComprobanteContable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoComprobanteContableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoComprobanteContable))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoComprobanteContable in the database
        List<TipoComprobanteContable> tipoComprobanteContableList = tipoComprobanteContableRepository.findAll();
        assertThat(tipoComprobanteContableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoComprobanteContable() throws Exception {
        int databaseSizeBeforeUpdate = tipoComprobanteContableRepository.findAll().size();
        tipoComprobanteContable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoComprobanteContableMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoComprobanteContable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoComprobanteContable in the database
        List<TipoComprobanteContable> tipoComprobanteContableList = tipoComprobanteContableRepository.findAll();
        assertThat(tipoComprobanteContableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoComprobanteContableWithPatch() throws Exception {
        // Initialize the database
        tipoComprobanteContableRepository.saveAndFlush(tipoComprobanteContable);

        int databaseSizeBeforeUpdate = tipoComprobanteContableRepository.findAll().size();

        // Update the tipoComprobanteContable using partial update
        TipoComprobanteContable partialUpdatedTipoComprobanteContable = new TipoComprobanteContable();
        partialUpdatedTipoComprobanteContable.setId(tipoComprobanteContable.getId());

        partialUpdatedTipoComprobanteContable.name(UPDATED_NAME);

        restTipoComprobanteContableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoComprobanteContable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoComprobanteContable))
            )
            .andExpect(status().isOk());

        // Validate the TipoComprobanteContable in the database
        List<TipoComprobanteContable> tipoComprobanteContableList = tipoComprobanteContableRepository.findAll();
        assertThat(tipoComprobanteContableList).hasSize(databaseSizeBeforeUpdate);
        TipoComprobanteContable testTipoComprobanteContable = tipoComprobanteContableList.get(tipoComprobanteContableList.size() - 1);
        assertThat(testTipoComprobanteContable.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTipoComprobanteContableWithPatch() throws Exception {
        // Initialize the database
        tipoComprobanteContableRepository.saveAndFlush(tipoComprobanteContable);

        int databaseSizeBeforeUpdate = tipoComprobanteContableRepository.findAll().size();

        // Update the tipoComprobanteContable using partial update
        TipoComprobanteContable partialUpdatedTipoComprobanteContable = new TipoComprobanteContable();
        partialUpdatedTipoComprobanteContable.setId(tipoComprobanteContable.getId());

        partialUpdatedTipoComprobanteContable.name(UPDATED_NAME);

        restTipoComprobanteContableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoComprobanteContable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoComprobanteContable))
            )
            .andExpect(status().isOk());

        // Validate the TipoComprobanteContable in the database
        List<TipoComprobanteContable> tipoComprobanteContableList = tipoComprobanteContableRepository.findAll();
        assertThat(tipoComprobanteContableList).hasSize(databaseSizeBeforeUpdate);
        TipoComprobanteContable testTipoComprobanteContable = tipoComprobanteContableList.get(tipoComprobanteContableList.size() - 1);
        assertThat(testTipoComprobanteContable.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTipoComprobanteContable() throws Exception {
        int databaseSizeBeforeUpdate = tipoComprobanteContableRepository.findAll().size();
        tipoComprobanteContable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoComprobanteContableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoComprobanteContable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoComprobanteContable))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoComprobanteContable in the database
        List<TipoComprobanteContable> tipoComprobanteContableList = tipoComprobanteContableRepository.findAll();
        assertThat(tipoComprobanteContableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoComprobanteContable() throws Exception {
        int databaseSizeBeforeUpdate = tipoComprobanteContableRepository.findAll().size();
        tipoComprobanteContable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoComprobanteContableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoComprobanteContable))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoComprobanteContable in the database
        List<TipoComprobanteContable> tipoComprobanteContableList = tipoComprobanteContableRepository.findAll();
        assertThat(tipoComprobanteContableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoComprobanteContable() throws Exception {
        int databaseSizeBeforeUpdate = tipoComprobanteContableRepository.findAll().size();
        tipoComprobanteContable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoComprobanteContableMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoComprobanteContable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoComprobanteContable in the database
        List<TipoComprobanteContable> tipoComprobanteContableList = tipoComprobanteContableRepository.findAll();
        assertThat(tipoComprobanteContableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoComprobanteContable() throws Exception {
        // Initialize the database
        tipoComprobanteContableRepository.saveAndFlush(tipoComprobanteContable);

        int databaseSizeBeforeDelete = tipoComprobanteContableRepository.findAll().size();

        // Delete the tipoComprobanteContable
        restTipoComprobanteContableMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoComprobanteContable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoComprobanteContable> tipoComprobanteContableList = tipoComprobanteContableRepository.findAll();
        assertThat(tipoComprobanteContableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
