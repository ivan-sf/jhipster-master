package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Precio;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Precio}.
 */
public interface PrecioService {
    /**
     * Save a precio.
     *
     * @param precio the entity to save.
     * @return the persisted entity.
     */
    Precio save(Precio precio);

    /**
     * Partially updates a precio.
     *
     * @param precio the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Precio> partialUpdate(Precio precio);

    /**
     * Get all the precios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Precio> findAll(Pageable pageable);

    /**
     * Get the "id" precio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Precio> findOne(Long id);

    /**
     * Delete the "id" precio.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
