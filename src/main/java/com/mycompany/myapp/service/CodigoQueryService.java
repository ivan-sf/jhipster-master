package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Codigo;
import com.mycompany.myapp.repository.CodigoRepository;
import com.mycompany.myapp.service.criteria.CodigoCriteria;
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
 * Service for executing complex queries for {@link Codigo} entities in the database.
 * The main input is a {@link CodigoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Codigo} or a {@link Page} of {@link Codigo} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CodigoQueryService extends QueryService<Codigo> {

    private final Logger log = LoggerFactory.getLogger(CodigoQueryService.class);

    private final CodigoRepository codigoRepository;

    public CodigoQueryService(CodigoRepository codigoRepository) {
        this.codigoRepository = codigoRepository;
    }

    /**
     * Return a {@link List} of {@link Codigo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Codigo> findByCriteria(CodigoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Codigo> specification = createSpecification(criteria);
        return codigoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Codigo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Codigo> findByCriteria(CodigoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Codigo> specification = createSpecification(criteria);
        return codigoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CodigoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Codigo> specification = createSpecification(criteria);
        return codigoRepository.count(specification);
    }

    /**
     * Function to convert {@link CodigoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Codigo> createSpecification(CodigoCriteria criteria) {
        Specification<Codigo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Codigo_.id));
            }
            if (criteria.getCodigo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodigo(), Codigo_.codigo));
            }
            if (criteria.getFechaRegistro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaRegistro(), Codigo_.fechaRegistro));
            }
            if (criteria.getProductoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProductoId(), root -> root.join(Codigo_.productos, JoinType.LEFT).get(Producto_.id))
                    );
            }
        }
        return specification;
    }
}
