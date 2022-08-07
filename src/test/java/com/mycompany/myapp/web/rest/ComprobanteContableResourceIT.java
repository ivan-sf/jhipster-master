package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ComprobanteContable;
import com.mycompany.myapp.repository.ComprobanteContableRepository;
import com.mycompany.myapp.service.criteria.ComprobanteContableCriteria;
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
 * Integration tests for the {@link ComprobanteContableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComprobanteContableResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/comprobante-contables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComprobanteContableRepository comprobanteContableRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComprobanteContableMockMvc;

    private ComprobanteContable comprobanteContable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComprobanteContable createEntity(EntityManager em) {
        ComprobanteContable comprobanteContable = new ComprobanteContable().name(DEFAULT_NAME);
        return comprobanteContable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComprobanteContable createUpdatedEntity(EntityManager em) {
        ComprobanteContable comprobanteContable = new ComprobanteContable().name(UPDATED_NAME);
        return comprobanteContable;
    }

    @BeforeEach
    public void initTest() {
        comprobanteContable = createEntity(em);
    }

    @Test
    @Transactional
    void createComprobanteContable() throws Exception {
        int databaseSizeBeforeCreate = comprobanteContableRepository.findAll().size();
        // Create the ComprobanteContable
        restComprobanteContableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comprobanteContable))
            )
            .andExpect(status().isCreated());

        // Validate the ComprobanteContable in the database
        List<ComprobanteContable> comprobanteContableList = comprobanteContableRepository.findAll();
        assertThat(comprobanteContableList).hasSize(databaseSizeBeforeCreate + 1);
        ComprobanteContable testComprobanteContable = comprobanteContableList.get(comprobanteContableList.size() - 1);
        assertThat(testComprobanteContable.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createComprobanteContableWithExistingId() throws Exception {
        // Create the ComprobanteContable with an existing ID
        comprobanteContable.setId(1L);

        int databaseSizeBeforeCreate = comprobanteContableRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComprobanteContableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comprobanteContable))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComprobanteContable in the database
        List<ComprobanteContable> comprobanteContableList = comprobanteContableRepository.findAll();
        assertThat(comprobanteContableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllComprobanteContables() throws Exception {
        // Initialize the database
        comprobanteContableRepository.saveAndFlush(comprobanteContable);

        // Get all the comprobanteContableList
        restComprobanteContableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comprobanteContable.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getComprobanteContable() throws Exception {
        // Initialize the database
        comprobanteContableRepository.saveAndFlush(comprobanteContable);

        // Get the comprobanteContable
        restComprobanteContableMockMvc
            .perform(get(ENTITY_API_URL_ID, comprobanteContable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(comprobanteContable.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getComprobanteContablesByIdFiltering() throws Exception {
        // Initialize the database
        comprobanteContableRepository.saveAndFlush(comprobanteContable);

        Long id = comprobanteContable.getId();

        defaultComprobanteContableShouldBeFound("id.equals=" + id);
        defaultComprobanteContableShouldNotBeFound("id.notEquals=" + id);

        defaultComprobanteContableShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultComprobanteContableShouldNotBeFound("id.greaterThan=" + id);

        defaultComprobanteContableShouldBeFound("id.lessThanOrEqual=" + id);
        defaultComprobanteContableShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllComprobanteContablesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        comprobanteContableRepository.saveAndFlush(comprobanteContable);

        // Get all the comprobanteContableList where name equals to DEFAULT_NAME
        defaultComprobanteContableShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the comprobanteContableList where name equals to UPDATED_NAME
        defaultComprobanteContableShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllComprobanteContablesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        comprobanteContableRepository.saveAndFlush(comprobanteContable);

        // Get all the comprobanteContableList where name not equals to DEFAULT_NAME
        defaultComprobanteContableShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the comprobanteContableList where name not equals to UPDATED_NAME
        defaultComprobanteContableShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllComprobanteContablesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        comprobanteContableRepository.saveAndFlush(comprobanteContable);

        // Get all the comprobanteContableList where name in DEFAULT_NAME or UPDATED_NAME
        defaultComprobanteContableShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the comprobanteContableList where name equals to UPDATED_NAME
        defaultComprobanteContableShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllComprobanteContablesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        comprobanteContableRepository.saveAndFlush(comprobanteContable);

        // Get all the comprobanteContableList where name is not null
        defaultComprobanteContableShouldBeFound("name.specified=true");

        // Get all the comprobanteContableList where name is null
        defaultComprobanteContableShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllComprobanteContablesByNameContainsSomething() throws Exception {
        // Initialize the database
        comprobanteContableRepository.saveAndFlush(comprobanteContable);

        // Get all the comprobanteContableList where name contains DEFAULT_NAME
        defaultComprobanteContableShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the comprobanteContableList where name contains UPDATED_NAME
        defaultComprobanteContableShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllComprobanteContablesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        comprobanteContableRepository.saveAndFlush(comprobanteContable);

        // Get all the comprobanteContableList where name does not contain DEFAULT_NAME
        defaultComprobanteContableShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the comprobanteContableList where name does not contain UPDATED_NAME
        defaultComprobanteContableShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultComprobanteContableShouldBeFound(String filter) throws Exception {
        restComprobanteContableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comprobanteContable.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restComprobanteContableMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultComprobanteContableShouldNotBeFound(String filter) throws Exception {
        restComprobanteContableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restComprobanteContableMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingComprobanteContable() throws Exception {
        // Get the comprobanteContable
        restComprobanteContableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewComprobanteContable() throws Exception {
        // Initialize the database
        comprobanteContableRepository.saveAndFlush(comprobanteContable);

        int databaseSizeBeforeUpdate = comprobanteContableRepository.findAll().size();

        // Update the comprobanteContable
        ComprobanteContable updatedComprobanteContable = comprobanteContableRepository.findById(comprobanteContable.getId()).get();
        // Disconnect from session so that the updates on updatedComprobanteContable are not directly saved in db
        em.detach(updatedComprobanteContable);
        updatedComprobanteContable.name(UPDATED_NAME);

        restComprobanteContableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedComprobanteContable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedComprobanteContable))
            )
            .andExpect(status().isOk());

        // Validate the ComprobanteContable in the database
        List<ComprobanteContable> comprobanteContableList = comprobanteContableRepository.findAll();
        assertThat(comprobanteContableList).hasSize(databaseSizeBeforeUpdate);
        ComprobanteContable testComprobanteContable = comprobanteContableList.get(comprobanteContableList.size() - 1);
        assertThat(testComprobanteContable.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingComprobanteContable() throws Exception {
        int databaseSizeBeforeUpdate = comprobanteContableRepository.findAll().size();
        comprobanteContable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComprobanteContableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, comprobanteContable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comprobanteContable))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComprobanteContable in the database
        List<ComprobanteContable> comprobanteContableList = comprobanteContableRepository.findAll();
        assertThat(comprobanteContableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComprobanteContable() throws Exception {
        int databaseSizeBeforeUpdate = comprobanteContableRepository.findAll().size();
        comprobanteContable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComprobanteContableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comprobanteContable))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComprobanteContable in the database
        List<ComprobanteContable> comprobanteContableList = comprobanteContableRepository.findAll();
        assertThat(comprobanteContableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComprobanteContable() throws Exception {
        int databaseSizeBeforeUpdate = comprobanteContableRepository.findAll().size();
        comprobanteContable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComprobanteContableMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comprobanteContable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComprobanteContable in the database
        List<ComprobanteContable> comprobanteContableList = comprobanteContableRepository.findAll();
        assertThat(comprobanteContableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComprobanteContableWithPatch() throws Exception {
        // Initialize the database
        comprobanteContableRepository.saveAndFlush(comprobanteContable);

        int databaseSizeBeforeUpdate = comprobanteContableRepository.findAll().size();

        // Update the comprobanteContable using partial update
        ComprobanteContable partialUpdatedComprobanteContable = new ComprobanteContable();
        partialUpdatedComprobanteContable.setId(comprobanteContable.getId());

        restComprobanteContableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComprobanteContable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComprobanteContable))
            )
            .andExpect(status().isOk());

        // Validate the ComprobanteContable in the database
        List<ComprobanteContable> comprobanteContableList = comprobanteContableRepository.findAll();
        assertThat(comprobanteContableList).hasSize(databaseSizeBeforeUpdate);
        ComprobanteContable testComprobanteContable = comprobanteContableList.get(comprobanteContableList.size() - 1);
        assertThat(testComprobanteContable.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateComprobanteContableWithPatch() throws Exception {
        // Initialize the database
        comprobanteContableRepository.saveAndFlush(comprobanteContable);

        int databaseSizeBeforeUpdate = comprobanteContableRepository.findAll().size();

        // Update the comprobanteContable using partial update
        ComprobanteContable partialUpdatedComprobanteContable = new ComprobanteContable();
        partialUpdatedComprobanteContable.setId(comprobanteContable.getId());

        partialUpdatedComprobanteContable.name(UPDATED_NAME);

        restComprobanteContableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComprobanteContable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComprobanteContable))
            )
            .andExpect(status().isOk());

        // Validate the ComprobanteContable in the database
        List<ComprobanteContable> comprobanteContableList = comprobanteContableRepository.findAll();
        assertThat(comprobanteContableList).hasSize(databaseSizeBeforeUpdate);
        ComprobanteContable testComprobanteContable = comprobanteContableList.get(comprobanteContableList.size() - 1);
        assertThat(testComprobanteContable.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingComprobanteContable() throws Exception {
        int databaseSizeBeforeUpdate = comprobanteContableRepository.findAll().size();
        comprobanteContable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComprobanteContableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, comprobanteContable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(comprobanteContable))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComprobanteContable in the database
        List<ComprobanteContable> comprobanteContableList = comprobanteContableRepository.findAll();
        assertThat(comprobanteContableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComprobanteContable() throws Exception {
        int databaseSizeBeforeUpdate = comprobanteContableRepository.findAll().size();
        comprobanteContable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComprobanteContableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(comprobanteContable))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComprobanteContable in the database
        List<ComprobanteContable> comprobanteContableList = comprobanteContableRepository.findAll();
        assertThat(comprobanteContableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComprobanteContable() throws Exception {
        int databaseSizeBeforeUpdate = comprobanteContableRepository.findAll().size();
        comprobanteContable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComprobanteContableMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(comprobanteContable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComprobanteContable in the database
        List<ComprobanteContable> comprobanteContableList = comprobanteContableRepository.findAll();
        assertThat(comprobanteContableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComprobanteContable() throws Exception {
        // Initialize the database
        comprobanteContableRepository.saveAndFlush(comprobanteContable);

        int databaseSizeBeforeDelete = comprobanteContableRepository.findAll().size();

        // Delete the comprobanteContable
        restComprobanteContableMockMvc
            .perform(delete(ENTITY_API_URL_ID, comprobanteContable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ComprobanteContable> comprobanteContableList = comprobanteContableRepository.findAll();
        assertThat(comprobanteContableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
