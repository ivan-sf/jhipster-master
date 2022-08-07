package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Inventario;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Inventario}.
 */
public interface InventarioService {
    /**
     * Save a inventario.
     *
     * @param inventario the entity to save.
     * @return the persisted entity.
     */
    Inventario save(Inventario inventario);

    /**
     * Partially updates a inventario.
     *
     * @param inventario the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Inventario> partialUpdate(Inventario inventario);

    /**
     * Get all the inventarios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Inventario> findAll(Pageable pageable);

    /**
     * Get the "id" inventario.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Inventario> findOne(Long id);

    /**
     * Delete the "id" inventario.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
