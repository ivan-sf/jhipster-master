package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.TipoUsuario;
import com.mycompany.myapp.repository.TipoUsuarioRepository;
import com.mycompany.myapp.service.TipoUsuarioService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TipoUsuario}.
 */
@Service
@Transactional
public class TipoUsuarioServiceImpl implements TipoUsuarioService {

    private final Logger log = LoggerFactory.getLogger(TipoUsuarioServiceImpl.class);

    private final TipoUsuarioRepository tipoUsuarioRepository;

    public TipoUsuarioServiceImpl(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    @Override
    public TipoUsuario save(TipoUsuario tipoUsuario) {
        log.debug("Request to save TipoUsuario : {}", tipoUsuario);
        return tipoUsuarioRepository.save(tipoUsuario);
    }

    @Override
    public Optional<TipoUsuario> partialUpdate(TipoUsuario tipoUsuario) {
        log.debug("Request to partially update TipoUsuario : {}", tipoUsuario);

        return tipoUsuarioRepository
            .findById(tipoUsuario.getId())
            .map(existingTipoUsuario -> {
                if (tipoUsuario.getName() != null) {
                    existingTipoUsuario.setName(tipoUsuario.getName());
                }

                return existingTipoUsuario;
            })
            .map(tipoUsuarioRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoUsuario> findAll(Pageable pageable) {
        log.debug("Request to get all TipoUsuarios");
        return tipoUsuarioRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoUsuario> findOne(Long id) {
        log.debug("Request to get TipoUsuario : {}", id);
        return tipoUsuarioRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TipoUsuario : {}", id);
        tipoUsuarioRepository.deleteById(id);
    }
}
