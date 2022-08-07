package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TipoComprobanteContable;
import com.mycompany.myapp.repository.TipoComprobanteContableRepository;
import com.mycompany.myapp.service.TipoComprobanteContableQueryService;
import com.mycompany.myapp.service.TipoComprobanteContableService;
import com.mycompany.myapp.service.criteria.TipoComprobanteContableCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TipoComprobanteContable}.
 */
@RestController
@RequestMapping("/api")
public class TipoComprobanteContableResource {

    private final Logger log = LoggerFactory.getLogger(TipoComprobanteContableResource.class);

    private static final String ENTITY_NAME = "tipoComprobanteContable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoComprobanteContableService tipoComprobanteContableService;

    private final TipoComprobanteContableRepository tipoComprobanteContableRepository;

    private final TipoComprobanteContableQueryService tipoComprobanteContableQueryService;

    public TipoComprobanteContableResource(
        TipoComprobanteContableService tipoComprobanteContableService,
        TipoComprobanteContableRepository tipoComprobanteContableRepository,
        TipoComprobanteContableQueryService tipoComprobanteContableQueryService
    ) {
        this.tipoComprobanteContableService = tipoComprobanteContableService;
        this.tipoComprobanteContableRepository = tipoComprobanteContableRepository;
        this.tipoComprobanteContableQueryService = tipoComprobanteContableQueryService;
    }

    /**
     * {@code POST  /tipo-comprobante-contables} : Create a new tipoComprobanteContable.
     *
     * @param tipoComprobanteContable the tipoComprobanteContable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoComprobanteContable, or with status {@code 400 (Bad Request)} if the tipoComprobanteContable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-comprobante-contables")
    public ResponseEntity<TipoComprobanteContable> createTipoComprobanteContable(
        @RequestBody TipoComprobanteContable tipoComprobanteContable
    ) throws URISyntaxException {
        log.debug("REST request to save TipoComprobanteContable : {}", tipoComprobanteContable);
        if (tipoComprobanteContable.getId() != null) {
            throw new BadRequestAlertException("A new tipoComprobanteContable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoComprobanteContable result = tipoComprobanteContableService.save(tipoComprobanteContable);
        return ResponseEntity
            .created(new URI("/api/tipo-comprobante-contables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-comprobante-contables/:id} : Updates an existing tipoComprobanteContable.
     *
     * @param id the id of the tipoComprobanteContable to save.
     * @param tipoComprobanteContable the tipoComprobanteContable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoComprobanteContable,
     * or with status {@code 400 (Bad Request)} if the tipoComprobanteContable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoComprobanteContable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-comprobante-contables/{id}")
    public ResponseEntity<TipoComprobanteContable> updateTipoComprobanteContable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TipoComprobanteContable tipoComprobanteContable
    ) throws URISyntaxException {
        log.debug("REST request to update TipoComprobanteContable : {}, {}", id, tipoComprobanteContable);
        if (tipoComprobanteContable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoComprobanteContable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoComprobanteContableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TipoComprobanteContable result = tipoComprobanteContableService.save(tipoComprobanteContable);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoComprobanteContable.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tipo-comprobante-contables/:id} : Partial updates given fields of an existing tipoComprobanteContable, field will ignore if it is null
     *
     * @param id the id of the tipoComprobanteContable to save.
     * @param tipoComprobanteContable the tipoComprobanteContable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoComprobanteContable,
     * or with status {@code 400 (Bad Request)} if the tipoComprobanteContable is not valid,
     * or with status {@code 404 (Not Found)} if the tipoComprobanteContable is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoComprobanteContable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tipo-comprobante-contables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoComprobanteContable> partialUpdateTipoComprobanteContable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TipoComprobanteContable tipoComprobanteContable
    ) throws URISyntaxException {
        log.debug("REST request to partial update TipoComprobanteContable partially : {}, {}", id, tipoComprobanteContable);
        if (tipoComprobanteContable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoComprobanteContable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoComprobanteContableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoComprobanteContable> result = tipoComprobanteContableService.partialUpdate(tipoComprobanteContable);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoComprobanteContable.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-comprobante-contables} : get all the tipoComprobanteContables.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoComprobanteContables in body.
     */
    @GetMapping("/tipo-comprobante-contables")
    public ResponseEntity<List<TipoComprobanteContable>> getAllTipoComprobanteContables(
        TipoComprobanteContableCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TipoComprobanteContables by criteria: {}", criteria);
        Page<TipoComprobanteContable> page = tipoComprobanteContableQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipo-comprobante-contables/count} : count all the tipoComprobanteContables.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tipo-comprobante-contables/count")
    public ResponseEntity<Long> countTipoComprobanteContables(TipoComprobanteContableCriteria criteria) {
        log.debug("REST request to count TipoComprobanteContables by criteria: {}", criteria);
        return ResponseEntity.ok().body(tipoComprobanteContableQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tipo-comprobante-contables/:id} : get the "id" tipoComprobanteContable.
     *
     * @param id the id of the tipoComprobanteContable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoComprobanteContable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-comprobante-contables/{id}")
    public ResponseEntity<TipoComprobanteContable> getTipoComprobanteContable(@PathVariable Long id) {
        log.debug("REST request to get TipoComprobanteContable : {}", id);
        Optional<TipoComprobanteContable> tipoComprobanteContable = tipoComprobanteContableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoComprobanteContable);
    }

    /**
     * {@code DELETE  /tipo-comprobante-contables/:id} : delete the "id" tipoComprobanteContable.
     *
     * @param id the id of the tipoComprobanteContable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-comprobante-contables/{id}")
    public ResponseEntity<Void> deleteTipoComprobanteContable(@PathVariable Long id) {
        log.debug("REST request to delete TipoComprobanteContable : {}", id);
        tipoComprobanteContableService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
