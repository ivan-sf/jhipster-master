package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Oficina;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Oficina}.
 */
public interface OficinaService {
    /**
     * Save a oficina.
     *
     * @param oficina the entity to save.
     * @return the persisted entity.
     */
    Oficina save(Oficina oficina);

    /**
     * Partially updates a oficina.
     *
     * @param oficina the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Oficina> partialUpdate(Oficina oficina);

    /**
     * Get all the oficinas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Oficina> findAll(Pageable pageable);

    /**
     * Get all the oficinas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Oficina> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" oficina.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Oficina> findOne(Long id);

    /**
     * Delete the "id" oficina.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
