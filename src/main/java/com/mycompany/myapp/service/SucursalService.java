package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Sucursal;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Sucursal}.
 */
public interface SucursalService {
    /**
     * Save a sucursal.
     *
     * @param sucursal the entity to save.
     * @return the persisted entity.
     */
    Sucursal save(Sucursal sucursal);

    /**
     * Partially updates a sucursal.
     *
     * @param sucursal the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Sucursal> partialUpdate(Sucursal sucursal);

    /**
     * Get all the sucursals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Sucursal> findAll(Pageable pageable);

    /**
     * Get the "id" sucursal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Sucursal> findOne(Long id);

    /**
     * Delete the "id" sucursal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
