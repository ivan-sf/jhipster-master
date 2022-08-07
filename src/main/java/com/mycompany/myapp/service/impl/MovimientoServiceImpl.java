package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Movimiento;
import com.mycompany.myapp.repository.MovimientoRepository;
import com.mycompany.myapp.service.MovimientoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Movimiento}.
 */
@Service
@Transactional
public class MovimientoServiceImpl implements MovimientoService {

    private final Logger log = LoggerFactory.getLogger(MovimientoServiceImpl.class);

    private final MovimientoRepository movimientoRepository;

    public MovimientoServiceImpl(MovimientoRepository movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }

    @Override
    public Movimiento save(Movimiento movimiento) {
        log.debug("Request to save Movimiento : {}", movimiento);
        return movimientoRepository.save(movimiento);
    }

    @Override
    public Optional<Movimiento> partialUpdate(Movimiento movimiento) {
        log.debug("Request to partially update Movimiento : {}", movimiento);

        return movimientoRepository
            .findById(movimiento.getId())
            .map(existingMovimiento -> {
                if (movimiento.getName() != null) {
                    existingMovimiento.setName(movimiento.getName());
                }

                return existingMovimiento;
            })
            .map(movimientoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Movimiento> findAll(Pageable pageable) {
        log.debug("Request to get all Movimientos");
        return movimientoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Movimiento> findOne(Long id) {
        log.debug("Request to get Movimiento : {}", id);
        return movimientoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Movimiento : {}", id);
        movimientoRepository.deleteById(id);
    }
}
