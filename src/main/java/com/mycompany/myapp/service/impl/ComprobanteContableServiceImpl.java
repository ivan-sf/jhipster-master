package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.ComprobanteContable;
import com.mycompany.myapp.repository.ComprobanteContableRepository;
import com.mycompany.myapp.service.ComprobanteContableService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ComprobanteContable}.
 */
@Service
@Transactional
public class ComprobanteContableServiceImpl implements ComprobanteContableService {

    private final Logger log = LoggerFactory.getLogger(ComprobanteContableServiceImpl.class);

    private final ComprobanteContableRepository comprobanteContableRepository;

    public ComprobanteContableServiceImpl(ComprobanteContableRepository comprobanteContableRepository) {
        this.comprobanteContableRepository = comprobanteContableRepository;
    }

    @Override
    public ComprobanteContable save(ComprobanteContable comprobanteContable) {
        log.debug("Request to save ComprobanteContable : {}", comprobanteContable);
        return comprobanteContableRepository.save(comprobanteContable);
    }

    @Override
    public Optional<ComprobanteContable> partialUpdate(ComprobanteContable comprobanteContable) {
        log.debug("Request to partially update ComprobanteContable : {}", comprobanteContable);

        return comprobanteContableRepository
            .findById(comprobanteContable.getId())
            .map(existingComprobanteContable -> {
                if (comprobanteContable.getName() != null) {
                    existingComprobanteContable.setName(comprobanteContable.getName());
                }

                return existingComprobanteContable;
            })
            .map(comprobanteContableRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComprobanteContable> findAll(Pageable pageable) {
        log.debug("Request to get all ComprobanteContables");
        return comprobanteContableRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComprobanteContable> findOne(Long id) {
        log.debug("Request to get ComprobanteContable : {}", id);
        return comprobanteContableRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ComprobanteContable : {}", id);
        comprobanteContableRepository.deleteById(id);
    }
}
