package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TipoUsuario;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TipoUsuario}.
 */
public interface TipoUsuarioService {
    /**
     * Save a tipoUsuario.
     *
     * @param tipoUsuario the entity to save.
     * @return the persisted entity.
     */
    TipoUsuario save(TipoUsuario tipoUsuario);

    /**
     * Partially updates a tipoUsuario.
     *
     * @param tipoUsuario the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TipoUsuario> partialUpdate(TipoUsuario tipoUsuario);

    /**
     * Get all the tipoUsuarios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TipoUsuario> findAll(Pageable pageable);

    /**
     * Get the "id" tipoUsuario.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TipoUsuario> findOne(Long id);

    /**
     * Delete the "id" tipoUsuario.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
