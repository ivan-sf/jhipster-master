package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Bodega;
import com.mycompany.myapp.repository.BodegaRepository;
import com.mycompany.myapp.service.BodegaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Bodega}.
 */
@Service
@Transactional
public class BodegaServiceImpl implements BodegaService {

    private final Logger log = LoggerFactory.getLogger(BodegaServiceImpl.class);

    private final BodegaRepository bodegaRepository;

    public BodegaServiceImpl(BodegaRepository bodegaRepository) {
        this.bodegaRepository = bodegaRepository;
    }

    @Override
    public Bodega save(Bodega bodega) {
        log.debug("Request to save Bodega : {}", bodega);
        return bodegaRepository.save(bodega);
    }

    @Override
    public Optional<Bodega> partialUpdate(Bodega bodega) {
        log.debug("Request to partially update Bodega : {}", bodega);

        return bodegaRepository
            .findById(bodega.getId())
            .map(existingBodega -> {
                if (bodega.getNombre() != null) {
                    existingBodega.setNombre(bodega.getNombre());
                }
                if (bodega.getDetalle() != null) {
                    existingBodega.setDetalle(bodega.getDetalle());
                }
                if (bodega.getUbicacion() != null) {
                    existingBodega.setUbicacion(bodega.getUbicacion());
                }

                return existingBodega;
            })
            .map(bodegaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Bodega> findAll(Pageable pageable) {
        log.debug("Request to get all Bodegas");
        return bodegaRepository.findAll(pageable);
    }

    public Page<Bodega> findAllWithEagerRelationships(Pageable pageable) {
        return bodegaRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Bodega> findOne(Long id) {
        log.debug("Request to get Bodega : {}", id);
        return bodegaRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Bodega : {}", id);
        bodegaRepository.deleteById(id);
    }
}
