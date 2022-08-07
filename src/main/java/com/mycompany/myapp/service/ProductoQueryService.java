package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Producto;
import com.mycompany.myapp.repository.ProductoRepository;
import com.mycompany.myapp.service.criteria.ProductoCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Producto} entities in the database.
 * The main input is a {@link ProductoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Producto} or a {@link Page} of {@link Producto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductoQueryService extends QueryService<Producto> {

    private final Logger log = LoggerFactory.getLogger(ProductoQueryService.class);

    private final ProductoRepository productoRepository;

    public ProductoQueryService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Return a {@link List} of {@link Producto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Producto> findByCriteria(ProductoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Producto> specification = createSpecification(criteria);
        return productoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Producto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Producto> findByCriteria(ProductoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Producto> specification = createSpecification(criteria);
        return productoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Producto> specification = createSpecification(criteria);
        return productoRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Producto> createSpecification(ProductoCriteria criteria) {
        Specification<Producto> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Producto_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Producto_.nombre));
            }
            if (criteria.getIva() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIva(), Producto_.iva));
            }
            if (criteria.getFechaRegistro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaRegistro(), Producto_.fechaRegistro));
            }
            if (criteria.getCodigoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCodigoId(), root -> root.join(Producto_.codigos, JoinType.LEFT).get(Codigo_.id))
                    );
            }
            if (criteria.getPrecioIngresoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPrecioIngresoId(),
                            root -> root.join(Producto_.precioIngresos, JoinType.LEFT).get(Precio_.id)
                        )
                    );
            }
            if (criteria.getPrecioSalidaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPrecioSalidaId(),
                            root -> root.join(Producto_.precioSalidas, JoinType.LEFT).get(Precio_.id)
                        )
                    );
            }
            if (criteria.getBodegaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBodegaId(), root -> root.join(Producto_.bodega, JoinType.LEFT).get(Bodega_.id))
                    );
            }
            if (criteria.getOficinaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOficinaId(), root -> root.join(Producto_.oficina, JoinType.LEFT).get(Oficina_.id))
                    );
            }
        }
        return specification;
    }
}
