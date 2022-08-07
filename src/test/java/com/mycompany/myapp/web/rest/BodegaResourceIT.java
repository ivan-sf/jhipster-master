package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Bodega;
import com.mycompany.myapp.domain.Inventario;
import com.mycompany.myapp.domain.Producto;
import com.mycompany.myapp.domain.Usuario;
import com.mycompany.myapp.repository.BodegaRepository;
import com.mycompany.myapp.service.BodegaService;
import com.mycompany.myapp.service.criteria.BodegaCriteria;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link BodegaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BodegaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DETALLE = "AAAAAAAAAA";
    private static final String UPDATED_DETALLE = "BBBBBBBBBB";

    private static final String DEFAULT_UBICACION = "AAAAAAAAAA";
    private static final String UPDATED_UBICACION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bodegas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BodegaRepository bodegaRepository;

    @Mock
    private BodegaRepository bodegaRepositoryMock;

    @Mock
    private BodegaService bodegaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBodegaMockMvc;

    private Bodega bodega;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bodega createEntity(EntityManager em) {
        Bodega bodega = new Bodega().nombre(DEFAULT_NOMBRE).detalle(DEFAULT_DETALLE).ubicacion(DEFAULT_UBICACION);
        return bodega;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bodega createUpdatedEntity(EntityManager em) {
        Bodega bodega = new Bodega().nombre(UPDATED_NOMBRE).detalle(UPDATED_DETALLE).ubicacion(UPDATED_UBICACION);
        return bodega;
    }

    @BeforeEach
    public void initTest() {
        bodega = createEntity(em);
    }

    @Test
    @Transactional
    void createBodega() throws Exception {
        int databaseSizeBeforeCreate = bodegaRepository.findAll().size();
        // Create the Bodega
        restBodegaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bodega)))
            .andExpect(status().isCreated());

        // Validate the Bodega in the database
        List<Bodega> bodegaList = bodegaRepository.findAll();
        assertThat(bodegaList).hasSize(databaseSizeBeforeCreate + 1);
        Bodega testBodega = bodegaList.get(bodegaList.size() - 1);
        assertThat(testBodega.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testBodega.getDetalle()).isEqualTo(DEFAULT_DETALLE);
        assertThat(testBodega.getUbicacion()).isEqualTo(DEFAULT_UBICACION);
    }

    @Test
    @Transactional
    void createBodegaWithExistingId() throws Exception {
        // Create the Bodega with an existing ID
        bodega.setId(1L);

        int databaseSizeBeforeCreate = bodegaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBodegaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bodega)))
            .andExpect(status().isBadRequest());

        // Validate the Bodega in the database
        List<Bodega> bodegaList = bodegaRepository.findAll();
        assertThat(bodegaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBodegas() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);

        // Get all the bodegaList
        restBodegaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bodega.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].detalle").value(hasItem(DEFAULT_DETALLE.toString())))
            .andExpect(jsonPath("$.[*].ubicacion").value(hasItem(DEFAULT_UBICACION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBodegasWithEagerRelationshipsIsEnabled() throws Exception {
        when(bodegaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBodegaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bodegaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBodegasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bodegaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBodegaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bodegaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getBodega() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);

        // Get the bodega
        restBodegaMockMvc
            .perform(get(ENTITY_API_URL_ID, bodega.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bodega.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.detalle").value(DEFAULT_DETALLE.toString()))
            .andExpect(jsonPath("$.ubicacion").value(DEFAULT_UBICACION));
    }

    @Test
    @Transactional
    void getBodegasByIdFiltering() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);

        Long id = bodega.getId();

        defaultBodegaShouldBeFound("id.equals=" + id);
        defaultBodegaShouldNotBeFound("id.notEquals=" + id);

        defaultBodegaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBodegaShouldNotBeFound("id.greaterThan=" + id);

        defaultBodegaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBodegaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBodegasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);

        // Get all the bodegaList where nombre equals to DEFAULT_NOMBRE
        defaultBodegaShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the bodegaList where nombre equals to UPDATED_NOMBRE
        defaultBodegaShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllBodegasByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);

        // Get all the bodegaList where nombre not equals to DEFAULT_NOMBRE
        defaultBodegaShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the bodegaList where nombre not equals to UPDATED_NOMBRE
        defaultBodegaShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllBodegasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);

        // Get all the bodegaList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultBodegaShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the bodegaList where nombre equals to UPDATED_NOMBRE
        defaultBodegaShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllBodegasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);

        // Get all the bodegaList where nombre is not null
        defaultBodegaShouldBeFound("nombre.specified=true");

        // Get all the bodegaList where nombre is null
        defaultBodegaShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllBodegasByNombreContainsSomething() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);

        // Get all the bodegaList where nombre contains DEFAULT_NOMBRE
        defaultBodegaShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the bodegaList where nombre contains UPDATED_NOMBRE
        defaultBodegaShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllBodegasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);

        // Get all the bodegaList where nombre does not contain DEFAULT_NOMBRE
        defaultBodegaShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the bodegaList where nombre does not contain UPDATED_NOMBRE
        defaultBodegaShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllBodegasByUbicacionIsEqualToSomething() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);

        // Get all the bodegaList where ubicacion equals to DEFAULT_UBICACION
        defaultBodegaShouldBeFound("ubicacion.equals=" + DEFAULT_UBICACION);

        // Get all the bodegaList where ubicacion equals to UPDATED_UBICACION
        defaultBodegaShouldNotBeFound("ubicacion.equals=" + UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void getAllBodegasByUbicacionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);

        // Get all the bodegaList where ubicacion not equals to DEFAULT_UBICACION
        defaultBodegaShouldNotBeFound("ubicacion.notEquals=" + DEFAULT_UBICACION);

        // Get all the bodegaList where ubicacion not equals to UPDATED_UBICACION
        defaultBodegaShouldBeFound("ubicacion.notEquals=" + UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void getAllBodegasByUbicacionIsInShouldWork() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);

        // Get all the bodegaList where ubicacion in DEFAULT_UBICACION or UPDATED_UBICACION
        defaultBodegaShouldBeFound("ubicacion.in=" + DEFAULT_UBICACION + "," + UPDATED_UBICACION);

        // Get all the bodegaList where ubicacion equals to UPDATED_UBICACION
        defaultBodegaShouldNotBeFound("ubicacion.in=" + UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void getAllBodegasByUbicacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);

        // Get all the bodegaList where ubicacion is not null
        defaultBodegaShouldBeFound("ubicacion.specified=true");

        // Get all the bodegaList where ubicacion is null
        defaultBodegaShouldNotBeFound("ubicacion.specified=false");
    }

    @Test
    @Transactional
    void getAllBodegasByUbicacionContainsSomething() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);

        // Get all the bodegaList where ubicacion contains DEFAULT_UBICACION
        defaultBodegaShouldBeFound("ubicacion.contains=" + DEFAULT_UBICACION);

        // Get all the bodegaList where ubicacion contains UPDATED_UBICACION
        defaultBodegaShouldNotBeFound("ubicacion.contains=" + UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void getAllBodegasByUbicacionNotContainsSomething() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);

        // Get all the bodegaList where ubicacion does not contain DEFAULT_UBICACION
        defaultBodegaShouldNotBeFound("ubicacion.doesNotContain=" + DEFAULT_UBICACION);

        // Get all the bodegaList where ubicacion does not contain UPDATED_UBICACION
        defaultBodegaShouldBeFound("ubicacion.doesNotContain=" + UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void getAllBodegasByProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);
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
        bodega.addProducto(producto);
        bodegaRepository.saveAndFlush(bodega);
        Long productoId = producto.getId();

        // Get all the bodegaList where producto equals to productoId
        defaultBodegaShouldBeFound("productoId.equals=" + productoId);

        // Get all the bodegaList where producto equals to (productoId + 1)
        defaultBodegaShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    @Test
    @Transactional
    void getAllBodegasByUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);
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
        bodega.addUsuario(usuario);
        bodegaRepository.saveAndFlush(bodega);
        Long usuarioId = usuario.getId();

        // Get all the bodegaList where usuario equals to usuarioId
        defaultBodegaShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the bodegaList where usuario equals to (usuarioId + 1)
        defaultBodegaShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    @Test
    @Transactional
    void getAllBodegasByInventarioIsEqualToSomething() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);
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
        bodega.setInventario(inventario);
        bodegaRepository.saveAndFlush(bodega);
        Long inventarioId = inventario.getId();

        // Get all the bodegaList where inventario equals to inventarioId
        defaultBodegaShouldBeFound("inventarioId.equals=" + inventarioId);

        // Get all the bodegaList where inventario equals to (inventarioId + 1)
        defaultBodegaShouldNotBeFound("inventarioId.equals=" + (inventarioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBodegaShouldBeFound(String filter) throws Exception {
        restBodegaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bodega.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].detalle").value(hasItem(DEFAULT_DETALLE.toString())))
            .andExpect(jsonPath("$.[*].ubicacion").value(hasItem(DEFAULT_UBICACION)));

        // Check, that the count call also returns 1
        restBodegaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBodegaShouldNotBeFound(String filter) throws Exception {
        restBodegaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBodegaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBodega() throws Exception {
        // Get the bodega
        restBodegaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBodega() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);

        int databaseSizeBeforeUpdate = bodegaRepository.findAll().size();

        // Update the bodega
        Bodega updatedBodega = bodegaRepository.findById(bodega.getId()).get();
        // Disconnect from session so that the updates on updatedBodega are not directly saved in db
        em.detach(updatedBodega);
        updatedBodega.nombre(UPDATED_NOMBRE).detalle(UPDATED_DETALLE).ubicacion(UPDATED_UBICACION);

        restBodegaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBodega.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBodega))
            )
            .andExpect(status().isOk());

        // Validate the Bodega in the database
        List<Bodega> bodegaList = bodegaRepository.findAll();
        assertThat(bodegaList).hasSize(databaseSizeBeforeUpdate);
        Bodega testBodega = bodegaList.get(bodegaList.size() - 1);
        assertThat(testBodega.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testBodega.getDetalle()).isEqualTo(UPDATED_DETALLE);
        assertThat(testBodega.getUbicacion()).isEqualTo(UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void putNonExistingBodega() throws Exception {
        int databaseSizeBeforeUpdate = bodegaRepository.findAll().size();
        bodega.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBodegaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bodega.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bodega))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bodega in the database
        List<Bodega> bodegaList = bodegaRepository.findAll();
        assertThat(bodegaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBodega() throws Exception {
        int databaseSizeBeforeUpdate = bodegaRepository.findAll().size();
        bodega.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodegaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bodega))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bodega in the database
        List<Bodega> bodegaList = bodegaRepository.findAll();
        assertThat(bodegaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBodega() throws Exception {
        int databaseSizeBeforeUpdate = bodegaRepository.findAll().size();
        bodega.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodegaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bodega)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bodega in the database
        List<Bodega> bodegaList = bodegaRepository.findAll();
        assertThat(bodegaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBodegaWithPatch() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);

        int databaseSizeBeforeUpdate = bodegaRepository.findAll().size();

        // Update the bodega using partial update
        Bodega partialUpdatedBodega = new Bodega();
        partialUpdatedBodega.setId(bodega.getId());

        partialUpdatedBodega.detalle(UPDATED_DETALLE).ubicacion(UPDATED_UBICACION);

        restBodegaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBodega.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBodega))
            )
            .andExpect(status().isOk());

        // Validate the Bodega in the database
        List<Bodega> bodegaList = bodegaRepository.findAll();
        assertThat(bodegaList).hasSize(databaseSizeBeforeUpdate);
        Bodega testBodega = bodegaList.get(bodegaList.size() - 1);
        assertThat(testBodega.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testBodega.getDetalle()).isEqualTo(UPDATED_DETALLE);
        assertThat(testBodega.getUbicacion()).isEqualTo(UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void fullUpdateBodegaWithPatch() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);

        int databaseSizeBeforeUpdate = bodegaRepository.findAll().size();

        // Update the bodega using partial update
        Bodega partialUpdatedBodega = new Bodega();
        partialUpdatedBodega.setId(bodega.getId());

        partialUpdatedBodega.nombre(UPDATED_NOMBRE).detalle(UPDATED_DETALLE).ubicacion(UPDATED_UBICACION);

        restBodegaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBodega.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBodega))
            )
            .andExpect(status().isOk());

        // Validate the Bodega in the database
        List<Bodega> bodegaList = bodegaRepository.findAll();
        assertThat(bodegaList).hasSize(databaseSizeBeforeUpdate);
        Bodega testBodega = bodegaList.get(bodegaList.size() - 1);
        assertThat(testBodega.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testBodega.getDetalle()).isEqualTo(UPDATED_DETALLE);
        assertThat(testBodega.getUbicacion()).isEqualTo(UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void patchNonExistingBodega() throws Exception {
        int databaseSizeBeforeUpdate = bodegaRepository.findAll().size();
        bodega.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBodegaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bodega.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bodega))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bodega in the database
        List<Bodega> bodegaList = bodegaRepository.findAll();
        assertThat(bodegaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBodega() throws Exception {
        int databaseSizeBeforeUpdate = bodegaRepository.findAll().size();
        bodega.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodegaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bodega))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bodega in the database
        List<Bodega> bodegaList = bodegaRepository.findAll();
        assertThat(bodegaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBodega() throws Exception {
        int databaseSizeBeforeUpdate = bodegaRepository.findAll().size();
        bodega.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodegaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bodega)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bodega in the database
        List<Bodega> bodegaList = bodegaRepository.findAll();
        assertThat(bodegaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBodega() throws Exception {
        // Initialize the database
        bodegaRepository.saveAndFlush(bodega);

        int databaseSizeBeforeDelete = bodegaRepository.findAll().size();

        // Delete the bodega
        restBodegaMockMvc
            .perform(delete(ENTITY_API_URL_ID, bodega.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bodega> bodegaList = bodegaRepository.findAll();
        assertThat(bodegaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
