package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Bodega;
import com.mycompany.myapp.repository.BodegaRepository;
import com.mycompany.myapp.service.criteria.BodegaCriteria;
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
 * Service for executing complex queries for {@link Bodega} entities in the database.
 * The main input is a {@link BodegaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Bodega} or a {@link Page} of {@link Bodega} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BodegaQueryService extends QueryService<Bodega> {

    private final Logger log = LoggerFactory.getLogger(BodegaQueryService.class);

    private final BodegaRepository bodegaRepository;

    public BodegaQueryService(BodegaRepository bodegaRepository) {
        this.bodegaRepository = bodegaRepository;
    }

    /**
     * Return a {@link List} of {@link Bodega} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Bodega> findByCriteria(BodegaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Bodega> specification = createSpecification(criteria);
        return bodegaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Bodega} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Bodega> findByCriteria(BodegaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Bodega> specification = createSpecification(criteria);
        return bodegaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BodegaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Bodega> specification = createSpecification(criteria);
        return bodegaRepository.count(specification);
    }

    /**
     * Function to convert {@link BodegaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Bodega> createSpecification(BodegaCriteria criteria) {
        Specification<Bodega> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Bodega_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Bodega_.nombre));
            }
            if (criteria.getUbicacion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUbicacion(), Bodega_.ubicacion));
            }
            if (criteria.getProductoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProductoId(), root -> root.join(Bodega_.productos, JoinType.LEFT).get(Producto_.id))
                    );
            }
            if (criteria.getUsuarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUsuarioId(), root -> root.join(Bodega_.usuarios, JoinType.LEFT).get(Usuario_.id))
                    );
            }
            if (criteria.getInventarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInventarioId(),
                            root -> root.join(Bodega_.inventario, JoinType.LEFT).get(Inventario_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
