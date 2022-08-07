package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Oficina;
import com.mycompany.myapp.repository.OficinaRepository;
import com.mycompany.myapp.service.OficinaQueryService;
import com.mycompany.myapp.service.OficinaService;
import com.mycompany.myapp.service.criteria.OficinaCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Oficina}.
 */
@RestController
@RequestMapping("/api")
public class OficinaResource {

    private final Logger log = LoggerFactory.getLogger(OficinaResource.class);

    private static final String ENTITY_NAME = "oficina";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OficinaService oficinaService;

    private final OficinaRepository oficinaRepository;

    private final OficinaQueryService oficinaQueryService;

    public OficinaResource(OficinaService oficinaService, OficinaRepository oficinaRepository, OficinaQueryService oficinaQueryService) {
        this.oficinaService = oficinaService;
        this.oficinaRepository = oficinaRepository;
        this.oficinaQueryService = oficinaQueryService;
    }

    /**
     * {@code POST  /oficinas} : Create a new oficina.
     *
     * @param oficina the oficina to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new oficina, or with status {@code 400 (Bad Request)} if the oficina has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/oficinas")
    public ResponseEntity<Oficina> createOficina(@RequestBody Oficina oficina) throws URISyntaxException {
        log.debug("REST request to save Oficina : {}", oficina);
        if (oficina.getId() != null) {
            throw new BadRequestAlertException("A new oficina cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Oficina result = oficinaService.save(oficina);
        return ResponseEntity
            .created(new URI("/api/oficinas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /oficinas/:id} : Updates an existing oficina.
     *
     * @param id the id of the oficina to save.
     * @param oficina the oficina to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oficina,
     * or with status {@code 400 (Bad Request)} if the oficina is not valid,
     * or with status {@code 500 (Internal Server Error)} if the oficina couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/oficinas/{id}")
    public ResponseEntity<Oficina> updateOficina(@PathVariable(value = "id", required = false) final Long id, @RequestBody Oficina oficina)
        throws URISyntaxException {
        log.debug("REST request to update Oficina : {}, {}", id, oficina);
        if (oficina.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oficina.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oficinaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Oficina result = oficinaService.save(oficina);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, oficina.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /oficinas/:id} : Partial updates given fields of an existing oficina, field will ignore if it is null
     *
     * @param id the id of the oficina to save.
     * @param oficina the oficina to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oficina,
     * or with status {@code 400 (Bad Request)} if the oficina is not valid,
     * or with status {@code 404 (Not Found)} if the oficina is not found,
     * or with status {@code 500 (Internal Server Error)} if the oficina couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/oficinas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Oficina> partialUpdateOficina(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Oficina oficina
    ) throws URISyntaxException {
        log.debug("REST request to partial update Oficina partially : {}, {}", id, oficina);
        if (oficina.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oficina.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oficinaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Oficina> result = oficinaService.partialUpdate(oficina);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, oficina.getId().toString())
        );
    }

    /**
     * {@code GET  /oficinas} : get all the oficinas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of oficinas in body.
     */
    @GetMapping("/oficinas")
    public ResponseEntity<List<Oficina>> getAllOficinas(
        OficinaCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Oficinas by criteria: {}", criteria);
        Page<Oficina> page = oficinaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /oficinas/count} : count all the oficinas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/oficinas/count")
    public ResponseEntity<Long> countOficinas(OficinaCriteria criteria) {
        log.debug("REST request to count Oficinas by criteria: {}", criteria);
        return ResponseEntity.ok().body(oficinaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /oficinas/:id} : get the "id" oficina.
     *
     * @param id the id of the oficina to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the oficina, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/oficinas/{id}")
    public ResponseEntity<Oficina> getOficina(@PathVariable Long id) {
        log.debug("REST request to get Oficina : {}", id);
        Optional<Oficina> oficina = oficinaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(oficina);
    }

    /**
     * {@code DELETE  /oficinas/:id} : delete the "id" oficina.
     *
     * @param id the id of the oficina to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/oficinas/{id}")
    public ResponseEntity<Void> deleteOficina(@PathVariable Long id) {
        log.debug("REST request to delete Oficina : {}", id);
        oficinaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
