package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.InfoLegal;
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
public class InfoLegalRepositoryWithBagRelationshipsImpl implements InfoLegalRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<InfoLegal> fetchBagRelationships(Optional<InfoLegal> infoLegal) {
        return infoLegal.map(this::fetchEmpresaIds).map(this::fetchSucursals);
    }

    @Override
    public Page<InfoLegal> fetchBagRelationships(Page<InfoLegal> infoLegals) {
        return new PageImpl<>(fetchBagRelationships(infoLegals.getContent()), infoLegals.getPageable(), infoLegals.getTotalElements());
    }

    @Override
    public List<InfoLegal> fetchBagRelationships(List<InfoLegal> infoLegals) {
        return Optional.of(infoLegals).map(this::fetchEmpresaIds).map(this::fetchSucursals).get();
    }

    InfoLegal fetchEmpresaIds(InfoLegal result) {
        return entityManager
            .createQuery(
                "select infoLegal from InfoLegal infoLegal left join fetch infoLegal.empresaIds where infoLegal is :infoLegal",
                InfoLegal.class
            )
            .setParameter("infoLegal", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<InfoLegal> fetchEmpresaIds(List<InfoLegal> infoLegals) {
        return entityManager
            .createQuery(
                "select distinct infoLegal from InfoLegal infoLegal left join fetch infoLegal.empresaIds where infoLegal in :infoLegals",
                InfoLegal.class
            )
            .setParameter("infoLegals", infoLegals)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    InfoLegal fetchSucursals(InfoLegal result) {
        return entityManager
            .createQuery(
                "select infoLegal from InfoLegal infoLegal left join fetch infoLegal.sucursals where infoLegal is :infoLegal",
                InfoLegal.class
            )
            .setParameter("infoLegal", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<InfoLegal> fetchSucursals(List<InfoLegal> infoLegals) {
        return entityManager
            .createQuery(
                "select distinct infoLegal from InfoLegal infoLegal left join fetch infoLegal.sucursals where infoLegal in :infoLegals",
                InfoLegal.class
            )
            .setParameter("infoLegals", infoLegals)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
