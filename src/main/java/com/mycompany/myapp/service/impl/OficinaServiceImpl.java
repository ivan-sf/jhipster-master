package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Oficina;
import com.mycompany.myapp.repository.OficinaRepository;
import com.mycompany.myapp.service.OficinaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Oficina}.
 */
@Service
@Transactional
public class OficinaServiceImpl implements OficinaService {

    private final Logger log = LoggerFactory.getLogger(OficinaServiceImpl.class);

    private final OficinaRepository oficinaRepository;

    public OficinaServiceImpl(OficinaRepository oficinaRepository) {
        this.oficinaRepository = oficinaRepository;
    }

    @Override
    public Oficina save(Oficina oficina) {
        log.debug("Request to save Oficina : {}", oficina);
        return oficinaRepository.save(oficina);
    }

    @Override
    public Optional<Oficina> partialUpdate(Oficina oficina) {
        log.debug("Request to partially update Oficina : {}", oficina);

        return oficinaRepository
            .findById(oficina.getId())
            .map(existingOficina -> {
                if (oficina.getNombre() != null) {
                    existingOficina.setNombre(oficina.getNombre());
                }
                if (oficina.getDetalle() != null) {
                    existingOficina.setDetalle(oficina.getDetalle());
                }
                if (oficina.getUbicacion() != null) {
                    existingOficina.setUbicacion(oficina.getUbicacion());
                }

                return existingOficina;
            })
            .map(oficinaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Oficina> findAll(Pageable pageable) {
        log.debug("Request to get all Oficinas");
        return oficinaRepository.findAll(pageable);
    }

    public Page<Oficina> findAllWithEagerRelationships(Pageable pageable) {
        return oficinaRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Oficina> findOne(Long id) {
        log.debug("Request to get Oficina : {}", id);
        return oficinaRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Oficina : {}", id);
        oficinaRepository.deleteById(id);
    }
}
