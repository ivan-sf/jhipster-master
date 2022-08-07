package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Precio;
import com.mycompany.myapp.repository.PrecioRepository;
import com.mycompany.myapp.service.PrecioService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Precio}.
 */
@Service
@Transactional
public class PrecioServiceImpl implements PrecioService {

    private final Logger log = LoggerFactory.getLogger(PrecioServiceImpl.class);

    private final PrecioRepository precioRepository;

    public PrecioServiceImpl(PrecioRepository precioRepository) {
        this.precioRepository = precioRepository;
    }

    @Override
    public Precio save(Precio precio) {
        log.debug("Request to save Precio : {}", precio);
        return precioRepository.save(precio);
    }

    @Override
    public Optional<Precio> partialUpdate(Precio precio) {
        log.debug("Request to partially update Precio : {}", precio);

        return precioRepository
            .findById(precio.getId())
            .map(existingPrecio -> {
                if (precio.getValor() != null) {
                    existingPrecio.setValor(precio.getValor());
                }
                if (precio.getDetalle() != null) {
                    existingPrecio.setDetalle(precio.getDetalle());
                }
                if (precio.getFechaRegistro() != null) {
                    existingPrecio.setFechaRegistro(precio.getFechaRegistro());
                }

                return existingPrecio;
            })
            .map(precioRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Precio> findAll(Pageable pageable) {
        log.debug("Request to get all Precios");
        return precioRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Precio> findOne(Long id) {
        log.debug("Request to get Precio : {}", id);
        return precioRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Precio : {}", id);
        precioRepository.deleteById(id);
    }
}
