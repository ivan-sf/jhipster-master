package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.InfoLegal;
import com.mycompany.myapp.repository.InfoLegalRepository;
import com.mycompany.myapp.service.InfoLegalQueryService;
import com.mycompany.myapp.service.InfoLegalService;
import com.mycompany.myapp.service.criteria.InfoLegalCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.InfoLegal}.
 */
@RestController
@RequestMapping("/api")
public class InfoLegalResource {

    private final Logger log = LoggerFactory.getLogger(InfoLegalResource.class);

    private static final String ENTITY_NAME = "infoLegal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InfoLegalService infoLegalService;

    private final InfoLegalRepository infoLegalRepository;

    private final InfoLegalQueryService infoLegalQueryService;

    public InfoLegalResource(
        InfoLegalService infoLegalService,
        InfoLegalRepository infoLegalRepository,
        InfoLegalQueryService infoLegalQueryService
    ) {
        this.infoLegalService = infoLegalService;
        this.infoLegalRepository = infoLegalRepository;
        this.infoLegalQueryService = infoLegalQueryService;
    }

    /**
     * {@code POST  /info-legals} : Create a new infoLegal.
     *
     * @param infoLegal the infoLegal to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new infoLegal, or with status {@code 400 (Bad Request)} if the infoLegal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/info-legals")
    public ResponseEntity<InfoLegal> createInfoLegal(@RequestBody InfoLegal infoLegal) throws URISyntaxException {
        log.debug("REST request to save InfoLegal : {}", infoLegal);
        if (infoLegal.getId() != null) {
            throw new BadRequestAlertException("A new infoLegal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InfoLegal result = infoLegalService.save(infoLegal);
        return ResponseEntity
            .created(new URI("/api/info-legals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /info-legals/:id} : Updates an existing infoLegal.
     *
     * @param id the id of the infoLegal to save.
     * @param infoLegal the infoLegal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infoLegal,
     * or with status {@code 400 (Bad Request)} if the infoLegal is not valid,
     * or with status {@code 500 (Internal Server Error)} if the infoLegal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/info-legals/{id}")
    public ResponseEntity<InfoLegal> updateInfoLegal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InfoLegal infoLegal
    ) throws URISyntaxException {
        log.debug("REST request to update InfoLegal : {}, {}", id, infoLegal);
        if (infoLegal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infoLegal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!infoLegalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InfoLegal result = infoLegalService.save(infoLegal);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, infoLegal.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /info-legals/:id} : Partial updates given fields of an existing infoLegal, field will ignore if it is null
     *
     * @param id the id of the infoLegal to save.
     * @param infoLegal the infoLegal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infoLegal,
     * or with status {@code 400 (Bad Request)} if the infoLegal is not valid,
     * or with status {@code 404 (Not Found)} if the infoLegal is not found,
     * or with status {@code 500 (Internal Server Error)} if the infoLegal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/info-legals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InfoLegal> partialUpdateInfoLegal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InfoLegal infoLegal
    ) throws URISyntaxException {
        log.debug("REST request to partial update InfoLegal partially : {}, {}", id, infoLegal);
        if (infoLegal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infoLegal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!infoLegalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InfoLegal> result = infoLegalService.partialUpdate(infoLegal);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, infoLegal.getId().toString())
        );
    }

    /**
     * {@code GET  /info-legals} : get all the infoLegals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of infoLegals in body.
     */
    @GetMapping("/info-legals")
    public ResponseEntity<List<InfoLegal>> getAllInfoLegals(
        InfoLegalCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get InfoLegals by criteria: {}", criteria);
        Page<InfoLegal> page = infoLegalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /info-legals/count} : count all the infoLegals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/info-legals/count")
    public ResponseEntity<Long> countInfoLegals(InfoLegalCriteria criteria) {
        log.debug("REST request to count InfoLegals by criteria: {}", criteria);
        return ResponseEntity.ok().body(infoLegalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /info-legals/:id} : get the "id" infoLegal.
     *
     * @param id the id of the infoLegal to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the infoLegal, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/info-legals/{id}")
    public ResponseEntity<InfoLegal> getInfoLegal(@PathVariable Long id) {
        log.debug("REST request to get InfoLegal : {}", id);
        Optional<InfoLegal> infoLegal = infoLegalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(infoLegal);
    }

    /**
     * {@code DELETE  /info-legals/:id} : delete the "id" infoLegal.
     *
     * @param id the id of the infoLegal to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/info-legals/{id}")
    public ResponseEntity<Void> deleteInfoLegal(@PathVariable Long id) {
        log.debug("REST request to delete InfoLegal : {}", id);
        infoLegalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
