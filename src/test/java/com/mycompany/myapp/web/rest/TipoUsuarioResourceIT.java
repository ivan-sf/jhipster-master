package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TipoUsuario;
import com.mycompany.myapp.repository.TipoUsuarioRepository;
import com.mycompany.myapp.service.criteria.TipoUsuarioCriteria;
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
 * Integration tests for the {@link TipoUsuarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoUsuarioResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoUsuarioMockMvc;

    private TipoUsuario tipoUsuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoUsuario createEntity(EntityManager em) {
        TipoUsuario tipoUsuario = new TipoUsuario().name(DEFAULT_NAME);
        return tipoUsuario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoUsuario createUpdatedEntity(EntityManager em) {
        TipoUsuario tipoUsuario = new TipoUsuario().name(UPDATED_NAME);
        return tipoUsuario;
    }

    @BeforeEach
    public void initTest() {
        tipoUsuario = createEntity(em);
    }

    @Test
    @Transactional
    void createTipoUsuario() throws Exception {
        int databaseSizeBeforeCreate = tipoUsuarioRepository.findAll().size();
        // Create the TipoUsuario
        restTipoUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoUsuario)))
            .andExpect(status().isCreated());

        // Validate the TipoUsuario in the database
        List<TipoUsuario> tipoUsuarioList = tipoUsuarioRepository.findAll();
        assertThat(tipoUsuarioList).hasSize(databaseSizeBeforeCreate + 1);
        TipoUsuario testTipoUsuario = tipoUsuarioList.get(tipoUsuarioList.size() - 1);
        assertThat(testTipoUsuario.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createTipoUsuarioWithExistingId() throws Exception {
        // Create the TipoUsuario with an existing ID
        tipoUsuario.setId(1L);

        int databaseSizeBeforeCreate = tipoUsuarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoUsuario)))
            .andExpect(status().isBadRequest());

        // Validate the TipoUsuario in the database
        List<TipoUsuario> tipoUsuarioList = tipoUsuarioRepository.findAll();
        assertThat(tipoUsuarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTipoUsuarios() throws Exception {
        // Initialize the database
        tipoUsuarioRepository.saveAndFlush(tipoUsuario);

        // Get all the tipoUsuarioList
        restTipoUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoUsuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTipoUsuario() throws Exception {
        // Initialize the database
        tipoUsuarioRepository.saveAndFlush(tipoUsuario);

        // Get the tipoUsuario
        restTipoUsuarioMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoUsuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoUsuario.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getTipoUsuariosByIdFiltering() throws Exception {
        // Initialize the database
        tipoUsuarioRepository.saveAndFlush(tipoUsuario);

        Long id = tipoUsuario.getId();

        defaultTipoUsuarioShouldBeFound("id.equals=" + id);
        defaultTipoUsuarioShouldNotBeFound("id.notEquals=" + id);

        defaultTipoUsuarioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTipoUsuarioShouldNotBeFound("id.greaterThan=" + id);

        defaultTipoUsuarioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTipoUsuarioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTipoUsuariosByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoUsuarioRepository.saveAndFlush(tipoUsuario);

        // Get all the tipoUsuarioList where name equals to DEFAULT_NAME
        defaultTipoUsuarioShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the tipoUsuarioList where name equals to UPDATED_NAME
        defaultTipoUsuarioShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTipoUsuariosByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tipoUsuarioRepository.saveAndFlush(tipoUsuario);

        // Get all the tipoUsuarioList where name not equals to DEFAULT_NAME
        defaultTipoUsuarioShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the tipoUsuarioList where name not equals to UPDATED_NAME
        defaultTipoUsuarioShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTipoUsuariosByNameIsInShouldWork() throws Exception {
        // Initialize the database
        tipoUsuarioRepository.saveAndFlush(tipoUsuario);

        // Get all the tipoUsuarioList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTipoUsuarioShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the tipoUsuarioList where name equals to UPDATED_NAME
        defaultTipoUsuarioShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTipoUsuariosByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tipoUsuarioRepository.saveAndFlush(tipoUsuario);

        // Get all the tipoUsuarioList where name is not null
        defaultTipoUsuarioShouldBeFound("name.specified=true");

        // Get all the tipoUsuarioList where name is null
        defaultTipoUsuarioShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoUsuariosByNameContainsSomething() throws Exception {
        // Initialize the database
        tipoUsuarioRepository.saveAndFlush(tipoUsuario);

        // Get all the tipoUsuarioList where name contains DEFAULT_NAME
        defaultTipoUsuarioShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the tipoUsuarioList where name contains UPDATED_NAME
        defaultTipoUsuarioShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTipoUsuariosByNameNotContainsSomething() throws Exception {
        // Initialize the database
        tipoUsuarioRepository.saveAndFlush(tipoUsuario);

        // Get all the tipoUsuarioList where name does not contain DEFAULT_NAME
        defaultTipoUsuarioShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the tipoUsuarioList where name does not contain UPDATED_NAME
        defaultTipoUsuarioShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTipoUsuarioShouldBeFound(String filter) throws Exception {
        restTipoUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoUsuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restTipoUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTipoUsuarioShouldNotBeFound(String filter) throws Exception {
        restTipoUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTipoUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTipoUsuario() throws Exception {
        // Get the tipoUsuario
        restTipoUsuarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTipoUsuario() throws Exception {
        // Initialize the database
        tipoUsuarioRepository.saveAndFlush(tipoUsuario);

        int databaseSizeBeforeUpdate = tipoUsuarioRepository.findAll().size();

        // Update the tipoUsuario
        TipoUsuario updatedTipoUsuario = tipoUsuarioRepository.findById(tipoUsuario.getId()).get();
        // Disconnect from session so that the updates on updatedTipoUsuario are not directly saved in db
        em.detach(updatedTipoUsuario);
        updatedTipoUsuario.name(UPDATED_NAME);

        restTipoUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTipoUsuario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTipoUsuario))
            )
            .andExpect(status().isOk());

        // Validate the TipoUsuario in the database
        List<TipoUsuario> tipoUsuarioList = tipoUsuarioRepository.findAll();
        assertThat(tipoUsuarioList).hasSize(databaseSizeBeforeUpdate);
        TipoUsuario testTipoUsuario = tipoUsuarioList.get(tipoUsuarioList.size() - 1);
        assertThat(testTipoUsuario.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTipoUsuario() throws Exception {
        int databaseSizeBeforeUpdate = tipoUsuarioRepository.findAll().size();
        tipoUsuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoUsuario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoUsuario in the database
        List<TipoUsuario> tipoUsuarioList = tipoUsuarioRepository.findAll();
        assertThat(tipoUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoUsuario() throws Exception {
        int databaseSizeBeforeUpdate = tipoUsuarioRepository.findAll().size();
        tipoUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoUsuario in the database
        List<TipoUsuario> tipoUsuarioList = tipoUsuarioRepository.findAll();
        assertThat(tipoUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoUsuario() throws Exception {
        int databaseSizeBeforeUpdate = tipoUsuarioRepository.findAll().size();
        tipoUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoUsuarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoUsuario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoUsuario in the database
        List<TipoUsuario> tipoUsuarioList = tipoUsuarioRepository.findAll();
        assertThat(tipoUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoUsuarioWithPatch() throws Exception {
        // Initialize the database
        tipoUsuarioRepository.saveAndFlush(tipoUsuario);

        int databaseSizeBeforeUpdate = tipoUsuarioRepository.findAll().size();

        // Update the tipoUsuario using partial update
        TipoUsuario partialUpdatedTipoUsuario = new TipoUsuario();
        partialUpdatedTipoUsuario.setId(tipoUsuario.getId());

        partialUpdatedTipoUsuario.name(UPDATED_NAME);

        restTipoUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoUsuario))
            )
            .andExpect(status().isOk());

        // Validate the TipoUsuario in the database
        List<TipoUsuario> tipoUsuarioList = tipoUsuarioRepository.findAll();
        assertThat(tipoUsuarioList).hasSize(databaseSizeBeforeUpdate);
        TipoUsuario testTipoUsuario = tipoUsuarioList.get(tipoUsuarioList.size() - 1);
        assertThat(testTipoUsuario.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTipoUsuarioWithPatch() throws Exception {
        // Initialize the database
        tipoUsuarioRepository.saveAndFlush(tipoUsuario);

        int databaseSizeBeforeUpdate = tipoUsuarioRepository.findAll().size();

        // Update the tipoUsuario using partial update
        TipoUsuario partialUpdatedTipoUsuario = new TipoUsuario();
        partialUpdatedTipoUsuario.setId(tipoUsuario.getId());

        partialUpdatedTipoUsuario.name(UPDATED_NAME);

        restTipoUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoUsuario))
            )
            .andExpect(status().isOk());

        // Validate the TipoUsuario in the database
        List<TipoUsuario> tipoUsuarioList = tipoUsuarioRepository.findAll();
        assertThat(tipoUsuarioList).hasSize(databaseSizeBeforeUpdate);
        TipoUsuario testTipoUsuario = tipoUsuarioList.get(tipoUsuarioList.size() - 1);
        assertThat(testTipoUsuario.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTipoUsuario() throws Exception {
        int databaseSizeBeforeUpdate = tipoUsuarioRepository.findAll().size();
        tipoUsuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoUsuario in the database
        List<TipoUsuario> tipoUsuarioList = tipoUsuarioRepository.findAll();
        assertThat(tipoUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoUsuario() throws Exception {
        int databaseSizeBeforeUpdate = tipoUsuarioRepository.findAll().size();
        tipoUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoUsuario in the database
        List<TipoUsuario> tipoUsuarioList = tipoUsuarioRepository.findAll();
        assertThat(tipoUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoUsuario() throws Exception {
        int databaseSizeBeforeUpdate = tipoUsuarioRepository.findAll().size();
        tipoUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tipoUsuario))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoUsuario in the database
        List<TipoUsuario> tipoUsuarioList = tipoUsuarioRepository.findAll();
        assertThat(tipoUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoUsuario() throws Exception {
        // Initialize the database
        tipoUsuarioRepository.saveAndFlush(tipoUsuario);

        int databaseSizeBeforeDelete = tipoUsuarioRepository.findAll().size();

        // Delete the tipoUsuario
        restTipoUsuarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoUsuario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoUsuario> tipoUsuarioList = tipoUsuarioRepository.findAll();
        assertThat(tipoUsuarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
