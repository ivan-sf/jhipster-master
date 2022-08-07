package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Rol;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Rol}.
 */
public interface RolService {
    /**
     * Save a rol.
     *
     * @param rol the entity to save.
     * @return the persisted entity.
     */
    Rol save(Rol rol);

    /**
     * Partially updates a rol.
     *
     * @param rol the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Rol> partialUpdate(Rol rol);

    /**
     * Get all the rols.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Rol> findAll(Pageable pageable);

    /**
     * Get the "id" rol.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Rol> findOne(Long id);

    /**
     * Delete the "id" rol.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
