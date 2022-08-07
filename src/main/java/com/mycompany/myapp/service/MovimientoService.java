package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Movimiento;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Movimiento}.
 */
public interface MovimientoService {
    /**
     * Save a movimiento.
     *
     * @param movimiento the entity to save.
     * @return the persisted entity.
     */
    Movimiento save(Movimiento movimiento);

    /**
     * Partially updates a movimiento.
     *
     * @param movimiento the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Movimiento> partialUpdate(Movimiento movimiento);

    /**
     * Get all the movimientos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Movimiento> findAll(Pageable pageable);

    /**
     * Get the "id" movimiento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Movimiento> findOne(Long id);

    /**
     * Delete the "id" movimiento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
