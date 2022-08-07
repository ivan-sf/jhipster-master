package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Producto;
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
public class ProductoRepositoryWithBagRelationshipsImpl implements ProductoRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Producto> fetchBagRelationships(Optional<Producto> producto) {
        return producto.map(this::fetchCodigos).map(this::fetchPrecioIngresos).map(this::fetchPrecioSalidas);
    }

    @Override
    public Page<Producto> fetchBagRelationships(Page<Producto> productos) {
        return new PageImpl<>(fetchBagRelationships(productos.getContent()), productos.getPageable(), productos.getTotalElements());
    }

    @Override
    public List<Producto> fetchBagRelationships(List<Producto> productos) {
        return Optional.of(productos).map(this::fetchCodigos).map(this::fetchPrecioIngresos).map(this::fetchPrecioSalidas).get();
    }

    Producto fetchCodigos(Producto result) {
        return entityManager
            .createQuery(
                "select producto from Producto producto left join fetch producto.codigos where producto is :producto",
                Producto.class
            )
            .setParameter("producto", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Producto> fetchCodigos(List<Producto> productos) {
        return entityManager
            .createQuery(
                "select distinct producto from Producto producto left join fetch producto.codigos where producto in :productos",
                Producto.class
            )
            .setParameter("productos", productos)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    Producto fetchPrecioIngresos(Producto result) {
        return entityManager
            .createQuery(
                "select producto from Producto producto left join fetch producto.precioIngresos where producto is :producto",
                Producto.class
            )
            .setParameter("producto", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Producto> fetchPrecioIngresos(List<Producto> productos) {
        return entityManager
            .createQuery(
                "select distinct producto from Producto producto left join fetch producto.precioIngresos where producto in :productos",
                Producto.class
            )
            .setParameter("productos", productos)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    Producto fetchPrecioSalidas(Producto result) {
        return entityManager
            .createQuery(
                "select producto from Producto producto left join fetch producto.precioSalidas where producto is :producto",
                Producto.class
            )
            .setParameter("producto", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Producto> fetchPrecioSalidas(List<Producto> productos) {
        return entityManager
            .createQuery(
                "select distinct producto from Producto producto left join fetch producto.precioSalidas where producto in :productos",
                Producto.class
            )
            .setParameter("productos", productos)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
