package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Oficina;
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
public class OficinaRepositoryWithBagRelationshipsImpl implements OficinaRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Oficina> fetchBagRelationships(Optional<Oficina> oficina) {
        return oficina.map(this::fetchUsuarios);
    }

    @Override
    public Page<Oficina> fetchBagRelationships(Page<Oficina> oficinas) {
        return new PageImpl<>(fetchBagRelationships(oficinas.getContent()), oficinas.getPageable(), oficinas.getTotalElements());
    }

    @Override
    public List<Oficina> fetchBagRelationships(List<Oficina> oficinas) {
        return Optional.of(oficinas).map(this::fetchUsuarios).get();
    }

    Oficina fetchUsuarios(Oficina result) {
        return entityManager
            .createQuery("select oficina from Oficina oficina left join fetch oficina.usuarios where oficina is :oficina", Oficina.class)
            .setParameter("oficina", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Oficina> fetchUsuarios(List<Oficina> oficinas) {
        return entityManager
            .createQuery(
                "select distinct oficina from Oficina oficina left join fetch oficina.usuarios where oficina in :oficinas",
                Oficina.class
            )
            .setParameter("oficinas", oficinas)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
