package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Empresa;
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
public class EmpresaRepositoryWithBagRelationshipsImpl implements EmpresaRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Empresa> fetchBagRelationships(Optional<Empresa> empresa) {
        return empresa.map(this::fetchSucursalIds);
    }

    @Override
    public Page<Empresa> fetchBagRelationships(Page<Empresa> empresas) {
        return new PageImpl<>(fetchBagRelationships(empresas.getContent()), empresas.getPageable(), empresas.getTotalElements());
    }

    @Override
    public List<Empresa> fetchBagRelationships(List<Empresa> empresas) {
        return Optional.of(empresas).map(this::fetchSucursalIds).get();
    }

    Empresa fetchSucursalIds(Empresa result) {
        return entityManager
            .createQuery("select empresa from Empresa empresa left join fetch empresa.sucursalIds where empresa is :empresa", Empresa.class)
            .setParameter("empresa", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Empresa> fetchSucursalIds(List<Empresa> empresas) {
        return entityManager
            .createQuery(
                "select distinct empresa from Empresa empresa left join fetch empresa.sucursalIds where empresa in :empresas",
                Empresa.class
            )
            .setParameter("empresas", empresas)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
