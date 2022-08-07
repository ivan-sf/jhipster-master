package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Sucursal;
import com.mycompany.myapp.repository.SucursalRepository;
import com.mycompany.myapp.service.SucursalQueryService;
import com.mycompany.myapp.service.SucursalService;
import com.mycompany.myapp.service.criteria.SucursalCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Sucursal}.
 */
@RestController
@RequestMapping("/api")
public class SucursalResource {

    private final Logger log = LoggerFactory.getLogger(SucursalResource.class);

    private static final String ENTITY_NAME = "sucursal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SucursalService sucursalService;

    private final SucursalRepository sucursalRepository;

    private final SucursalQueryService sucursalQueryService;

    public SucursalResource(
        SucursalService sucursalService,
        SucursalRepository sucursalRepository,
        SucursalQueryService sucursalQueryService
    ) {
        this.sucursalService = sucursalService;
        this.sucursalRepository = sucursalRepository;
        this.sucursalQueryService = sucursalQueryService;
    }

    /**
     * {@code POST  /sucursals} : Create a new sucursal.
     *
     * @param sucursal the sucursal to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sucursal, or with status {@code 400 (Bad Request)} if the sucursal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sucursals")
    public ResponseEntity<Sucursal> createSucursal(@RequestBody Sucursal sucursal) throws URISyntaxException {
        log.debug("REST request to save Sucursal : {}", sucursal);
        if (sucursal.getId() != null) {
            throw new BadRequestAlertException("A new sucursal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sucursal result = sucursalService.save(sucursal);
        return ResponseEntity
            .created(new URI("/api/sucursals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sucursals/:id} : Updates an existing sucursal.
     *
     * @param id the id of the sucursal to save.
     * @param sucursal the sucursal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sucursal,
     * or with status {@code 400 (Bad Request)} if the sucursal is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sucursal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sucursals/{id}")
    public ResponseEntity<Sucursal> updateSucursal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Sucursal sucursal
    ) throws URISyntaxException {
        log.debug("REST request to update Sucursal : {}, {}", id, sucursal);
        if (sucursal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sucursal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sucursalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Sucursal result = sucursalService.save(sucursal);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sucursal.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sucursals/:id} : Partial updates given fields of an existing sucursal, field will ignore if it is null
     *
     * @param id the id of the sucursal to save.
     * @param sucursal the sucursal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sucursal,
     * or with status {@code 400 (Bad Request)} if the sucursal is not valid,
     * or with status {@code 404 (Not Found)} if the sucursal is not found,
     * or with status {@code 500 (Internal Server Error)} if the sucursal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sucursals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Sucursal> partialUpdateSucursal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Sucursal sucursal
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sucursal partially : {}, {}", id, sucursal);
        if (sucursal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sucursal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sucursalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Sucursal> result = sucursalService.partialUpdate(sucursal);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sucursal.getId().toString())
        );
    }

    /**
     * {@code GET  /sucursals} : get all the sucursals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sucursals in body.
     */
    @GetMapping("/sucursals")
    public ResponseEntity<List<Sucursal>> getAllSucursals(
        SucursalCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Sucursals by criteria: {}", criteria);
        Page<Sucursal> page = sucursalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sucursals/count} : count all the sucursals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sucursals/count")
    public ResponseEntity<Long> countSucursals(SucursalCriteria criteria) {
        log.debug("REST request to count Sucursals by criteria: {}", criteria);
        return ResponseEntity.ok().body(sucursalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sucursals/:id} : get the "id" sucursal.
     *
     * @param id the id of the sucursal to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sucursal, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sucursals/{id}")
    public ResponseEntity<Sucursal> getSucursal(@PathVariable Long id) {
        log.debug("REST request to get Sucursal : {}", id);
        Optional<Sucursal> sucursal = sucursalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sucursal);
    }

    /**
     * {@code DELETE  /sucursals/:id} : delete the "id" sucursal.
     *
     * @param id the id of the sucursal to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sucursals/{id}")
    public ResponseEntity<Void> deleteSucursal(@PathVariable Long id) {
        log.debug("REST request to delete Sucursal : {}", id);
        sucursalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
