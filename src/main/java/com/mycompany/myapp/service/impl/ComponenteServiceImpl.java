package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Componente;
import com.mycompany.myapp.repository.ComponenteRepository;
import com.mycompany.myapp.service.ComponenteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Componente}.
 */
@Service
@Transactional
public class ComponenteServiceImpl implements ComponenteService {

    private final Logger log = LoggerFactory.getLogger(ComponenteServiceImpl.class);

    private final ComponenteRepository componenteRepository;

    public ComponenteServiceImpl(ComponenteRepository componenteRepository) {
        this.componenteRepository = componenteRepository;
    }

    @Override
    public Componente save(Componente componente) {
        log.debug("Request to save Componente : {}", componente);
        return componenteRepository.save(componente);
    }

    @Override
    public Optional<Componente> partialUpdate(Componente componente) {
        log.debug("Request to partially update Componente : {}", componente);

        return componenteRepository
            .findById(componente.getId())
            .map(existingComponente -> {
                if (componente.getNombre() != null) {
                    existingComponente.setNombre(componente.getNombre());
                }
                if (componente.getDescripcion() != null) {
                    existingComponente.setDescripcion(componente.getDescripcion());
                }
                if (componente.getEstado() != null) {
                    existingComponente.setEstado(componente.getEstado());
                }
                if (componente.getFechaRegistro() != null) {
                    existingComponente.setFechaRegistro(componente.getFechaRegistro());
                }

                return existingComponente;
            })
            .map(componenteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Componente> findAll(Pageable pageable) {
        log.debug("Request to get all Componentes");
        return componenteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Componente> findOne(Long id) {
        log.debug("Request to get Componente : {}", id);
        return componenteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Componente : {}", id);
        componenteRepository.deleteById(id);
    }
}
