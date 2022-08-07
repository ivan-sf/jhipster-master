package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Codigo;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Codigo}.
 */
public interface CodigoService {
    /**
     * Save a codigo.
     *
     * @param codigo the entity to save.
     * @return the persisted entity.
     */
    Codigo save(Codigo codigo);

    /**
     * Partially updates a codigo.
     *
     * @param codigo the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Codigo> partialUpdate(Codigo codigo);

    /**
     * Get all the codigos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Codigo> findAll(Pageable pageable);

    /**
     * Get the "id" codigo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Codigo> findOne(Long id);

    /**
     * Delete the "id" codigo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
