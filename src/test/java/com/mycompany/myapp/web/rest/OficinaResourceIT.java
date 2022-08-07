package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Oficina;
import com.mycompany.myapp.domain.Producto;
import com.mycompany.myapp.domain.Sucursal;
import com.mycompany.myapp.domain.Usuario;
import com.mycompany.myapp.repository.OficinaRepository;
import com.mycompany.myapp.service.OficinaService;
import com.mycompany.myapp.service.criteria.OficinaCriteria;
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
 * Integration tests for the {@link OficinaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OficinaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DETALLE = "AAAAAAAAAA";
    private static final String UPDATED_DETALLE = "BBBBBBBBBB";

    private static final String DEFAULT_UBICACION = "AAAAAAAAAA";
    private static final String UPDATED_UBICACION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/oficinas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OficinaRepository oficinaRepository;

    @Mock
    private OficinaRepository oficinaRepositoryMock;

    @Mock
    private OficinaService oficinaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOficinaMockMvc;

    private Oficina oficina;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Oficina createEntity(EntityManager em) {
        Oficina oficina = new Oficina().nombre(DEFAULT_NOMBRE).detalle(DEFAULT_DETALLE).ubicacion(DEFAULT_UBICACION);
        return oficina;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Oficina createUpdatedEntity(EntityManager em) {
        Oficina oficina = new Oficina().nombre(UPDATED_NOMBRE).detalle(UPDATED_DETALLE).ubicacion(UPDATED_UBICACION);
        return oficina;
    }

    @BeforeEach
    public void initTest() {
        oficina = createEntity(em);
    }

    @Test
    @Transactional
    void createOficina() throws Exception {
        int databaseSizeBeforeCreate = oficinaRepository.findAll().size();
        // Create the Oficina
        restOficinaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oficina)))
            .andExpect(status().isCreated());

        // Validate the Oficina in the database
        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeCreate + 1);
        Oficina testOficina = oficinaList.get(oficinaList.size() - 1);
        assertThat(testOficina.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testOficina.getDetalle()).isEqualTo(DEFAULT_DETALLE);
        assertThat(testOficina.getUbicacion()).isEqualTo(DEFAULT_UBICACION);
    }

    @Test
    @Transactional
    void createOficinaWithExistingId() throws Exception {
        // Create the Oficina with an existing ID
        oficina.setId(1L);

        int databaseSizeBeforeCreate = oficinaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOficinaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oficina)))
            .andExpect(status().isBadRequest());

        // Validate the Oficina in the database
        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOficinas() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);

        // Get all the oficinaList
        restOficinaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oficina.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].detalle").value(hasItem(DEFAULT_DETALLE.toString())))
            .andExpect(jsonPath("$.[*].ubicacion").value(hasItem(DEFAULT_UBICACION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOficinasWithEagerRelationshipsIsEnabled() throws Exception {
        when(oficinaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOficinaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(oficinaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOficinasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(oficinaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOficinaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(oficinaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getOficina() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);

        // Get the oficina
        restOficinaMockMvc
            .perform(get(ENTITY_API_URL_ID, oficina.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(oficina.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.detalle").value(DEFAULT_DETALLE.toString()))
            .andExpect(jsonPath("$.ubicacion").value(DEFAULT_UBICACION));
    }

    @Test
    @Transactional
    void getOficinasByIdFiltering() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);

        Long id = oficina.getId();

        defaultOficinaShouldBeFound("id.equals=" + id);
        defaultOficinaShouldNotBeFound("id.notEquals=" + id);

        defaultOficinaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOficinaShouldNotBeFound("id.greaterThan=" + id);

        defaultOficinaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOficinaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOficinasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);

        // Get all the oficinaList where nombre equals to DEFAULT_NOMBRE
        defaultOficinaShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the oficinaList where nombre equals to UPDATED_NOMBRE
        defaultOficinaShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllOficinasByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);

        // Get all the oficinaList where nombre not equals to DEFAULT_NOMBRE
        defaultOficinaShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the oficinaList where nombre not equals to UPDATED_NOMBRE
        defaultOficinaShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllOficinasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);

        // Get all the oficinaList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultOficinaShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the oficinaList where nombre equals to UPDATED_NOMBRE
        defaultOficinaShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllOficinasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);

        // Get all the oficinaList where nombre is not null
        defaultOficinaShouldBeFound("nombre.specified=true");

        // Get all the oficinaList where nombre is null
        defaultOficinaShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllOficinasByNombreContainsSomething() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);

        // Get all the oficinaList where nombre contains DEFAULT_NOMBRE
        defaultOficinaShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the oficinaList where nombre contains UPDATED_NOMBRE
        defaultOficinaShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllOficinasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);

        // Get all the oficinaList where nombre does not contain DEFAULT_NOMBRE
        defaultOficinaShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the oficinaList where nombre does not contain UPDATED_NOMBRE
        defaultOficinaShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllOficinasByUbicacionIsEqualToSomething() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);

        // Get all the oficinaList where ubicacion equals to DEFAULT_UBICACION
        defaultOficinaShouldBeFound("ubicacion.equals=" + DEFAULT_UBICACION);

        // Get all the oficinaList where ubicacion equals to UPDATED_UBICACION
        defaultOficinaShouldNotBeFound("ubicacion.equals=" + UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void getAllOficinasByUbicacionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);

        // Get all the oficinaList where ubicacion not equals to DEFAULT_UBICACION
        defaultOficinaShouldNotBeFound("ubicacion.notEquals=" + DEFAULT_UBICACION);

        // Get all the oficinaList where ubicacion not equals to UPDATED_UBICACION
        defaultOficinaShouldBeFound("ubicacion.notEquals=" + UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void getAllOficinasByUbicacionIsInShouldWork() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);

        // Get all the oficinaList where ubicacion in DEFAULT_UBICACION or UPDATED_UBICACION
        defaultOficinaShouldBeFound("ubicacion.in=" + DEFAULT_UBICACION + "," + UPDATED_UBICACION);

        // Get all the oficinaList where ubicacion equals to UPDATED_UBICACION
        defaultOficinaShouldNotBeFound("ubicacion.in=" + UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void getAllOficinasByUbicacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);

        // Get all the oficinaList where ubicacion is not null
        defaultOficinaShouldBeFound("ubicacion.specified=true");

        // Get all the oficinaList where ubicacion is null
        defaultOficinaShouldNotBeFound("ubicacion.specified=false");
    }

    @Test
    @Transactional
    void getAllOficinasByUbicacionContainsSomething() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);

        // Get all the oficinaList where ubicacion contains DEFAULT_UBICACION
        defaultOficinaShouldBeFound("ubicacion.contains=" + DEFAULT_UBICACION);

        // Get all the oficinaList where ubicacion contains UPDATED_UBICACION
        defaultOficinaShouldNotBeFound("ubicacion.contains=" + UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void getAllOficinasByUbicacionNotContainsSomething() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);

        // Get all the oficinaList where ubicacion does not contain DEFAULT_UBICACION
        defaultOficinaShouldNotBeFound("ubicacion.doesNotContain=" + DEFAULT_UBICACION);

        // Get all the oficinaList where ubicacion does not contain UPDATED_UBICACION
        defaultOficinaShouldBeFound("ubicacion.doesNotContain=" + UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void getAllOficinasByProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);
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
        oficina.addProducto(producto);
        oficinaRepository.saveAndFlush(oficina);
        Long productoId = producto.getId();

        // Get all the oficinaList where producto equals to productoId
        defaultOficinaShouldBeFound("productoId.equals=" + productoId);

        // Get all the oficinaList where producto equals to (productoId + 1)
        defaultOficinaShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    @Test
    @Transactional
    void getAllOficinasByUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);
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
        oficina.addUsuario(usuario);
        oficinaRepository.saveAndFlush(oficina);
        Long usuarioId = usuario.getId();

        // Get all the oficinaList where usuario equals to usuarioId
        defaultOficinaShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the oficinaList where usuario equals to (usuarioId + 1)
        defaultOficinaShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    @Test
    @Transactional
    void getAllOficinasBySucursalIsEqualToSomething() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);
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
        oficina.setSucursal(sucursal);
        oficinaRepository.saveAndFlush(oficina);
        Long sucursalId = sucursal.getId();

        // Get all the oficinaList where sucursal equals to sucursalId
        defaultOficinaShouldBeFound("sucursalId.equals=" + sucursalId);

        // Get all the oficinaList where sucursal equals to (sucursalId + 1)
        defaultOficinaShouldNotBeFound("sucursalId.equals=" + (sucursalId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOficinaShouldBeFound(String filter) throws Exception {
        restOficinaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oficina.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].detalle").value(hasItem(DEFAULT_DETALLE.toString())))
            .andExpect(jsonPath("$.[*].ubicacion").value(hasItem(DEFAULT_UBICACION)));

        // Check, that the count call also returns 1
        restOficinaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOficinaShouldNotBeFound(String filter) throws Exception {
        restOficinaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOficinaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOficina() throws Exception {
        // Get the oficina
        restOficinaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOficina() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);

        int databaseSizeBeforeUpdate = oficinaRepository.findAll().size();

        // Update the oficina
        Oficina updatedOficina = oficinaRepository.findById(oficina.getId()).get();
        // Disconnect from session so that the updates on updatedOficina are not directly saved in db
        em.detach(updatedOficina);
        updatedOficina.nombre(UPDATED_NOMBRE).detalle(UPDATED_DETALLE).ubicacion(UPDATED_UBICACION);

        restOficinaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOficina.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOficina))
            )
            .andExpect(status().isOk());

        // Validate the Oficina in the database
        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeUpdate);
        Oficina testOficina = oficinaList.get(oficinaList.size() - 1);
        assertThat(testOficina.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testOficina.getDetalle()).isEqualTo(UPDATED_DETALLE);
        assertThat(testOficina.getUbicacion()).isEqualTo(UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void putNonExistingOficina() throws Exception {
        int databaseSizeBeforeUpdate = oficinaRepository.findAll().size();
        oficina.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOficinaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, oficina.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oficina))
            )
            .andExpect(status().isBadRequest());

        // Validate the Oficina in the database
        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOficina() throws Exception {
        int databaseSizeBeforeUpdate = oficinaRepository.findAll().size();
        oficina.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOficinaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oficina))
            )
            .andExpect(status().isBadRequest());

        // Validate the Oficina in the database
        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOficina() throws Exception {
        int databaseSizeBeforeUpdate = oficinaRepository.findAll().size();
        oficina.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOficinaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oficina)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Oficina in the database
        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOficinaWithPatch() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);

        int databaseSizeBeforeUpdate = oficinaRepository.findAll().size();

        // Update the oficina using partial update
        Oficina partialUpdatedOficina = new Oficina();
        partialUpdatedOficina.setId(oficina.getId());

        partialUpdatedOficina.nombre(UPDATED_NOMBRE).detalle(UPDATED_DETALLE);

        restOficinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOficina.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOficina))
            )
            .andExpect(status().isOk());

        // Validate the Oficina in the database
        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeUpdate);
        Oficina testOficina = oficinaList.get(oficinaList.size() - 1);
        assertThat(testOficina.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testOficina.getDetalle()).isEqualTo(UPDATED_DETALLE);
        assertThat(testOficina.getUbicacion()).isEqualTo(DEFAULT_UBICACION);
    }

    @Test
    @Transactional
    void fullUpdateOficinaWithPatch() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);

        int databaseSizeBeforeUpdate = oficinaRepository.findAll().size();

        // Update the oficina using partial update
        Oficina partialUpdatedOficina = new Oficina();
        partialUpdatedOficina.setId(oficina.getId());

        partialUpdatedOficina.nombre(UPDATED_NOMBRE).detalle(UPDATED_DETALLE).ubicacion(UPDATED_UBICACION);

        restOficinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOficina.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOficina))
            )
            .andExpect(status().isOk());

        // Validate the Oficina in the database
        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeUpdate);
        Oficina testOficina = oficinaList.get(oficinaList.size() - 1);
        assertThat(testOficina.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testOficina.getDetalle()).isEqualTo(UPDATED_DETALLE);
        assertThat(testOficina.getUbicacion()).isEqualTo(UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void patchNonExistingOficina() throws Exception {
        int databaseSizeBeforeUpdate = oficinaRepository.findAll().size();
        oficina.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOficinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, oficina.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oficina))
            )
            .andExpect(status().isBadRequest());

        // Validate the Oficina in the database
        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOficina() throws Exception {
        int databaseSizeBeforeUpdate = oficinaRepository.findAll().size();
        oficina.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOficinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oficina))
            )
            .andExpect(status().isBadRequest());

        // Validate the Oficina in the database
        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOficina() throws Exception {
        int databaseSizeBeforeUpdate = oficinaRepository.findAll().size();
        oficina.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOficinaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(oficina)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Oficina in the database
        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOficina() throws Exception {
        // Initialize the database
        oficinaRepository.saveAndFlush(oficina);

        int databaseSizeBeforeDelete = oficinaRepository.findAll().size();

        // Delete the oficina
        restOficinaMockMvc
            .perform(delete(ENTITY_API_URL_ID, oficina.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Oficina> oficinaList = oficinaRepository.findAll();
        assertThat(oficinaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
