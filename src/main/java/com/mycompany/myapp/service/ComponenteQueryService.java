package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Componente;
import com.mycompany.myapp.repository.ComponenteRepository;
import com.mycompany.myapp.service.criteria.ComponenteCriteria;
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
 * Service for executing complex queries for {@link Componente} entities in the database.
 * The main input is a {@link ComponenteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Componente} or a {@link Page} of {@link Componente} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ComponenteQueryService extends QueryService<Componente> {

    private final Logger log = LoggerFactory.getLogger(ComponenteQueryService.class);

    private final ComponenteRepository componenteRepository;

    public ComponenteQueryService(ComponenteRepository componenteRepository) {
        this.componenteRepository = componenteRepository;
    }

    /**
     * Return a {@link List} of {@link Componente} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Componente> findByCriteria(ComponenteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Componente> specification = createSpecification(criteria);
        return componenteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Componente} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Componente> findByCriteria(ComponenteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Componente> specification = createSpecification(criteria);
        return componenteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ComponenteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Componente> specification = createSpecification(criteria);
        return componenteRepository.count(specification);
    }

    /**
     * Function to convert {@link ComponenteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Componente> createSpecification(ComponenteCriteria criteria) {
        Specification<Componente> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Componente_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Componente_.nombre));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEstado(), Componente_.estado));
            }
            if (criteria.getFechaRegistro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaRegistro(), Componente_.fechaRegistro));
            }
            if (criteria.getEmpresaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEmpresaId(), root -> root.join(Componente_.empresa, JoinType.LEFT).get(Empresa_.id))
                    );
            }
        }
        return specification;
    }
}
