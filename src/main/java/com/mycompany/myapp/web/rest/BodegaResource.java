package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Bodega;
import com.mycompany.myapp.repository.BodegaRepository;
import com.mycompany.myapp.service.BodegaQueryService;
import com.mycompany.myapp.service.BodegaService;
import com.mycompany.myapp.service.criteria.BodegaCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Bodega}.
 */
@RestController
@RequestMapping("/api")
public class BodegaResource {

    private final Logger log = LoggerFactory.getLogger(BodegaResource.class);

    private static final String ENTITY_NAME = "bodega";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BodegaService bodegaService;

    private final BodegaRepository bodegaRepository;

    private final BodegaQueryService bodegaQueryService;

    public BodegaResource(BodegaService bodegaService, BodegaRepository bodegaRepository, BodegaQueryService bodegaQueryService) {
        this.bodegaService = bodegaService;
        this.bodegaRepository = bodegaRepository;
        this.bodegaQueryService = bodegaQueryService;
    }

    /**
     * {@code POST  /bodegas} : Create a new bodega.
     *
     * @param bodega the bodega to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bodega, or with status {@code 400 (Bad Request)} if the bodega has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bodegas")
    public ResponseEntity<Bodega> createBodega(@RequestBody Bodega bodega) throws URISyntaxException {
        log.debug("REST request to save Bodega : {}", bodega);
        if (bodega.getId() != null) {
            throw new BadRequestAlertException("A new bodega cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bodega result = bodegaService.save(bodega);
        return ResponseEntity
            .created(new URI("/api/bodegas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bodegas/:id} : Updates an existing bodega.
     *
     * @param id the id of the bodega to save.
     * @param bodega the bodega to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bodega,
     * or with status {@code 400 (Bad Request)} if the bodega is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bodega couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bodegas/{id}")
    public ResponseEntity<Bodega> updateBodega(@PathVariable(value = "id", required = false) final Long id, @RequestBody Bodega bodega)
        throws URISyntaxException {
        log.debug("REST request to update Bodega : {}, {}", id, bodega);
        if (bodega.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bodega.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bodegaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Bodega result = bodegaService.save(bodega);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bodega.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bodegas/:id} : Partial updates given fields of an existing bodega, field will ignore if it is null
     *
     * @param id the id of the bodega to save.
     * @param bodega the bodega to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bodega,
     * or with status {@code 400 (Bad Request)} if the bodega is not valid,
     * or with status {@code 404 (Not Found)} if the bodega is not found,
     * or with status {@code 500 (Internal Server Error)} if the bodega couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bodegas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Bodega> partialUpdateBodega(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Bodega bodega
    ) throws URISyntaxException {
        log.debug("REST request to partial update Bodega partially : {}, {}", id, bodega);
        if (bodega.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bodega.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bodegaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Bodega> result = bodegaService.partialUpdate(bodega);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bodega.getId().toString())
        );
    }

    /**
     * {@code GET  /bodegas} : get all the bodegas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bodegas in body.
     */
    @GetMapping("/bodegas")
    public ResponseEntity<List<Bodega>> getAllBodegas(
        BodegaCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Bodegas by criteria: {}", criteria);
        Page<Bodega> page = bodegaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bodegas/count} : count all the bodegas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/bodegas/count")
    public ResponseEntity<Long> countBodegas(BodegaCriteria criteria) {
        log.debug("REST request to count Bodegas by criteria: {}", criteria);
        return ResponseEntity.ok().body(bodegaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bodegas/:id} : get the "id" bodega.
     *
     * @param id the id of the bodega to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bodega, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bodegas/{id}")
    public ResponseEntity<Bodega> getBodega(@PathVariable Long id) {
        log.debug("REST request to get Bodega : {}", id);
        Optional<Bodega> bodega = bodegaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bodega);
    }

    /**
     * {@code DELETE  /bodegas/:id} : delete the "id" bodega.
     *
     * @param id the id of the bodega to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bodegas/{id}")
    public ResponseEntity<Void> deleteBodega(@PathVariable Long id) {
        log.debug("REST request to delete Bodega : {}", id);
        bodegaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
