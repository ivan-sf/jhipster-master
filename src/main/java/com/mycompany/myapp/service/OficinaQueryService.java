package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Oficina;
import com.mycompany.myapp.repository.OficinaRepository;
import com.mycompany.myapp.service.criteria.OficinaCriteria;
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
 * Service for executing complex queries for {@link Oficina} entities in the database.
 * The main input is a {@link OficinaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Oficina} or a {@link Page} of {@link Oficina} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OficinaQueryService extends QueryService<Oficina> {

    private final Logger log = LoggerFactory.getLogger(OficinaQueryService.class);

    private final OficinaRepository oficinaRepository;

    public OficinaQueryService(OficinaRepository oficinaRepository) {
        this.oficinaRepository = oficinaRepository;
    }

    /**
     * Return a {@link List} of {@link Oficina} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Oficina> findByCriteria(OficinaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Oficina> specification = createSpecification(criteria);
        return oficinaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Oficina} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Oficina> findByCriteria(OficinaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Oficina> specification = createSpecification(criteria);
        return oficinaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OficinaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Oficina> specification = createSpecification(criteria);
        return oficinaRepository.count(specification);
    }

    /**
     * Function to convert {@link OficinaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Oficina> createSpecification(OficinaCriteria criteria) {
        Specification<Oficina> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Oficina_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Oficina_.nombre));
            }
            if (criteria.getUbicacion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUbicacion(), Oficina_.ubicacion));
            }
            if (criteria.getProductoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProductoId(), root -> root.join(Oficina_.productos, JoinType.LEFT).get(Producto_.id))
                    );
            }
            if (criteria.getUsuarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUsuarioId(), root -> root.join(Oficina_.usuarios, JoinType.LEFT).get(Usuario_.id))
                    );
            }
            if (criteria.getSucursalId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSucursalId(), root -> root.join(Oficina_.sucursal, JoinType.LEFT).get(Sucursal_.id))
                    );
            }
        }
        return specification;
    }
}
