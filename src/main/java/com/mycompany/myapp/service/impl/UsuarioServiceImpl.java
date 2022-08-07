package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Usuario;
import com.mycompany.myapp.repository.UsuarioRepository;
import com.mycompany.myapp.service.UsuarioService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Usuario}.
 */
@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        log.debug("Request to save Usuario : {}", usuario);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> partialUpdate(Usuario usuario) {
        log.debug("Request to partially update Usuario : {}", usuario);

        return usuarioRepository
            .findById(usuario.getId())
            .map(existingUsuario -> {
                if (usuario.getPrimerNombre() != null) {
                    existingUsuario.setPrimerNombre(usuario.getPrimerNombre());
                }
                if (usuario.getSegundoNombre() != null) {
                    existingUsuario.setSegundoNombre(usuario.getSegundoNombre());
                }
                if (usuario.getPrimerApellido() != null) {
                    existingUsuario.setPrimerApellido(usuario.getPrimerApellido());
                }
                if (usuario.getSegundoApellido() != null) {
                    existingUsuario.setSegundoApellido(usuario.getSegundoApellido());
                }
                if (usuario.getTipoDocumento() != null) {
                    existingUsuario.setTipoDocumento(usuario.getTipoDocumento());
                }
                if (usuario.getDocumento() != null) {
                    existingUsuario.setDocumento(usuario.getDocumento());
                }
                if (usuario.getDocumentoDV() != null) {
                    existingUsuario.setDocumentoDV(usuario.getDocumentoDV());
                }
                if (usuario.getEdad() != null) {
                    existingUsuario.setEdad(usuario.getEdad());
                }
                if (usuario.getIndicativo() != null) {
                    existingUsuario.setIndicativo(usuario.getIndicativo());
                }
                if (usuario.getCelular() != null) {
                    existingUsuario.setCelular(usuario.getCelular());
                }
                if (usuario.getDireccion() != null) {
                    existingUsuario.setDireccion(usuario.getDireccion());
                }
                if (usuario.getDireccionGps() != null) {
                    existingUsuario.setDireccionGps(usuario.getDireccionGps());
                }
                if (usuario.getFoto() != null) {
                    existingUsuario.setFoto(usuario.getFoto());
                }
                if (usuario.getFotoContentType() != null) {
                    existingUsuario.setFotoContentType(usuario.getFotoContentType());
                }
                if (usuario.getFechaRegistro() != null) {
                    existingUsuario.setFechaRegistro(usuario.getFechaRegistro());
                }

                return existingUsuario;
            })
            .map(usuarioRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> findAll(Pageable pageable) {
        log.debug("Request to get all Usuarios");
        return usuarioRepository.findAll(pageable);
    }

    public Page<Usuario> findAllWithEagerRelationships(Pageable pageable) {
        return usuarioRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findOne(Long id) {
        log.debug("Request to get Usuario : {}", id);
        return usuarioRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Usuario : {}", id);
        usuarioRepository.deleteById(id);
    }
}
