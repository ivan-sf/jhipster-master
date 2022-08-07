package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Usuario;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class UsuarioRepositoryWithBagRelationshipsImpl implements UsuarioRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Usuario> fetchBagRelationships(Optional<Usuario> usuario) {
        return usuario.map(this::fetchSucursals).map(this::fetchEmpresas);
    }

    @Override
    public Page<Usuario> fetchBagRelationships(Page<Usuario> usuarios) {
        return new PageImpl<>(fetchBagRelationships(usuarios.getContent()), usuarios.getPageable(), usuarios.getTotalElements());
    }

    @Override
    public List<Usuario> fetchBagRelationships(List<Usuario> usuarios) {
        return Optional.of(usuarios).map(this::fetchSucursals).map(this::fetchEmpresas).get();
    }

    Usuario fetchSucursals(Usuario result) {
        return entityManager
            .createQuery("select usuario from Usuario usuario left join fetch usuario.sucursals where usuario is :usuario", Usuario.class)
            .setParameter("usuario", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Usuario> fetchSucursals(List<Usuario> usuarios) {
        return entityManager
            .createQuery(
                "select distinct usuario from Usuario usuario left join fetch usuario.sucursals where usuario in :usuarios",
                Usuario.class
            )
            .setParameter("usuarios", usuarios)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    Usuario fetchEmpresas(Usuario result) {
        return entityManager
            .createQuery("select usuario from Usuario usuario left join fetch usuario.empresas where usuario is :usuario", Usuario.class)
            .setParameter("usuario", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Usuario> fetchEmpresas(List<Usuario> usuarios) {
        return entityManager
            .createQuery(
                "select distinct usuario from Usuario usuario left join fetch usuario.empresas where usuario in :usuarios",
                Usuario.class
            )
            .setParameter("usuarios", usuarios)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
