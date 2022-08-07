package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.ComprobanteContable;
import com.mycompany.myapp.repository.ComprobanteContableRepository;
import com.mycompany.myapp.service.criteria.ComprobanteContableCriteria;
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
 * Service for executing complex queries for {@link ComprobanteContable} entities in the database.
 * The main input is a {@link ComprobanteContableCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ComprobanteContable} or a {@link Page} of {@link ComprobanteContable} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ComprobanteContableQueryService extends QueryService<ComprobanteContable> {

    private final Logger log = LoggerFactory.getLogger(ComprobanteContableQueryService.class);

    private final ComprobanteContableRepository comprobanteContableRepository;

    public ComprobanteContableQueryService(ComprobanteContableRepository comprobanteContableRepository) {
        this.comprobanteContableRepository = comprobanteContableRepository;
    }

    /**
     * Return a {@link List} of {@link ComprobanteContable} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ComprobanteContable> findByCriteria(ComprobanteContableCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ComprobanteContable> specification = createSpecification(criteria);
        return comprobanteContableRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ComprobanteContable} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ComprobanteContable> findByCriteria(ComprobanteContableCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ComprobanteContable> specification = createSpecification(criteria);
        return comprobanteContableRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ComprobanteContableCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ComprobanteContable> specification = createSpecification(criteria);
        return comprobanteContableRepository.count(specification);
    }

    /**
     * Function to convert {@link ComprobanteContableCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ComprobanteContable> createSpecification(ComprobanteContableCriteria criteria) {
        Specification<ComprobanteContable> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ComprobanteContable_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ComprobanteContable_.name));
            }
        }
        return specification;
    }
}
