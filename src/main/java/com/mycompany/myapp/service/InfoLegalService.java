package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.InfoLegal;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link InfoLegal}.
 */
public interface InfoLegalService {
    /**
     * Save a infoLegal.
     *
     * @param infoLegal the entity to save.
     * @return the persisted entity.
     */
    InfoLegal save(InfoLegal infoLegal);

    /**
     * Partially updates a infoLegal.
     *
     * @param infoLegal the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InfoLegal> partialUpdate(InfoLegal infoLegal);

    /**
     * Get all the infoLegals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InfoLegal> findAll(Pageable pageable);

    /**
     * Get all the infoLegals with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InfoLegal> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" infoLegal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InfoLegal> findOne(Long id);

    /**
     * Delete the "id" infoLegal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
