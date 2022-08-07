package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TipoComprobanteContable;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TipoComprobanteContable}.
 */
public interface TipoComprobanteContableService {
    /**
     * Save a tipoComprobanteContable.
     *
     * @param tipoComprobanteContable the entity to save.
     * @return the persisted entity.
     */
    TipoComprobanteContable save(TipoComprobanteContable tipoComprobanteContable);

    /**
     * Partially updates a tipoComprobanteContable.
     *
     * @param tipoComprobanteContable the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TipoComprobanteContable> partialUpdate(TipoComprobanteContable tipoComprobanteContable);

    /**
     * Get all the tipoComprobanteContables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TipoComprobanteContable> findAll(Pageable pageable);

    /**
     * Get the "id" tipoComprobanteContable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TipoComprobanteContable> findOne(Long id);

    /**
     * Delete the "id" tipoComprobanteContable.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
