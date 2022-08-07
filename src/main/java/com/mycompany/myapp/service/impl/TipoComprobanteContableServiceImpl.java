package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.TipoComprobanteContable;
import com.mycompany.myapp.repository.TipoComprobanteContableRepository;
import com.mycompany.myapp.service.TipoComprobanteContableService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TipoComprobanteContable}.
 */
@Service
@Transactional
public class TipoComprobanteContableServiceImpl implements TipoComprobanteContableService {

    private final Logger log = LoggerFactory.getLogger(TipoComprobanteContableServiceImpl.class);

    private final TipoComprobanteContableRepository tipoComprobanteContableRepository;

    public TipoComprobanteContableServiceImpl(TipoComprobanteContableRepository tipoComprobanteContableRepository) {
        this.tipoComprobanteContableRepository = tipoComprobanteContableRepository;
    }

    @Override
    public TipoComprobanteContable save(TipoComprobanteContable tipoComprobanteContable) {
        log.debug("Request to save TipoComprobanteContable : {}", tipoComprobanteContable);
        return tipoComprobanteContableRepository.save(tipoComprobanteContable);
    }

    @Override
    public Optional<TipoComprobanteContable> partialUpdate(TipoComprobanteContable tipoComprobanteContable) {
        log.debug("Request to partially update TipoComprobanteContable : {}", tipoComprobanteContable);

        return tipoComprobanteContableRepository
            .findById(tipoComprobanteContable.getId())
            .map(existingTipoComprobanteContable -> {
                if (tipoComprobanteContable.getName() != null) {
                    existingTipoComprobanteContable.setName(tipoComprobanteContable.getName());
                }

                return existingTipoComprobanteContable;
            })
            .map(tipoComprobanteContableRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoComprobanteContable> findAll(Pageable pageable) {
        log.debug("Request to get all TipoComprobanteContables");
        return tipoComprobanteContableRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoComprobanteContable> findOne(Long id) {
        log.debug("Request to get TipoComprobanteContable : {}", id);
        return tipoComprobanteContableRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TipoComprobanteContable : {}", id);
        tipoComprobanteContableRepository.deleteById(id);
    }
}
