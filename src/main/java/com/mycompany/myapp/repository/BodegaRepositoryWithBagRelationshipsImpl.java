package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Bodega;
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
public class BodegaRepositoryWithBagRelationshipsImpl implements BodegaRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Bodega> fetchBagRelationships(Optional<Bodega> bodega) {
        return bodega.map(this::fetchUsuarios);
    }

    @Override
    public Page<Bodega> fetchBagRelationships(Page<Bodega> bodegas) {
        return new PageImpl<>(fetchBagRelationships(bodegas.getContent()), bodegas.getPageable(), bodegas.getTotalElements());
    }

    @Override
    public List<Bodega> fetchBagRelationships(List<Bodega> bodegas) {
        return Optional.of(bodegas).map(this::fetchUsuarios).get();
    }

    Bodega fetchUsuarios(Bodega result) {
        return entityManager
            .createQuery("select bodega from Bodega bodega left join fetch bodega.usuarios where bodega is :bodega", Bodega.class)
            .setParameter("bodega", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Bodega> fetchUsuarios(List<Bodega> bodegas) {
        return entityManager
            .createQuery("select distinct bodega from Bodega bodega left join fetch bodega.usuarios where bodega in :bodegas", Bodega.class)
            .setParameter("bodegas", bodegas)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
