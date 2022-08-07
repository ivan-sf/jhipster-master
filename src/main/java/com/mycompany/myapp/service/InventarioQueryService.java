package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Inventario;
import com.mycompany.myapp.repository.InventarioRepository;
import com.mycompany.myapp.service.criteria.InventarioCriteria;
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
 * Service for executing complex queries for {@link Inventario} entities in the database.
 * The main input is a {@link InventarioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Inventario} or a {@link Page} of {@link Inventario} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InventarioQueryService extends QueryService<Inventario> {

    private final Logger log = LoggerFactory.getLogger(InventarioQueryService.class);

    private final InventarioRepository inventarioRepository;

    public InventarioQueryService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    /**
     * Return a {@link List} of {@link Inventario} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Inventario> findByCriteria(InventarioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Inventario> specification = createSpecification(criteria);
        return inventarioRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Inventario} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Inventario> findByCriteria(InventarioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Inventario> specification = createSpecification(criteria);
        return inventarioRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InventarioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Inventario> specification = createSpecification(criteria);
        return inventarioRepository.count(specification);
    }

    /**
     * Function to convert {@link InventarioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Inventario> createSpecification(InventarioCriteria criteria) {
        Specification<Inventario> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Inventario_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Inventario_.nombre));
            }
            if (criteria.getBodegaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBodegaId(), root -> root.join(Inventario_.bodegas, JoinType.LEFT).get(Bodega_.id))
                    );
            }
            if (criteria.getSucursalId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSucursalId(),
                            root -> root.join(Inventario_.sucursal, JoinType.LEFT).get(Sucursal_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
