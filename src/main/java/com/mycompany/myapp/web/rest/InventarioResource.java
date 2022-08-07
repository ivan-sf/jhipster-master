package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Inventario;
import com.mycompany.myapp.repository.InventarioRepository;
import com.mycompany.myapp.service.InventarioQueryService;
import com.mycompany.myapp.service.InventarioService;
import com.mycompany.myapp.service.criteria.InventarioCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Inventario}.
 */
@RestController
@RequestMapping("/api")
public class InventarioResource {

    private final Logger log = LoggerFactory.getLogger(InventarioResource.class);

    private static final String ENTITY_NAME = "inventario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InventarioService inventarioService;

    private final InventarioRepository inventarioRepository;

    private final InventarioQueryService inventarioQueryService;

    public InventarioResource(
        InventarioService inventarioService,
        InventarioRepository inventarioRepository,
        InventarioQueryService inventarioQueryService
    ) {
        this.inventarioService = inventarioService;
        this.inventarioRepository = inventarioRepository;
        this.inventarioQueryService = inventarioQueryService;
    }

    /**
     * {@code POST  /inventarios} : Create a new inventario.
     *
     * @param inventario the inventario to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inventario, or with status {@code 400 (Bad Request)} if the inventario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inventarios")
    public ResponseEntity<Inventario> createInventario(@RequestBody Inventario inventario) throws URISyntaxException {
        log.debug("REST request to save Inventario : {}", inventario);
        if (inventario.getId() != null) {
            throw new BadRequestAlertException("A new inventario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Inventario result = inventarioService.save(inventario);
        return ResponseEntity
            .created(new URI("/api/inventarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inventarios/:id} : Updates an existing inventario.
     *
     * @param id the id of the inventario to save.
     * @param inventario the inventario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventario,
     * or with status {@code 400 (Bad Request)} if the inventario is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inventario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inventarios/{id}")
    public ResponseEntity<Inventario> updateInventario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Inventario inventario
    ) throws URISyntaxException {
        log.debug("REST request to update Inventario : {}, {}", id, inventario);
        if (inventario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inventarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Inventario result = inventarioService.save(inventario);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, inventario.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /inventarios/:id} : Partial updates given fields of an existing inventario, field will ignore if it is null
     *
     * @param id the id of the inventario to save.
     * @param inventario the inventario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventario,
     * or with status {@code 400 (Bad Request)} if the inventario is not valid,
     * or with status {@code 404 (Not Found)} if the inventario is not found,
     * or with status {@code 500 (Internal Server Error)} if the inventario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/inventarios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Inventario> partialUpdateInventario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Inventario inventario
    ) throws URISyntaxException {
        log.debug("REST request to partial update Inventario partially : {}, {}", id, inventario);
        if (inventario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inventarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Inventario> result = inventarioService.partialUpdate(inventario);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, inventario.getId().toString())
        );
    }

    /**
     * {@code GET  /inventarios} : get all the inventarios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inventarios in body.
     */
    @GetMapping("/inventarios")
    public ResponseEntity<List<Inventario>> getAllInventarios(
        InventarioCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Inventarios by criteria: {}", criteria);
        Page<Inventario> page = inventarioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inventarios/count} : count all the inventarios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/inventarios/count")
    public ResponseEntity<Long> countInventarios(InventarioCriteria criteria) {
        log.debug("REST request to count Inventarios by criteria: {}", criteria);
        return ResponseEntity.ok().body(inventarioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /inventarios/:id} : get the "id" inventario.
     *
     * @param id the id of the inventario to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inventario, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inventarios/{id}")
    public ResponseEntity<Inventario> getInventario(@PathVariable Long id) {
        log.debug("REST request to get Inventario : {}", id);
        Optional<Inventario> inventario = inventarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inventario);
    }

    /**
     * {@code DELETE  /inventarios/:id} : delete the "id" inventario.
     *
     * @param id the id of the inventario to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inventarios/{id}")
    public ResponseEntity<Void> deleteInventario(@PathVariable Long id) {
        log.debug("REST request to delete Inventario : {}", id);
        inventarioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
