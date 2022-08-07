package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Movimiento;
import com.mycompany.myapp.repository.MovimientoRepository;
import com.mycompany.myapp.service.criteria.MovimientoCriteria;
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
 * Service for executing complex queries for {@link Movimiento} entities in the database.
 * The main input is a {@link MovimientoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Movimiento} or a {@link Page} of {@link Movimiento} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MovimientoQueryService extends QueryService<Movimiento> {

    private final Logger log = LoggerFactory.getLogger(MovimientoQueryService.class);

    private final MovimientoRepository movimientoRepository;

    public MovimientoQueryService(MovimientoRepository movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }

    /**
     * Return a {@link List} of {@link Movimiento} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Movimiento> findByCriteria(MovimientoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Movimiento> specification = createSpecification(criteria);
        return movimientoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Movimiento} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Movimiento> findByCriteria(MovimientoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Movimiento> specification = createSpecification(criteria);
        return movimientoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MovimientoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Movimiento> specification = createSpecification(criteria);
        return movimientoRepository.count(specification);
    }

    /**
     * Function to convert {@link MovimientoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Movimiento> createSpecification(MovimientoCriteria criteria) {
        Specification<Movimiento> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Movimiento_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Movimiento_.name));
            }
        }
        return specification;
    }
}
