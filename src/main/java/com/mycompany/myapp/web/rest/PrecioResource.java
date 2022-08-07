package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Precio;
import com.mycompany.myapp.repository.PrecioRepository;
import com.mycompany.myapp.service.PrecioQueryService;
import com.mycompany.myapp.service.PrecioService;
import com.mycompany.myapp.service.criteria.PrecioCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Precio}.
 */
@RestController
@RequestMapping("/api")
public class PrecioResource {

    private final Logger log = LoggerFactory.getLogger(PrecioResource.class);

    private static final String ENTITY_NAME = "precio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrecioService precioService;

    private final PrecioRepository precioRepository;

    private final PrecioQueryService precioQueryService;

    public PrecioResource(PrecioService precioService, PrecioRepository precioRepository, PrecioQueryService precioQueryService) {
        this.precioService = precioService;
        this.precioRepository = precioRepository;
        this.precioQueryService = precioQueryService;
    }

    /**
     * {@code POST  /precios} : Create a new precio.
     *
     * @param precio the precio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new precio, or with status {@code 400 (Bad Request)} if the precio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/precios")
    public ResponseEntity<Precio> createPrecio(@RequestBody Precio precio) throws URISyntaxException {
        log.debug("REST request to save Precio : {}", precio);
        if (precio.getId() != null) {
            throw new BadRequestAlertException("A new precio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Precio result = precioService.save(precio);
        return ResponseEntity
            .created(new URI("/api/precios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /precios/:id} : Updates an existing precio.
     *
     * @param id the id of the precio to save.
     * @param precio the precio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated precio,
     * or with status {@code 400 (Bad Request)} if the precio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the precio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/precios/{id}")
    public ResponseEntity<Precio> updatePrecio(@PathVariable(value = "id", required = false) final Long id, @RequestBody Precio precio)
        throws URISyntaxException {
        log.debug("REST request to update Precio : {}, {}", id, precio);
        if (precio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, precio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!precioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Precio result = precioService.save(precio);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, precio.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /precios/:id} : Partial updates given fields of an existing precio, field will ignore if it is null
     *
     * @param id the id of the precio to save.
     * @param precio the precio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated precio,
     * or with status {@code 400 (Bad Request)} if the precio is not valid,
     * or with status {@code 404 (Not Found)} if the precio is not found,
     * or with status {@code 500 (Internal Server Error)} if the precio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/precios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Precio> partialUpdatePrecio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Precio precio
    ) throws URISyntaxException {
        log.debug("REST request to partial update Precio partially : {}, {}", id, precio);
        if (precio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, precio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!precioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Precio> result = precioService.partialUpdate(precio);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, precio.getId().toString())
        );
    }

    /**
     * {@code GET  /precios} : get all the precios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of precios in body.
     */
    @GetMapping("/precios")
    public ResponseEntity<List<Precio>> getAllPrecios(
        PrecioCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Precios by criteria: {}", criteria);
        Page<Precio> page = precioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /precios/count} : count all the precios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/precios/count")
    public ResponseEntity<Long> countPrecios(PrecioCriteria criteria) {
        log.debug("REST request to count Precios by criteria: {}", criteria);
        return ResponseEntity.ok().body(precioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /precios/:id} : get the "id" precio.
     *
     * @param id the id of the precio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the precio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/precios/{id}")
    public ResponseEntity<Precio> getPrecio(@PathVariable Long id) {
        log.debug("REST request to get Precio : {}", id);
        Optional<Precio> precio = precioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(precio);
    }

    /**
     * {@code DELETE  /precios/:id} : delete the "id" precio.
     *
     * @param id the id of the precio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/precios/{id}")
    public ResponseEntity<Void> deletePrecio(@PathVariable Long id) {
        log.debug("REST request to delete Precio : {}", id);
        precioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
