package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TipoUsuario;
import com.mycompany.myapp.repository.TipoUsuarioRepository;
import com.mycompany.myapp.service.TipoUsuarioQueryService;
import com.mycompany.myapp.service.TipoUsuarioService;
import com.mycompany.myapp.service.criteria.TipoUsuarioCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.TipoUsuario}.
 */
@RestController
@RequestMapping("/api")
public class TipoUsuarioResource {

    private final Logger log = LoggerFactory.getLogger(TipoUsuarioResource.class);

    private static final String ENTITY_NAME = "tipoUsuario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoUsuarioService tipoUsuarioService;

    private final TipoUsuarioRepository tipoUsuarioRepository;

    private final TipoUsuarioQueryService tipoUsuarioQueryService;

    public TipoUsuarioResource(
        TipoUsuarioService tipoUsuarioService,
        TipoUsuarioRepository tipoUsuarioRepository,
        TipoUsuarioQueryService tipoUsuarioQueryService
    ) {
        this.tipoUsuarioService = tipoUsuarioService;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.tipoUsuarioQueryService = tipoUsuarioQueryService;
    }

    /**
     * {@code POST  /tipo-usuarios} : Create a new tipoUsuario.
     *
     * @param tipoUsuario the tipoUsuario to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoUsuario, or with status {@code 400 (Bad Request)} if the tipoUsuario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-usuarios")
    public ResponseEntity<TipoUsuario> createTipoUsuario(@RequestBody TipoUsuario tipoUsuario) throws URISyntaxException {
        log.debug("REST request to save TipoUsuario : {}", tipoUsuario);
        if (tipoUsuario.getId() != null) {
            throw new BadRequestAlertException("A new tipoUsuario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoUsuario result = tipoUsuarioService.save(tipoUsuario);
        return ResponseEntity
            .created(new URI("/api/tipo-usuarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-usuarios/:id} : Updates an existing tipoUsuario.
     *
     * @param id the id of the tipoUsuario to save.
     * @param tipoUsuario the tipoUsuario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoUsuario,
     * or with status {@code 400 (Bad Request)} if the tipoUsuario is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoUsuario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-usuarios/{id}")
    public ResponseEntity<TipoUsuario> updateTipoUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TipoUsuario tipoUsuario
    ) throws URISyntaxException {
        log.debug("REST request to update TipoUsuario : {}, {}", id, tipoUsuario);
        if (tipoUsuario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoUsuario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoUsuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TipoUsuario result = tipoUsuarioService.save(tipoUsuario);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoUsuario.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tipo-usuarios/:id} : Partial updates given fields of an existing tipoUsuario, field will ignore if it is null
     *
     * @param id the id of the tipoUsuario to save.
     * @param tipoUsuario the tipoUsuario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoUsuario,
     * or with status {@code 400 (Bad Request)} if the tipoUsuario is not valid,
     * or with status {@code 404 (Not Found)} if the tipoUsuario is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoUsuario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tipo-usuarios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoUsuario> partialUpdateTipoUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TipoUsuario tipoUsuario
    ) throws URISyntaxException {
        log.debug("REST request to partial update TipoUsuario partially : {}, {}", id, tipoUsuario);
        if (tipoUsuario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoUsuario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoUsuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoUsuario> result = tipoUsuarioService.partialUpdate(tipoUsuario);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoUsuario.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-usuarios} : get all the tipoUsuarios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoUsuarios in body.
     */
    @GetMapping("/tipo-usuarios")
    public ResponseEntity<List<TipoUsuario>> getAllTipoUsuarios(
        TipoUsuarioCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TipoUsuarios by criteria: {}", criteria);
        Page<TipoUsuario> page = tipoUsuarioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipo-usuarios/count} : count all the tipoUsuarios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tipo-usuarios/count")
    public ResponseEntity<Long> countTipoUsuarios(TipoUsuarioCriteria criteria) {
        log.debug("REST request to count TipoUsuarios by criteria: {}", criteria);
        return ResponseEntity.ok().body(tipoUsuarioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tipo-usuarios/:id} : get the "id" tipoUsuario.
     *
     * @param id the id of the tipoUsuario to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoUsuario, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-usuarios/{id}")
    public ResponseEntity<TipoUsuario> getTipoUsuario(@PathVariable Long id) {
        log.debug("REST request to get TipoUsuario : {}", id);
        Optional<TipoUsuario> tipoUsuario = tipoUsuarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoUsuario);
    }

    /**
     * {@code DELETE  /tipo-usuarios/:id} : delete the "id" tipoUsuario.
     *
     * @param id the id of the tipoUsuario to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-usuarios/{id}")
    public ResponseEntity<Void> deleteTipoUsuario(@PathVariable Long id) {
        log.debug("REST request to delete TipoUsuario : {}", id);
        tipoUsuarioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
