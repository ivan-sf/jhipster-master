package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Empresa;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Empresa}.
 */
public interface EmpresaService {
    /**
     * Save a empresa.
     *
     * @param empresa the entity to save.
     * @return the persisted entity.
     */
    Empresa save(Empresa empresa);

    /**
     * Partially updates a empresa.
     *
     * @param empresa the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Empresa> partialUpdate(Empresa empresa);

    /**
     * Get all the empresas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Empresa> findAll(Pageable pageable);

    /**
     * Get all the empresas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Empresa> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" empresa.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Empresa> findOne(Long id);

    /**
     * Delete the "id" empresa.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
