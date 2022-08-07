package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Bodega;
import com.mycompany.myapp.domain.Empresa;
import com.mycompany.myapp.domain.InfoLegal;
import com.mycompany.myapp.domain.Oficina;
import com.mycompany.myapp.domain.Rol;
import com.mycompany.myapp.domain.Sucursal;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.Usuario;
import com.mycompany.myapp.repository.UsuarioRepository;
import com.mycompany.myapp.service.UsuarioService;
import com.mycompany.myapp.service.criteria.UsuarioCriteria;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link UsuarioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UsuarioResourceIT {

    private static final String DEFAULT_PRIMER_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_PRIMER_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_SEGUNDO_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_SEGUNDO_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMER_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_PRIMER_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_SEGUNDO_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_SEGUNDO_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_DOCUMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENTO_DV = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTO_DV = "BBBBBBBBBB";

    private static final Instant DEFAULT_EDAD = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EDAD = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_INDICATIVO = "AAAAAAAAAA";
    private static final String UPDATED_INDICATIVO = "BBBBBBBBBB";

    private static final String DEFAULT_CELULAR = "AAAAAAAAAA";
    private static final String UPDATED_CELULAR = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION_GPS = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION_GPS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTO_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioRepository usuarioRepositoryMock;

    @Mock
    private UsuarioService usuarioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsuarioMockMvc;

    private Usuario usuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .primerNombre(DEFAULT_PRIMER_NOMBRE)
            .segundoNombre(DEFAULT_SEGUNDO_NOMBRE)
            .primerApellido(DEFAULT_PRIMER_APELLIDO)
            .segundoApellido(DEFAULT_SEGUNDO_APELLIDO)
            .tipoDocumento(DEFAULT_TIPO_DOCUMENTO)
            .documento(DEFAULT_DOCUMENTO)
            .documentoDV(DEFAULT_DOCUMENTO_DV)
            .edad(DEFAULT_EDAD)
            .indicativo(DEFAULT_INDICATIVO)
            .celular(DEFAULT_CELULAR)
            .direccion(DEFAULT_DIRECCION)
            .direccionGps(DEFAULT_DIRECCION_GPS)
            .foto(DEFAULT_FOTO)
            .fotoContentType(DEFAULT_FOTO_CONTENT_TYPE)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return usuario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createUpdatedEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .primerApellido(UPDATED_PRIMER_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .documento(UPDATED_DOCUMENTO)
            .documentoDV(UPDATED_DOCUMENTO_DV)
            .edad(UPDATED_EDAD)
            .indicativo(UPDATED_INDICATIVO)
            .celular(UPDATED_CELULAR)
            .direccion(UPDATED_DIRECCION)
            .direccionGps(UPDATED_DIRECCION_GPS)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);
        return usuario;
    }

    @BeforeEach
    public void initTest() {
        usuario = createEntity(em);
    }

    @Test
    @Transactional
    void createUsuario() throws Exception {
        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();
        // Create the Usuario
        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuario)))
            .andExpect(status().isCreated());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate + 1);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getPrimerNombre()).isEqualTo(DEFAULT_PRIMER_NOMBRE);
        assertThat(testUsuario.getSegundoNombre()).isEqualTo(DEFAULT_SEGUNDO_NOMBRE);
        assertThat(testUsuario.getPrimerApellido()).isEqualTo(DEFAULT_PRIMER_APELLIDO);
        assertThat(testUsuario.getSegundoApellido()).isEqualTo(DEFAULT_SEGUNDO_APELLIDO);
        assertThat(testUsuario.getTipoDocumento()).isEqualTo(DEFAULT_TIPO_DOCUMENTO);
        assertThat(testUsuario.getDocumento()).isEqualTo(DEFAULT_DOCUMENTO);
        assertThat(testUsuario.getDocumentoDV()).isEqualTo(DEFAULT_DOCUMENTO_DV);
        assertThat(testUsuario.getEdad()).isEqualTo(DEFAULT_EDAD);
        assertThat(testUsuario.getIndicativo()).isEqualTo(DEFAULT_INDICATIVO);
        assertThat(testUsuario.getCelular()).isEqualTo(DEFAULT_CELULAR);
        assertThat(testUsuario.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testUsuario.getDireccionGps()).isEqualTo(DEFAULT_DIRECCION_GPS);
        assertThat(testUsuario.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testUsuario.getFotoContentType()).isEqualTo(DEFAULT_FOTO_CONTENT_TYPE);
        assertThat(testUsuario.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createUsuarioWithExistingId() throws Exception {
        // Create the Usuario with an existing ID
        usuario.setId(1L);

        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuario)))
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUsuarios() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].primerNombre").value(hasItem(DEFAULT_PRIMER_NOMBRE)))
            .andExpect(jsonPath("$.[*].segundoNombre").value(hasItem(DEFAULT_SEGUNDO_NOMBRE)))
            .andExpect(jsonPath("$.[*].primerApellido").value(hasItem(DEFAULT_PRIMER_APELLIDO)))
            .andExpect(jsonPath("$.[*].segundoApellido").value(hasItem(DEFAULT_SEGUNDO_APELLIDO)))
            .andExpect(jsonPath("$.[*].tipoDocumento").value(hasItem(DEFAULT_TIPO_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].documento").value(hasItem(DEFAULT_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].documentoDV").value(hasItem(DEFAULT_DOCUMENTO_DV)))
            .andExpect(jsonPath("$.[*].edad").value(hasItem(DEFAULT_EDAD.toString())))
            .andExpect(jsonPath("$.[*].indicativo").value(hasItem(DEFAULT_INDICATIVO)))
            .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].direccionGps").value(hasItem(DEFAULT_DIRECCION_GPS)))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTO))))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUsuariosWithEagerRelationshipsIsEnabled() throws Exception {
        when(usuarioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUsuarioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(usuarioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUsuariosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(usuarioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUsuarioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(usuarioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get the usuario
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL_ID, usuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(usuario.getId().intValue()))
            .andExpect(jsonPath("$.primerNombre").value(DEFAULT_PRIMER_NOMBRE))
            .andExpect(jsonPath("$.segundoNombre").value(DEFAULT_SEGUNDO_NOMBRE))
            .andExpect(jsonPath("$.primerApellido").value(DEFAULT_PRIMER_APELLIDO))
            .andExpect(jsonPath("$.segundoApellido").value(DEFAULT_SEGUNDO_APELLIDO))
            .andExpect(jsonPath("$.tipoDocumento").value(DEFAULT_TIPO_DOCUMENTO))
            .andExpect(jsonPath("$.documento").value(DEFAULT_DOCUMENTO))
            .andExpect(jsonPath("$.documentoDV").value(DEFAULT_DOCUMENTO_DV))
            .andExpect(jsonPath("$.edad").value(DEFAULT_EDAD.toString()))
            .andExpect(jsonPath("$.indicativo").value(DEFAULT_INDICATIVO))
            .andExpect(jsonPath("$.celular").value(DEFAULT_CELULAR))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.direccionGps").value(DEFAULT_DIRECCION_GPS))
            .andExpect(jsonPath("$.fotoContentType").value(DEFAULT_FOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.foto").value(Base64Utils.encodeToString(DEFAULT_FOTO)))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getUsuariosByIdFiltering() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        Long id = usuario.getId();

        defaultUsuarioShouldBeFound("id.equals=" + id);
        defaultUsuarioShouldNotBeFound("id.notEquals=" + id);

        defaultUsuarioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUsuarioShouldNotBeFound("id.greaterThan=" + id);

        defaultUsuarioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUsuarioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUsuariosByPrimerNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where primerNombre equals to DEFAULT_PRIMER_NOMBRE
        defaultUsuarioShouldBeFound("primerNombre.equals=" + DEFAULT_PRIMER_NOMBRE);

        // Get all the usuarioList where primerNombre equals to UPDATED_PRIMER_NOMBRE
        defaultUsuarioShouldNotBeFound("primerNombre.equals=" + UPDATED_PRIMER_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosByPrimerNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where primerNombre not equals to DEFAULT_PRIMER_NOMBRE
        defaultUsuarioShouldNotBeFound("primerNombre.notEquals=" + DEFAULT_PRIMER_NOMBRE);

        // Get all the usuarioList where primerNombre not equals to UPDATED_PRIMER_NOMBRE
        defaultUsuarioShouldBeFound("primerNombre.notEquals=" + UPDATED_PRIMER_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosByPrimerNombreIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where primerNombre in DEFAULT_PRIMER_NOMBRE or UPDATED_PRIMER_NOMBRE
        defaultUsuarioShouldBeFound("primerNombre.in=" + DEFAULT_PRIMER_NOMBRE + "," + UPDATED_PRIMER_NOMBRE);

        // Get all the usuarioList where primerNombre equals to UPDATED_PRIMER_NOMBRE
        defaultUsuarioShouldNotBeFound("primerNombre.in=" + UPDATED_PRIMER_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosByPrimerNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where primerNombre is not null
        defaultUsuarioShouldBeFound("primerNombre.specified=true");

        // Get all the usuarioList where primerNombre is null
        defaultUsuarioShouldNotBeFound("primerNombre.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByPrimerNombreContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where primerNombre contains DEFAULT_PRIMER_NOMBRE
        defaultUsuarioShouldBeFound("primerNombre.contains=" + DEFAULT_PRIMER_NOMBRE);

        // Get all the usuarioList where primerNombre contains UPDATED_PRIMER_NOMBRE
        defaultUsuarioShouldNotBeFound("primerNombre.contains=" + UPDATED_PRIMER_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosByPrimerNombreNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where primerNombre does not contain DEFAULT_PRIMER_NOMBRE
        defaultUsuarioShouldNotBeFound("primerNombre.doesNotContain=" + DEFAULT_PRIMER_NOMBRE);

        // Get all the usuarioList where primerNombre does not contain UPDATED_PRIMER_NOMBRE
        defaultUsuarioShouldBeFound("primerNombre.doesNotContain=" + UPDATED_PRIMER_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosBySegundoNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoNombre equals to DEFAULT_SEGUNDO_NOMBRE
        defaultUsuarioShouldBeFound("segundoNombre.equals=" + DEFAULT_SEGUNDO_NOMBRE);

        // Get all the usuarioList where segundoNombre equals to UPDATED_SEGUNDO_NOMBRE
        defaultUsuarioShouldNotBeFound("segundoNombre.equals=" + UPDATED_SEGUNDO_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosBySegundoNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoNombre not equals to DEFAULT_SEGUNDO_NOMBRE
        defaultUsuarioShouldNotBeFound("segundoNombre.notEquals=" + DEFAULT_SEGUNDO_NOMBRE);

        // Get all the usuarioList where segundoNombre not equals to UPDATED_SEGUNDO_NOMBRE
        defaultUsuarioShouldBeFound("segundoNombre.notEquals=" + UPDATED_SEGUNDO_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosBySegundoNombreIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoNombre in DEFAULT_SEGUNDO_NOMBRE or UPDATED_SEGUNDO_NOMBRE
        defaultUsuarioShouldBeFound("segundoNombre.in=" + DEFAULT_SEGUNDO_NOMBRE + "," + UPDATED_SEGUNDO_NOMBRE);

        // Get all the usuarioList where segundoNombre equals to UPDATED_SEGUNDO_NOMBRE
        defaultUsuarioShouldNotBeFound("segundoNombre.in=" + UPDATED_SEGUNDO_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosBySegundoNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoNombre is not null
        defaultUsuarioShouldBeFound("segundoNombre.specified=true");

        // Get all the usuarioList where segundoNombre is null
        defaultUsuarioShouldNotBeFound("segundoNombre.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosBySegundoNombreContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoNombre contains DEFAULT_SEGUNDO_NOMBRE
        defaultUsuarioShouldBeFound("segundoNombre.contains=" + DEFAULT_SEGUNDO_NOMBRE);

        // Get all the usuarioList where segundoNombre contains UPDATED_SEGUNDO_NOMBRE
        defaultUsuarioShouldNotBeFound("segundoNombre.contains=" + UPDATED_SEGUNDO_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosBySegundoNombreNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoNombre does not contain DEFAULT_SEGUNDO_NOMBRE
        defaultUsuarioShouldNotBeFound("segundoNombre.doesNotContain=" + DEFAULT_SEGUNDO_NOMBRE);

        // Get all the usuarioList where segundoNombre does not contain UPDATED_SEGUNDO_NOMBRE
        defaultUsuarioShouldBeFound("segundoNombre.doesNotContain=" + UPDATED_SEGUNDO_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosByPrimerApellidoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where primerApellido equals to DEFAULT_PRIMER_APELLIDO
        defaultUsuarioShouldBeFound("primerApellido.equals=" + DEFAULT_PRIMER_APELLIDO);

        // Get all the usuarioList where primerApellido equals to UPDATED_PRIMER_APELLIDO
        defaultUsuarioShouldNotBeFound("primerApellido.equals=" + UPDATED_PRIMER_APELLIDO);
    }

    @Test
    @Transactional
    void getAllUsuariosByPrimerApellidoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where primerApellido not equals to DEFAULT_PRIMER_APELLIDO
        defaultUsuarioShouldNotBeFound("primerApellido.notEquals=" + DEFAULT_PRIMER_APELLIDO);

        // Get all the usuarioList where primerApellido not equals to UPDATED_PRIMER_APELLIDO
        defaultUsuarioShouldBeFound("primerApellido.notEquals=" + UPDATED_PRIMER_APELLIDO);
    }

    @Test
    @Transactional
    void getAllUsuariosByPrimerApellidoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where primerApellido in DEFAULT_PRIMER_APELLIDO or UPDATED_PRIMER_APELLIDO
        defaultUsuarioShouldBeFound("primerApellido.in=" + DEFAULT_PRIMER_APELLIDO + "," + UPDATED_PRIMER_APELLIDO);

        // Get all the usuarioList where primerApellido equals to UPDATED_PRIMER_APELLIDO
        defaultUsuarioShouldNotBeFound("primerApellido.in=" + UPDATED_PRIMER_APELLIDO);
    }

    @Test
    @Transactional
    void getAllUsuariosByPrimerApellidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where primerApellido is not null
        defaultUsuarioShouldBeFound("primerApellido.specified=true");

        // Get all the usuarioList where primerApellido is null
        defaultUsuarioShouldNotBeFound("primerApellido.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByPrimerApellidoContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where primerApellido contains DEFAULT_PRIMER_APELLIDO
        defaultUsuarioShouldBeFound("primerApellido.contains=" + DEFAULT_PRIMER_APELLIDO);

        // Get all the usuarioList where primerApellido contains UPDATED_PRIMER_APELLIDO
        defaultUsuarioShouldNotBeFound("primerApellido.contains=" + UPDATED_PRIMER_APELLIDO);
    }

    @Test
    @Transactional
    void getAllUsuariosByPrimerApellidoNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where primerApellido does not contain DEFAULT_PRIMER_APELLIDO
        defaultUsuarioShouldNotBeFound("primerApellido.doesNotContain=" + DEFAULT_PRIMER_APELLIDO);

        // Get all the usuarioList where primerApellido does not contain UPDATED_PRIMER_APELLIDO
        defaultUsuarioShouldBeFound("primerApellido.doesNotContain=" + UPDATED_PRIMER_APELLIDO);
    }

    @Test
    @Transactional
    void getAllUsuariosBySegundoApellidoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoApellido equals to DEFAULT_SEGUNDO_APELLIDO
        defaultUsuarioShouldBeFound("segundoApellido.equals=" + DEFAULT_SEGUNDO_APELLIDO);

        // Get all the usuarioList where segundoApellido equals to UPDATED_SEGUNDO_APELLIDO
        defaultUsuarioShouldNotBeFound("segundoApellido.equals=" + UPDATED_SEGUNDO_APELLIDO);
    }

    @Test
    @Transactional
    void getAllUsuariosBySegundoApellidoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoApellido not equals to DEFAULT_SEGUNDO_APELLIDO
        defaultUsuarioShouldNotBeFound("segundoApellido.notEquals=" + DEFAULT_SEGUNDO_APELLIDO);

        // Get all the usuarioList where segundoApellido not equals to UPDATED_SEGUNDO_APELLIDO
        defaultUsuarioShouldBeFound("segundoApellido.notEquals=" + UPDATED_SEGUNDO_APELLIDO);
    }

    @Test
    @Transactional
    void getAllUsuariosBySegundoApellidoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoApellido in DEFAULT_SEGUNDO_APELLIDO or UPDATED_SEGUNDO_APELLIDO
        defaultUsuarioShouldBeFound("segundoApellido.in=" + DEFAULT_SEGUNDO_APELLIDO + "," + UPDATED_SEGUNDO_APELLIDO);

        // Get all the usuarioList where segundoApellido equals to UPDATED_SEGUNDO_APELLIDO
        defaultUsuarioShouldNotBeFound("segundoApellido.in=" + UPDATED_SEGUNDO_APELLIDO);
    }

    @Test
    @Transactional
    void getAllUsuariosBySegundoApellidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoApellido is not null
        defaultUsuarioShouldBeFound("segundoApellido.specified=true");

        // Get all the usuarioList where segundoApellido is null
        defaultUsuarioShouldNotBeFound("segundoApellido.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosBySegundoApellidoContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoApellido contains DEFAULT_SEGUNDO_APELLIDO
        defaultUsuarioShouldBeFound("segundoApellido.contains=" + DEFAULT_SEGUNDO_APELLIDO);

        // Get all the usuarioList where segundoApellido contains UPDATED_SEGUNDO_APELLIDO
        defaultUsuarioShouldNotBeFound("segundoApellido.contains=" + UPDATED_SEGUNDO_APELLIDO);
    }

    @Test
    @Transactional
    void getAllUsuariosBySegundoApellidoNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoApellido does not contain DEFAULT_SEGUNDO_APELLIDO
        defaultUsuarioShouldNotBeFound("segundoApellido.doesNotContain=" + DEFAULT_SEGUNDO_APELLIDO);

        // Get all the usuarioList where segundoApellido does not contain UPDATED_SEGUNDO_APELLIDO
        defaultUsuarioShouldBeFound("segundoApellido.doesNotContain=" + UPDATED_SEGUNDO_APELLIDO);
    }

    @Test
    @Transactional
    void getAllUsuariosByTipoDocumentoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where tipoDocumento equals to DEFAULT_TIPO_DOCUMENTO
        defaultUsuarioShouldBeFound("tipoDocumento.equals=" + DEFAULT_TIPO_DOCUMENTO);

        // Get all the usuarioList where tipoDocumento equals to UPDATED_TIPO_DOCUMENTO
        defaultUsuarioShouldNotBeFound("tipoDocumento.equals=" + UPDATED_TIPO_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllUsuariosByTipoDocumentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where tipoDocumento not equals to DEFAULT_TIPO_DOCUMENTO
        defaultUsuarioShouldNotBeFound("tipoDocumento.notEquals=" + DEFAULT_TIPO_DOCUMENTO);

        // Get all the usuarioList where tipoDocumento not equals to UPDATED_TIPO_DOCUMENTO
        defaultUsuarioShouldBeFound("tipoDocumento.notEquals=" + UPDATED_TIPO_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllUsuariosByTipoDocumentoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where tipoDocumento in DEFAULT_TIPO_DOCUMENTO or UPDATED_TIPO_DOCUMENTO
        defaultUsuarioShouldBeFound("tipoDocumento.in=" + DEFAULT_TIPO_DOCUMENTO + "," + UPDATED_TIPO_DOCUMENTO);

        // Get all the usuarioList where tipoDocumento equals to UPDATED_TIPO_DOCUMENTO
        defaultUsuarioShouldNotBeFound("tipoDocumento.in=" + UPDATED_TIPO_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllUsuariosByTipoDocumentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where tipoDocumento is not null
        defaultUsuarioShouldBeFound("tipoDocumento.specified=true");

        // Get all the usuarioList where tipoDocumento is null
        defaultUsuarioShouldNotBeFound("tipoDocumento.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByTipoDocumentoContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where tipoDocumento contains DEFAULT_TIPO_DOCUMENTO
        defaultUsuarioShouldBeFound("tipoDocumento.contains=" + DEFAULT_TIPO_DOCUMENTO);

        // Get all the usuarioList where tipoDocumento contains UPDATED_TIPO_DOCUMENTO
        defaultUsuarioShouldNotBeFound("tipoDocumento.contains=" + UPDATED_TIPO_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllUsuariosByTipoDocumentoNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where tipoDocumento does not contain DEFAULT_TIPO_DOCUMENTO
        defaultUsuarioShouldNotBeFound("tipoDocumento.doesNotContain=" + DEFAULT_TIPO_DOCUMENTO);

        // Get all the usuarioList where tipoDocumento does not contain UPDATED_TIPO_DOCUMENTO
        defaultUsuarioShouldBeFound("tipoDocumento.doesNotContain=" + UPDATED_TIPO_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllUsuariosByDocumentoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where documento equals to DEFAULT_DOCUMENTO
        defaultUsuarioShouldBeFound("documento.equals=" + DEFAULT_DOCUMENTO);

        // Get all the usuarioList where documento equals to UPDATED_DOCUMENTO
        defaultUsuarioShouldNotBeFound("documento.equals=" + UPDATED_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllUsuariosByDocumentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where documento not equals to DEFAULT_DOCUMENTO
        defaultUsuarioShouldNotBeFound("documento.notEquals=" + DEFAULT_DOCUMENTO);

        // Get all the usuarioList where documento not equals to UPDATED_DOCUMENTO
        defaultUsuarioShouldBeFound("documento.notEquals=" + UPDATED_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllUsuariosByDocumentoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where documento in DEFAULT_DOCUMENTO or UPDATED_DOCUMENTO
        defaultUsuarioShouldBeFound("documento.in=" + DEFAULT_DOCUMENTO + "," + UPDATED_DOCUMENTO);

        // Get all the usuarioList where documento equals to UPDATED_DOCUMENTO
        defaultUsuarioShouldNotBeFound("documento.in=" + UPDATED_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllUsuariosByDocumentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where documento is not null
        defaultUsuarioShouldBeFound("documento.specified=true");

        // Get all the usuarioList where documento is null
        defaultUsuarioShouldNotBeFound("documento.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByDocumentoContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where documento contains DEFAULT_DOCUMENTO
        defaultUsuarioShouldBeFound("documento.contains=" + DEFAULT_DOCUMENTO);

        // Get all the usuarioList where documento contains UPDATED_DOCUMENTO
        defaultUsuarioShouldNotBeFound("documento.contains=" + UPDATED_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllUsuariosByDocumentoNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where documento does not contain DEFAULT_DOCUMENTO
        defaultUsuarioShouldNotBeFound("documento.doesNotContain=" + DEFAULT_DOCUMENTO);

        // Get all the usuarioList where documento does not contain UPDATED_DOCUMENTO
        defaultUsuarioShouldBeFound("documento.doesNotContain=" + UPDATED_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllUsuariosByDocumentoDVIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where documentoDV equals to DEFAULT_DOCUMENTO_DV
        defaultUsuarioShouldBeFound("documentoDV.equals=" + DEFAULT_DOCUMENTO_DV);

        // Get all the usuarioList where documentoDV equals to UPDATED_DOCUMENTO_DV
        defaultUsuarioShouldNotBeFound("documentoDV.equals=" + UPDATED_DOCUMENTO_DV);
    }

    @Test
    @Transactional
    void getAllUsuariosByDocumentoDVIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where documentoDV not equals to DEFAULT_DOCUMENTO_DV
        defaultUsuarioShouldNotBeFound("documentoDV.notEquals=" + DEFAULT_DOCUMENTO_DV);

        // Get all the usuarioList where documentoDV not equals to UPDATED_DOCUMENTO_DV
        defaultUsuarioShouldBeFound("documentoDV.notEquals=" + UPDATED_DOCUMENTO_DV);
    }

    @Test
    @Transactional
    void getAllUsuariosByDocumentoDVIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where documentoDV in DEFAULT_DOCUMENTO_DV or UPDATED_DOCUMENTO_DV
        defaultUsuarioShouldBeFound("documentoDV.in=" + DEFAULT_DOCUMENTO_DV + "," + UPDATED_DOCUMENTO_DV);

        // Get all the usuarioList where documentoDV equals to UPDATED_DOCUMENTO_DV
        defaultUsuarioShouldNotBeFound("documentoDV.in=" + UPDATED_DOCUMENTO_DV);
    }

    @Test
    @Transactional
    void getAllUsuariosByDocumentoDVIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where documentoDV is not null
        defaultUsuarioShouldBeFound("documentoDV.specified=true");

        // Get all the usuarioList where documentoDV is null
        defaultUsuarioShouldNotBeFound("documentoDV.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByDocumentoDVContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where documentoDV contains DEFAULT_DOCUMENTO_DV
        defaultUsuarioShouldBeFound("documentoDV.contains=" + DEFAULT_DOCUMENTO_DV);

        // Get all the usuarioList where documentoDV contains UPDATED_DOCUMENTO_DV
        defaultUsuarioShouldNotBeFound("documentoDV.contains=" + UPDATED_DOCUMENTO_DV);
    }

    @Test
    @Transactional
    void getAllUsuariosByDocumentoDVNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where documentoDV does not contain DEFAULT_DOCUMENTO_DV
        defaultUsuarioShouldNotBeFound("documentoDV.doesNotContain=" + DEFAULT_DOCUMENTO_DV);

        // Get all the usuarioList where documentoDV does not contain UPDATED_DOCUMENTO_DV
        defaultUsuarioShouldBeFound("documentoDV.doesNotContain=" + UPDATED_DOCUMENTO_DV);
    }

    @Test
    @Transactional
    void getAllUsuariosByEdadIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where edad equals to DEFAULT_EDAD
        defaultUsuarioShouldBeFound("edad.equals=" + DEFAULT_EDAD);

        // Get all the usuarioList where edad equals to UPDATED_EDAD
        defaultUsuarioShouldNotBeFound("edad.equals=" + UPDATED_EDAD);
    }

    @Test
    @Transactional
    void getAllUsuariosByEdadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where edad not equals to DEFAULT_EDAD
        defaultUsuarioShouldNotBeFound("edad.notEquals=" + DEFAULT_EDAD);

        // Get all the usuarioList where edad not equals to UPDATED_EDAD
        defaultUsuarioShouldBeFound("edad.notEquals=" + UPDATED_EDAD);
    }

    @Test
    @Transactional
    void getAllUsuariosByEdadIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where edad in DEFAULT_EDAD or UPDATED_EDAD
        defaultUsuarioShouldBeFound("edad.in=" + DEFAULT_EDAD + "," + UPDATED_EDAD);

        // Get all the usuarioList where edad equals to UPDATED_EDAD
        defaultUsuarioShouldNotBeFound("edad.in=" + UPDATED_EDAD);
    }

    @Test
    @Transactional
    void getAllUsuariosByEdadIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where edad is not null
        defaultUsuarioShouldBeFound("edad.specified=true");

        // Get all the usuarioList where edad is null
        defaultUsuarioShouldNotBeFound("edad.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByIndicativoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where indicativo equals to DEFAULT_INDICATIVO
        defaultUsuarioShouldBeFound("indicativo.equals=" + DEFAULT_INDICATIVO);

        // Get all the usuarioList where indicativo equals to UPDATED_INDICATIVO
        defaultUsuarioShouldNotBeFound("indicativo.equals=" + UPDATED_INDICATIVO);
    }

    @Test
    @Transactional
    void getAllUsuariosByIndicativoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where indicativo not equals to DEFAULT_INDICATIVO
        defaultUsuarioShouldNotBeFound("indicativo.notEquals=" + DEFAULT_INDICATIVO);

        // Get all the usuarioList where indicativo not equals to UPDATED_INDICATIVO
        defaultUsuarioShouldBeFound("indicativo.notEquals=" + UPDATED_INDICATIVO);
    }

    @Test
    @Transactional
    void getAllUsuariosByIndicativoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where indicativo in DEFAULT_INDICATIVO or UPDATED_INDICATIVO
        defaultUsuarioShouldBeFound("indicativo.in=" + DEFAULT_INDICATIVO + "," + UPDATED_INDICATIVO);

        // Get all the usuarioList where indicativo equals to UPDATED_INDICATIVO
        defaultUsuarioShouldNotBeFound("indicativo.in=" + UPDATED_INDICATIVO);
    }

    @Test
    @Transactional
    void getAllUsuariosByIndicativoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where indicativo is not null
        defaultUsuarioShouldBeFound("indicativo.specified=true");

        // Get all the usuarioList where indicativo is null
        defaultUsuarioShouldNotBeFound("indicativo.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByIndicativoContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where indicativo contains DEFAULT_INDICATIVO
        defaultUsuarioShouldBeFound("indicativo.contains=" + DEFAULT_INDICATIVO);

        // Get all the usuarioList where indicativo contains UPDATED_INDICATIVO
        defaultUsuarioShouldNotBeFound("indicativo.contains=" + UPDATED_INDICATIVO);
    }

    @Test
    @Transactional
    void getAllUsuariosByIndicativoNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where indicativo does not contain DEFAULT_INDICATIVO
        defaultUsuarioShouldNotBeFound("indicativo.doesNotContain=" + DEFAULT_INDICATIVO);

        // Get all the usuarioList where indicativo does not contain UPDATED_INDICATIVO
        defaultUsuarioShouldBeFound("indicativo.doesNotContain=" + UPDATED_INDICATIVO);
    }

    @Test
    @Transactional
    void getAllUsuariosByCelularIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where celular equals to DEFAULT_CELULAR
        defaultUsuarioShouldBeFound("celular.equals=" + DEFAULT_CELULAR);

        // Get all the usuarioList where celular equals to UPDATED_CELULAR
        defaultUsuarioShouldNotBeFound("celular.equals=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    void getAllUsuariosByCelularIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where celular not equals to DEFAULT_CELULAR
        defaultUsuarioShouldNotBeFound("celular.notEquals=" + DEFAULT_CELULAR);

        // Get all the usuarioList where celular not equals to UPDATED_CELULAR
        defaultUsuarioShouldBeFound("celular.notEquals=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    void getAllUsuariosByCelularIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where celular in DEFAULT_CELULAR or UPDATED_CELULAR
        defaultUsuarioShouldBeFound("celular.in=" + DEFAULT_CELULAR + "," + UPDATED_CELULAR);

        // Get all the usuarioList where celular equals to UPDATED_CELULAR
        defaultUsuarioShouldNotBeFound("celular.in=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    void getAllUsuariosByCelularIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where celular is not null
        defaultUsuarioShouldBeFound("celular.specified=true");

        // Get all the usuarioList where celular is null
        defaultUsuarioShouldNotBeFound("celular.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByCelularContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where celular contains DEFAULT_CELULAR
        defaultUsuarioShouldBeFound("celular.contains=" + DEFAULT_CELULAR);

        // Get all the usuarioList where celular contains UPDATED_CELULAR
        defaultUsuarioShouldNotBeFound("celular.contains=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    void getAllUsuariosByCelularNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where celular does not contain DEFAULT_CELULAR
        defaultUsuarioShouldNotBeFound("celular.doesNotContain=" + DEFAULT_CELULAR);

        // Get all the usuarioList where celular does not contain UPDATED_CELULAR
        defaultUsuarioShouldBeFound("celular.doesNotContain=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    void getAllUsuariosByDireccionIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccion equals to DEFAULT_DIRECCION
        defaultUsuarioShouldBeFound("direccion.equals=" + DEFAULT_DIRECCION);

        // Get all the usuarioList where direccion equals to UPDATED_DIRECCION
        defaultUsuarioShouldNotBeFound("direccion.equals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllUsuariosByDireccionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccion not equals to DEFAULT_DIRECCION
        defaultUsuarioShouldNotBeFound("direccion.notEquals=" + DEFAULT_DIRECCION);

        // Get all the usuarioList where direccion not equals to UPDATED_DIRECCION
        defaultUsuarioShouldBeFound("direccion.notEquals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllUsuariosByDireccionIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccion in DEFAULT_DIRECCION or UPDATED_DIRECCION
        defaultUsuarioShouldBeFound("direccion.in=" + DEFAULT_DIRECCION + "," + UPDATED_DIRECCION);

        // Get all the usuarioList where direccion equals to UPDATED_DIRECCION
        defaultUsuarioShouldNotBeFound("direccion.in=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllUsuariosByDireccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccion is not null
        defaultUsuarioShouldBeFound("direccion.specified=true");

        // Get all the usuarioList where direccion is null
        defaultUsuarioShouldNotBeFound("direccion.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByDireccionContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccion contains DEFAULT_DIRECCION
        defaultUsuarioShouldBeFound("direccion.contains=" + DEFAULT_DIRECCION);

        // Get all the usuarioList where direccion contains UPDATED_DIRECCION
        defaultUsuarioShouldNotBeFound("direccion.contains=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllUsuariosByDireccionNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccion does not contain DEFAULT_DIRECCION
        defaultUsuarioShouldNotBeFound("direccion.doesNotContain=" + DEFAULT_DIRECCION);

        // Get all the usuarioList where direccion does not contain UPDATED_DIRECCION
        defaultUsuarioShouldBeFound("direccion.doesNotContain=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllUsuariosByDireccionGpsIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccionGps equals to DEFAULT_DIRECCION_GPS
        defaultUsuarioShouldBeFound("direccionGps.equals=" + DEFAULT_DIRECCION_GPS);

        // Get all the usuarioList where direccionGps equals to UPDATED_DIRECCION_GPS
        defaultUsuarioShouldNotBeFound("direccionGps.equals=" + UPDATED_DIRECCION_GPS);
    }

    @Test
    @Transactional
    void getAllUsuariosByDireccionGpsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccionGps not equals to DEFAULT_DIRECCION_GPS
        defaultUsuarioShouldNotBeFound("direccionGps.notEquals=" + DEFAULT_DIRECCION_GPS);

        // Get all the usuarioList where direccionGps not equals to UPDATED_DIRECCION_GPS
        defaultUsuarioShouldBeFound("direccionGps.notEquals=" + UPDATED_DIRECCION_GPS);
    }

    @Test
    @Transactional
    void getAllUsuariosByDireccionGpsIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccionGps in DEFAULT_DIRECCION_GPS or UPDATED_DIRECCION_GPS
        defaultUsuarioShouldBeFound("direccionGps.in=" + DEFAULT_DIRECCION_GPS + "," + UPDATED_DIRECCION_GPS);

        // Get all the usuarioList where direccionGps equals to UPDATED_DIRECCION_GPS
        defaultUsuarioShouldNotBeFound("direccionGps.in=" + UPDATED_DIRECCION_GPS);
    }

    @Test
    @Transactional
    void getAllUsuariosByDireccionGpsIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccionGps is not null
        defaultUsuarioShouldBeFound("direccionGps.specified=true");

        // Get all the usuarioList where direccionGps is null
        defaultUsuarioShouldNotBeFound("direccionGps.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByDireccionGpsContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccionGps contains DEFAULT_DIRECCION_GPS
        defaultUsuarioShouldBeFound("direccionGps.contains=" + DEFAULT_DIRECCION_GPS);

        // Get all the usuarioList where direccionGps contains UPDATED_DIRECCION_GPS
        defaultUsuarioShouldNotBeFound("direccionGps.contains=" + UPDATED_DIRECCION_GPS);
    }

    @Test
    @Transactional
    void getAllUsuariosByDireccionGpsNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccionGps does not contain DEFAULT_DIRECCION_GPS
        defaultUsuarioShouldNotBeFound("direccionGps.doesNotContain=" + DEFAULT_DIRECCION_GPS);

        // Get all the usuarioList where direccionGps does not contain UPDATED_DIRECCION_GPS
        defaultUsuarioShouldBeFound("direccionGps.doesNotContain=" + UPDATED_DIRECCION_GPS);
    }

    @Test
    @Transactional
    void getAllUsuariosByFechaRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where fechaRegistro equals to DEFAULT_FECHA_REGISTRO
        defaultUsuarioShouldBeFound("fechaRegistro.equals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the usuarioList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultUsuarioShouldNotBeFound("fechaRegistro.equals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllUsuariosByFechaRegistroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where fechaRegistro not equals to DEFAULT_FECHA_REGISTRO
        defaultUsuarioShouldNotBeFound("fechaRegistro.notEquals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the usuarioList where fechaRegistro not equals to UPDATED_FECHA_REGISTRO
        defaultUsuarioShouldBeFound("fechaRegistro.notEquals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllUsuariosByFechaRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where fechaRegistro in DEFAULT_FECHA_REGISTRO or UPDATED_FECHA_REGISTRO
        defaultUsuarioShouldBeFound("fechaRegistro.in=" + DEFAULT_FECHA_REGISTRO + "," + UPDATED_FECHA_REGISTRO);

        // Get all the usuarioList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultUsuarioShouldNotBeFound("fechaRegistro.in=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllUsuariosByFechaRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where fechaRegistro is not null
        defaultUsuarioShouldBeFound("fechaRegistro.specified=true");

        // Get all the usuarioList where fechaRegistro is null
        defaultUsuarioShouldNotBeFound("fechaRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            user = UserResourceIT.createEntity(em);
            em.persist(user);
            em.flush();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        usuario.setUser(user);
        usuarioRepository.saveAndFlush(usuario);
        Long userId = user.getId();

        // Get all the usuarioList where user equals to userId
        defaultUsuarioShouldBeFound("userId.equals=" + userId);

        // Get all the usuarioList where user equals to (userId + 1)
        defaultUsuarioShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByRolIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);
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
        usuario.setRol(rol);
        usuarioRepository.saveAndFlush(usuario);
        Long rolId = rol.getId();

        // Get all the usuarioList where rol equals to rolId
        defaultUsuarioShouldBeFound("rolId.equals=" + rolId);

        // Get all the usuarioList where rol equals to (rolId + 1)
        defaultUsuarioShouldNotBeFound("rolId.equals=" + (rolId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByInfoLegalIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);
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
        usuario.addInfoLegal(infoLegal);
        usuarioRepository.saveAndFlush(usuario);
        Long infoLegalId = infoLegal.getId();

        // Get all the usuarioList where infoLegal equals to infoLegalId
        defaultUsuarioShouldBeFound("infoLegalId.equals=" + infoLegalId);

        // Get all the usuarioList where infoLegal equals to (infoLegalId + 1)
        defaultUsuarioShouldNotBeFound("infoLegalId.equals=" + (infoLegalId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosBySucursalIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);
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
        usuario.addSucursal(sucursal);
        usuarioRepository.saveAndFlush(usuario);
        Long sucursalId = sucursal.getId();

        // Get all the usuarioList where sucursal equals to sucursalId
        defaultUsuarioShouldBeFound("sucursalId.equals=" + sucursalId);

        // Get all the usuarioList where sucursal equals to (sucursalId + 1)
        defaultUsuarioShouldNotBeFound("sucursalId.equals=" + (sucursalId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByEmpresaIdIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);
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
        usuario.addEmpresaId(empresaId);
        usuarioRepository.saveAndFlush(usuario);
        Long empresaIdId = empresaId.getId();

        // Get all the usuarioList where empresaId equals to empresaIdId
        defaultUsuarioShouldBeFound("empresaIdId.equals=" + empresaIdId);

        // Get all the usuarioList where empresaId equals to (empresaIdId + 1)
        defaultUsuarioShouldNotBeFound("empresaIdId.equals=" + (empresaIdId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByBodegaIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);
        Bodega bodega;
        if (TestUtil.findAll(em, Bodega.class).isEmpty()) {
            bodega = BodegaResourceIT.createEntity(em);
            em.persist(bodega);
            em.flush();
        } else {
            bodega = TestUtil.findAll(em, Bodega.class).get(0);
        }
        em.persist(bodega);
        em.flush();
        usuario.addBodega(bodega);
        usuarioRepository.saveAndFlush(usuario);
        Long bodegaId = bodega.getId();

        // Get all the usuarioList where bodega equals to bodegaId
        defaultUsuarioShouldBeFound("bodegaId.equals=" + bodegaId);

        // Get all the usuarioList where bodega equals to (bodegaId + 1)
        defaultUsuarioShouldNotBeFound("bodegaId.equals=" + (bodegaId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByOficinaIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);
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
        usuario.addOficina(oficina);
        usuarioRepository.saveAndFlush(usuario);
        Long oficinaId = oficina.getId();

        // Get all the usuarioList where oficina equals to oficinaId
        defaultUsuarioShouldBeFound("oficinaId.equals=" + oficinaId);

        // Get all the usuarioList where oficina equals to (oficinaId + 1)
        defaultUsuarioShouldNotBeFound("oficinaId.equals=" + (oficinaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUsuarioShouldBeFound(String filter) throws Exception {
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].primerNombre").value(hasItem(DEFAULT_PRIMER_NOMBRE)))
            .andExpect(jsonPath("$.[*].segundoNombre").value(hasItem(DEFAULT_SEGUNDO_NOMBRE)))
            .andExpect(jsonPath("$.[*].primerApellido").value(hasItem(DEFAULT_PRIMER_APELLIDO)))
            .andExpect(jsonPath("$.[*].segundoApellido").value(hasItem(DEFAULT_SEGUNDO_APELLIDO)))
            .andExpect(jsonPath("$.[*].tipoDocumento").value(hasItem(DEFAULT_TIPO_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].documento").value(hasItem(DEFAULT_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].documentoDV").value(hasItem(DEFAULT_DOCUMENTO_DV)))
            .andExpect(jsonPath("$.[*].edad").value(hasItem(DEFAULT_EDAD.toString())))
            .andExpect(jsonPath("$.[*].indicativo").value(hasItem(DEFAULT_INDICATIVO)))
            .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].direccionGps").value(hasItem(DEFAULT_DIRECCION_GPS)))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTO))))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));

        // Check, that the count call also returns 1
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUsuarioShouldNotBeFound(String filter) throws Exception {
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUsuario() throws Exception {
        // Get the usuario
        restUsuarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario
        Usuario updatedUsuario = usuarioRepository.findById(usuario.getId()).get();
        // Disconnect from session so that the updates on updatedUsuario are not directly saved in db
        em.detach(updatedUsuario);
        updatedUsuario
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .primerApellido(UPDATED_PRIMER_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .documento(UPDATED_DOCUMENTO)
            .documentoDV(UPDATED_DOCUMENTO_DV)
            .edad(UPDATED_EDAD)
            .indicativo(UPDATED_INDICATIVO)
            .celular(UPDATED_CELULAR)
            .direccion(UPDATED_DIRECCION)
            .direccionGps(UPDATED_DIRECCION_GPS)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUsuario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getPrimerNombre()).isEqualTo(UPDATED_PRIMER_NOMBRE);
        assertThat(testUsuario.getSegundoNombre()).isEqualTo(UPDATED_SEGUNDO_NOMBRE);
        assertThat(testUsuario.getPrimerApellido()).isEqualTo(UPDATED_PRIMER_APELLIDO);
        assertThat(testUsuario.getSegundoApellido()).isEqualTo(UPDATED_SEGUNDO_APELLIDO);
        assertThat(testUsuario.getTipoDocumento()).isEqualTo(UPDATED_TIPO_DOCUMENTO);
        assertThat(testUsuario.getDocumento()).isEqualTo(UPDATED_DOCUMENTO);
        assertThat(testUsuario.getDocumentoDV()).isEqualTo(UPDATED_DOCUMENTO_DV);
        assertThat(testUsuario.getEdad()).isEqualTo(UPDATED_EDAD);
        assertThat(testUsuario.getIndicativo()).isEqualTo(UPDATED_INDICATIVO);
        assertThat(testUsuario.getCelular()).isEqualTo(UPDATED_CELULAR);
        assertThat(testUsuario.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testUsuario.getDireccionGps()).isEqualTo(UPDATED_DIRECCION_GPS);
        assertThat(testUsuario.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testUsuario.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
        assertThat(testUsuario.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .edad(UPDATED_EDAD)
            .indicativo(UPDATED_INDICATIVO)
            .celular(UPDATED_CELULAR)
            .direccionGps(UPDATED_DIRECCION_GPS)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getPrimerNombre()).isEqualTo(UPDATED_PRIMER_NOMBRE);
        assertThat(testUsuario.getSegundoNombre()).isEqualTo(UPDATED_SEGUNDO_NOMBRE);
        assertThat(testUsuario.getPrimerApellido()).isEqualTo(DEFAULT_PRIMER_APELLIDO);
        assertThat(testUsuario.getSegundoApellido()).isEqualTo(DEFAULT_SEGUNDO_APELLIDO);
        assertThat(testUsuario.getTipoDocumento()).isEqualTo(UPDATED_TIPO_DOCUMENTO);
        assertThat(testUsuario.getDocumento()).isEqualTo(DEFAULT_DOCUMENTO);
        assertThat(testUsuario.getDocumentoDV()).isEqualTo(DEFAULT_DOCUMENTO_DV);
        assertThat(testUsuario.getEdad()).isEqualTo(UPDATED_EDAD);
        assertThat(testUsuario.getIndicativo()).isEqualTo(UPDATED_INDICATIVO);
        assertThat(testUsuario.getCelular()).isEqualTo(UPDATED_CELULAR);
        assertThat(testUsuario.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testUsuario.getDireccionGps()).isEqualTo(UPDATED_DIRECCION_GPS);
        assertThat(testUsuario.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testUsuario.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
        assertThat(testUsuario.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .primerApellido(UPDATED_PRIMER_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .documento(UPDATED_DOCUMENTO)
            .documentoDV(UPDATED_DOCUMENTO_DV)
            .edad(UPDATED_EDAD)
            .indicativo(UPDATED_INDICATIVO)
            .celular(UPDATED_CELULAR)
            .direccion(UPDATED_DIRECCION)
            .direccionGps(UPDATED_DIRECCION_GPS)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getPrimerNombre()).isEqualTo(UPDATED_PRIMER_NOMBRE);
        assertThat(testUsuario.getSegundoNombre()).isEqualTo(UPDATED_SEGUNDO_NOMBRE);
        assertThat(testUsuario.getPrimerApellido()).isEqualTo(UPDATED_PRIMER_APELLIDO);
        assertThat(testUsuario.getSegundoApellido()).isEqualTo(UPDATED_SEGUNDO_APELLIDO);
        assertThat(testUsuario.getTipoDocumento()).isEqualTo(UPDATED_TIPO_DOCUMENTO);
        assertThat(testUsuario.getDocumento()).isEqualTo(UPDATED_DOCUMENTO);
        assertThat(testUsuario.getDocumentoDV()).isEqualTo(UPDATED_DOCUMENTO_DV);
        assertThat(testUsuario.getEdad()).isEqualTo(UPDATED_EDAD);
        assertThat(testUsuario.getIndicativo()).isEqualTo(UPDATED_INDICATIVO);
        assertThat(testUsuario.getCelular()).isEqualTo(UPDATED_CELULAR);
        assertThat(testUsuario.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testUsuario.getDireccionGps()).isEqualTo(UPDATED_DIRECCION_GPS);
        assertThat(testUsuario.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testUsuario.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
        assertThat(testUsuario.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(usuario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeDelete = usuarioRepository.findAll().size();

        // Delete the usuario
        restUsuarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, usuario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
