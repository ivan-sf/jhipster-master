package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ComprobanteContable;
import com.mycompany.myapp.repository.ComprobanteContableRepository;
import com.mycompany.myapp.service.ComprobanteContableQueryService;
import com.mycompany.myapp.service.ComprobanteContableService;
import com.mycompany.myapp.service.criteria.ComprobanteContableCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ComprobanteContable}.
 */
@RestController
@RequestMapping("/api")
public class ComprobanteContableResource {

    private final Logger log = LoggerFactory.getLogger(ComprobanteContableResource.class);

    private static final String ENTITY_NAME = "comprobanteContable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComprobanteContableService comprobanteContableService;

    private final ComprobanteContableRepository comprobanteContableRepository;

    private final ComprobanteContableQueryService comprobanteContableQueryService;

    public ComprobanteContableResource(
        ComprobanteContableService comprobanteContableService,
        ComprobanteContableRepository comprobanteContableRepository,
        ComprobanteContableQueryService comprobanteContableQueryService
    ) {
        this.comprobanteContableService = comprobanteContableService;
        this.comprobanteContableRepository = comprobanteContableRepository;
        this.comprobanteContableQueryService = comprobanteContableQueryService;
    }

    /**
     * {@code POST  /comprobante-contables} : Create a new comprobanteContable.
     *
     * @param comprobanteContable the comprobanteContable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new comprobanteContable, or with status {@code 400 (Bad Request)} if the comprobanteContable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comprobante-contables")
    public ResponseEntity<ComprobanteContable> createComprobanteContable(@RequestBody ComprobanteContable comprobanteContable)
        throws URISyntaxException {
        log.debug("REST request to save ComprobanteContable : {}", comprobanteContable);
        if (comprobanteContable.getId() != null) {
            throw new BadRequestAlertException("A new comprobanteContable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComprobanteContable result = comprobanteContableService.save(comprobanteContable);
        return ResponseEntity
            .created(new URI("/api/comprobante-contables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comprobante-contables/:id} : Updates an existing comprobanteContable.
     *
     * @param id the id of the comprobanteContable to save.
     * @param comprobanteContable the comprobanteContable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comprobanteContable,
     * or with status {@code 400 (Bad Request)} if the comprobanteContable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the comprobanteContable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comprobante-contables/{id}")
    public ResponseEntity<ComprobanteContable> updateComprobanteContable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ComprobanteContable comprobanteContable
    ) throws URISyntaxException {
        log.debug("REST request to update ComprobanteContable : {}, {}", id, comprobanteContable);
        if (comprobanteContable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comprobanteContable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!comprobanteContableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ComprobanteContable result = comprobanteContableService.save(comprobanteContable);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, comprobanteContable.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /comprobante-contables/:id} : Partial updates given fields of an existing comprobanteContable, field will ignore if it is null
     *
     * @param id the id of the comprobanteContable to save.
     * @param comprobanteContable the comprobanteContable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comprobanteContable,
     * or with status {@code 400 (Bad Request)} if the comprobanteContable is not valid,
     * or with status {@code 404 (Not Found)} if the comprobanteContable is not found,
     * or with status {@code 500 (Internal Server Error)} if the comprobanteContable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/comprobante-contables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ComprobanteContable> partialUpdateComprobanteContable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ComprobanteContable comprobanteContable
    ) throws URISyntaxException {
        log.debug("REST request to partial update ComprobanteContable partially : {}, {}", id, comprobanteContable);
        if (comprobanteContable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comprobanteContable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!comprobanteContableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ComprobanteContable> result = comprobanteContableService.partialUpdate(comprobanteContable);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, comprobanteContable.getId().toString())
        );
    }

    /**
     * {@code GET  /comprobante-contables} : get all the comprobanteContables.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comprobanteContables in body.
     */
    @GetMapping("/comprobante-contables")
    public ResponseEntity<List<ComprobanteContable>> getAllComprobanteContables(
        ComprobanteContableCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ComprobanteContables by criteria: {}", criteria);
        Page<ComprobanteContable> page = comprobanteContableQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /comprobante-contables/count} : count all the comprobanteContables.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/comprobante-contables/count")
    public ResponseEntity<Long> countComprobanteContables(ComprobanteContableCriteria criteria) {
        log.debug("REST request to count ComprobanteContables by criteria: {}", criteria);
        return ResponseEntity.ok().body(comprobanteContableQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /comprobante-contables/:id} : get the "id" comprobanteContable.
     *
     * @param id the id of the comprobanteContable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the comprobanteContable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comprobante-contables/{id}")
    public ResponseEntity<ComprobanteContable> getComprobanteContable(@PathVariable Long id) {
        log.debug("REST request to get ComprobanteContable : {}", id);
        Optional<ComprobanteContable> comprobanteContable = comprobanteContableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(comprobanteContable);
    }

    /**
     * {@code DELETE  /comprobante-contables/:id} : delete the "id" comprobanteContable.
     *
     * @param id the id of the comprobanteContable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comprobante-contables/{id}")
    public ResponseEntity<Void> deleteComprobanteContable(@PathVariable Long id) {
        log.debug("REST request to delete ComprobanteContable : {}", id);
        comprobanteContableService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
