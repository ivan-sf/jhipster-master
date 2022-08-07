package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Componente;
import com.mycompany.myapp.repository.ComponenteRepository;
import com.mycompany.myapp.service.ComponenteQueryService;
import com.mycompany.myapp.service.ComponenteService;
import com.mycompany.myapp.service.criteria.ComponenteCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Componente}.
 */
@RestController
@RequestMapping("/api")
public class ComponenteResource {

    private final Logger log = LoggerFactory.getLogger(ComponenteResource.class);

    private static final String ENTITY_NAME = "componente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComponenteService componenteService;

    private final ComponenteRepository componenteRepository;

    private final ComponenteQueryService componenteQueryService;

    public ComponenteResource(
        ComponenteService componenteService,
        ComponenteRepository componenteRepository,
        ComponenteQueryService componenteQueryService
    ) {
        this.componenteService = componenteService;
        this.componenteRepository = componenteRepository;
        this.componenteQueryService = componenteQueryService;
    }

    /**
     * {@code POST  /componentes} : Create a new componente.
     *
     * @param componente the componente to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new componente, or with status {@code 400 (Bad Request)} if the componente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/componentes")
    public ResponseEntity<Componente> createComponente(@RequestBody Componente componente) throws URISyntaxException {
        log.debug("REST request to save Componente : {}", componente);
        if (componente.getId() != null) {
            throw new BadRequestAlertException("A new componente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Componente result = componenteService.save(componente);
        return ResponseEntity
            .created(new URI("/api/componentes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /componentes/:id} : Updates an existing componente.
     *
     * @param id the id of the componente to save.
     * @param componente the componente to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated componente,
     * or with status {@code 400 (Bad Request)} if the componente is not valid,
     * or with status {@code 500 (Internal Server Error)} if the componente couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/componentes/{id}")
    public ResponseEntity<Componente> updateComponente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Componente componente
    ) throws URISyntaxException {
        log.debug("REST request to update Componente : {}, {}", id, componente);
        if (componente.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, componente.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!componenteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Componente result = componenteService.save(componente);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, componente.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /componentes/:id} : Partial updates given fields of an existing componente, field will ignore if it is null
     *
     * @param id the id of the componente to save.
     * @param componente the componente to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated componente,
     * or with status {@code 400 (Bad Request)} if the componente is not valid,
     * or with status {@code 404 (Not Found)} if the componente is not found,
     * or with status {@code 500 (Internal Server Error)} if the componente couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/componentes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Componente> partialUpdateComponente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Componente componente
    ) throws URISyntaxException {
        log.debug("REST request to partial update Componente partially : {}, {}", id, componente);
        if (componente.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, componente.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!componenteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Componente> result = componenteService.partialUpdate(componente);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, componente.getId().toString())
        );
    }

    /**
     * {@code GET  /componentes} : get all the componentes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of componentes in body.
     */
    @GetMapping("/componentes")
    public ResponseEntity<List<Componente>> getAllComponentes(
        ComponenteCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Componentes by criteria: {}", criteria);
        Page<Componente> page = componenteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /componentes/count} : count all the componentes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/componentes/count")
    public ResponseEntity<Long> countComponentes(ComponenteCriteria criteria) {
        log.debug("REST request to count Componentes by criteria: {}", criteria);
        return ResponseEntity.ok().body(componenteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /componentes/:id} : get the "id" componente.
     *
     * @param id the id of the componente to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the componente, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/componentes/{id}")
    public ResponseEntity<Componente> getComponente(@PathVariable Long id) {
        log.debug("REST request to get Componente : {}", id);
        Optional<Componente> componente = componenteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(componente);
    }

    /**
     * {@code DELETE  /componentes/:id} : delete the "id" componente.
     *
     * @param id the id of the componente to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/componentes/{id}")
    public ResponseEntity<Void> deleteComponente(@PathVariable Long id) {
        log.debug("REST request to delete Componente : {}", id);
        componenteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
