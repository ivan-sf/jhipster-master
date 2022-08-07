package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ComprobanteContable;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ComprobanteContable}.
 */
public interface ComprobanteContableService {
    /**
     * Save a comprobanteContable.
     *
     * @param comprobanteContable the entity to save.
     * @return the persisted entity.
     */
    ComprobanteContable save(ComprobanteContable comprobanteContable);

    /**
     * Partially updates a comprobanteContable.
     *
     * @param comprobanteContable the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ComprobanteContable> partialUpdate(ComprobanteContable comprobanteContable);

    /**
     * Get all the comprobanteContables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ComprobanteContable> findAll(Pageable pageable);

    /**
     * Get the "id" comprobanteContable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ComprobanteContable> findOne(Long id);

    /**
     * Delete the "id" comprobanteContable.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
