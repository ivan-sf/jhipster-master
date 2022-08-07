package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Bodega;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Bodega}.
 */
public interface BodegaService {
    /**
     * Save a bodega.
     *
     * @param bodega the entity to save.
     * @return the persisted entity.
     */
    Bodega save(Bodega bodega);

    /**
     * Partially updates a bodega.
     *
     * @param bodega the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Bodega> partialUpdate(Bodega bodega);

    /**
     * Get all the bodegas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Bodega> findAll(Pageable pageable);

    /**
     * Get all the bodegas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Bodega> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" bodega.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Bodega> findOne(Long id);

    /**
     * Delete the "id" bodega.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
