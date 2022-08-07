package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Sucursal;
import com.mycompany.myapp.repository.SucursalRepository;
import com.mycompany.myapp.service.SucursalService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sucursal}.
 */
@Service
@Transactional
public class SucursalServiceImpl implements SucursalService {

    private final Logger log = LoggerFactory.getLogger(SucursalServiceImpl.class);

    private final SucursalRepository sucursalRepository;

    public SucursalServiceImpl(SucursalRepository sucursalRepository) {
        this.sucursalRepository = sucursalRepository;
    }

    @Override
    public Sucursal save(Sucursal sucursal) {
        log.debug("Request to save Sucursal : {}", sucursal);
        return sucursalRepository.save(sucursal);
    }

    @Override
    public Optional<Sucursal> partialUpdate(Sucursal sucursal) {
        log.debug("Request to partially update Sucursal : {}", sucursal);

        return sucursalRepository
            .findById(sucursal.getId())
            .map(existingSucursal -> {
                if (sucursal.getNombre() != null) {
                    existingSucursal.setNombre(sucursal.getNombre());
                }
                if (sucursal.getNit() != null) {
                    existingSucursal.setNit(sucursal.getNit());
                }
                if (sucursal.getDetalle() != null) {
                    existingSucursal.setDetalle(sucursal.getDetalle());
                }
                if (sucursal.getDireccion() != null) {
                    existingSucursal.setDireccion(sucursal.getDireccion());
                }
                if (sucursal.getDireccionGPS() != null) {
                    existingSucursal.setDireccionGPS(sucursal.getDireccionGPS());
                }
                if (sucursal.getLogo() != null) {
                    existingSucursal.setLogo(sucursal.getLogo());
                }
                if (sucursal.getLogoContentType() != null) {
                    existingSucursal.setLogoContentType(sucursal.getLogoContentType());
                }
                if (sucursal.getEstado() != null) {
                    existingSucursal.setEstado(sucursal.getEstado());
                }
                if (sucursal.getFechaRegistro() != null) {
                    existingSucursal.setFechaRegistro(sucursal.getFechaRegistro());
                }

                return existingSucursal;
            })
            .map(sucursalRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sucursal> findAll(Pageable pageable) {
        log.debug("Request to get all Sucursals");
        return sucursalRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sucursal> findOne(Long id) {
        log.debug("Request to get Sucursal : {}", id);
        return sucursalRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sucursal : {}", id);
        sucursalRepository.deleteById(id);
    }
}
