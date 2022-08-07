package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Precio;
import com.mycompany.myapp.repository.PrecioRepository;
import com.mycompany.myapp.service.criteria.PrecioCriteria;
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
 * Service for executing complex queries for {@link Precio} entities in the database.
 * The main input is a {@link PrecioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Precio} or a {@link Page} of {@link Precio} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PrecioQueryService extends QueryService<Precio> {

    private final Logger log = LoggerFactory.getLogger(PrecioQueryService.class);

    private final PrecioRepository precioRepository;

    public PrecioQueryService(PrecioRepository precioRepository) {
        this.precioRepository = precioRepository;
    }

    /**
     * Return a {@link List} of {@link Precio} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Precio> findByCriteria(PrecioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Precio> specification = createSpecification(criteria);
        return precioRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Precio} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Precio> findByCriteria(PrecioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Precio> specification = createSpecification(criteria);
        return precioRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PrecioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Precio> specification = createSpecification(criteria);
        return precioRepository.count(specification);
    }

    /**
     * Function to convert {@link PrecioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Precio> createSpecification(PrecioCriteria criteria) {
        Specification<Precio> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Precio_.id));
            }
            if (criteria.getValor() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValor(), Precio_.valor));
            }
            if (criteria.getFechaRegistro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaRegistro(), Precio_.fechaRegistro));
            }
            if (criteria.getProductoIngresoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductoIngresoId(),
                            root -> root.join(Precio_.productoIngresos, JoinType.LEFT).get(Producto_.id)
                        )
                    );
            }
            if (criteria.getProductoSalidaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductoSalidaId(),
                            root -> root.join(Precio_.productoSalidas, JoinType.LEFT).get(Producto_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
