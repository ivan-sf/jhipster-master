package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Rol;
import com.mycompany.myapp.repository.RolRepository;
import com.mycompany.myapp.service.RolService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Rol}.
 */
@Service
@Transactional
public class RolServiceImpl implements RolService {

    private final Logger log = LoggerFactory.getLogger(RolServiceImpl.class);

    private final RolRepository rolRepository;

    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public Rol save(Rol rol) {
        log.debug("Request to save Rol : {}", rol);
        return rolRepository.save(rol);
    }

    @Override
    public Optional<Rol> partialUpdate(Rol rol) {
        log.debug("Request to partially update Rol : {}", rol);

        return rolRepository
            .findById(rol.getId())
            .map(existingRol -> {
                if (rol.getNombre() != null) {
                    existingRol.setNombre(rol.getNombre());
                }
                if (rol.getDescripcion() != null) {
                    existingRol.setDescripcion(rol.getDescripcion());
                }
                if (rol.getEstado() != null) {
                    existingRol.setEstado(rol.getEstado());
                }
                if (rol.getFechaRegistro() != null) {
                    existingRol.setFechaRegistro(rol.getFechaRegistro());
                }

                return existingRol;
            })
            .map(rolRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Rol> findAll(Pageable pageable) {
        log.debug("Request to get all Rols");
        return rolRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rol> findOne(Long id) {
        log.debug("Request to get Rol : {}", id);
        return rolRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Rol : {}", id);
        rolRepository.deleteById(id);
    }
}
