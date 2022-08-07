package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.TipoUsuario;
import com.mycompany.myapp.repository.TipoUsuarioRepository;
import com.mycompany.myapp.service.criteria.TipoUsuarioCriteria;
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
 * Service for executing complex queries for {@link TipoUsuario} entities in the database.
 * The main input is a {@link TipoUsuarioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TipoUsuario} or a {@link Page} of {@link TipoUsuario} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TipoUsuarioQueryService extends QueryService<TipoUsuario> {

    private final Logger log = LoggerFactory.getLogger(TipoUsuarioQueryService.class);

    private final TipoUsuarioRepository tipoUsuarioRepository;

    public TipoUsuarioQueryService(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    /**
     * Return a {@link List} of {@link TipoUsuario} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TipoUsuario> findByCriteria(TipoUsuarioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TipoUsuario> specification = createSpecification(criteria);
        return tipoUsuarioRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TipoUsuario} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoUsuario> findByCriteria(TipoUsuarioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TipoUsuario> specification = createSpecification(criteria);
        return tipoUsuarioRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TipoUsuarioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TipoUsuario> specification = createSpecification(criteria);
        return tipoUsuarioRepository.count(specification);
    }

    /**
     * Function to convert {@link TipoUsuarioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TipoUsuario> createSpecification(TipoUsuarioCriteria criteria) {
        Specification<TipoUsuario> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TipoUsuario_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TipoUsuario_.name));
            }
        }
        return specification;
    }
}
