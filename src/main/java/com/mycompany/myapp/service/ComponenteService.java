package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Componente;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Componente}.
 */
public interface ComponenteService {
    /**
     * Save a componente.
     *
     * @param componente the entity to save.
     * @return the persisted entity.
     */
    Componente save(Componente componente);

    /**
     * Partially updates a componente.
     *
     * @param componente the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Componente> partialUpdate(Componente componente);

    /**
     * Get all the componentes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Componente> findAll(Pageable pageable);

    /**
     * Get the "id" componente.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Componente> findOne(Long id);

    /**
     * Delete the "id" componente.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
