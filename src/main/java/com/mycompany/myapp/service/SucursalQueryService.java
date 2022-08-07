package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Sucursal;
import com.mycompany.myapp.repository.SucursalRepository;
import com.mycompany.myapp.service.criteria.SucursalCriteria;
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
 * Service for executing complex queries for {@link Sucursal} entities in the database.
 * The main input is a {@link SucursalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Sucursal} or a {@link Page} of {@link Sucursal} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SucursalQueryService extends QueryService<Sucursal> {

    private final Logger log = LoggerFactory.getLogger(SucursalQueryService.class);

    private final SucursalRepository sucursalRepository;

    public SucursalQueryService(SucursalRepository sucursalRepository) {
        this.sucursalRepository = sucursalRepository;
    }

    /**
     * Return a {@link List} of {@link Sucursal} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Sucursal> findByCriteria(SucursalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Sucursal> specification = createSpecification(criteria);
        return sucursalRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Sucursal} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Sucursal> findByCriteria(SucursalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Sucursal> specification = createSpecification(criteria);
        return sucursalRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SucursalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Sucursal> specification = createSpecification(criteria);
        return sucursalRepository.count(specification);
    }

    /**
     * Function to convert {@link SucursalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Sucursal> createSpecification(SucursalCriteria criteria) {
        Specification<Sucursal> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Sucursal_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Sucursal_.nombre));
            }
            if (criteria.getNit() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNit(), Sucursal_.nit));
            }
            if (criteria.getDireccion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDireccion(), Sucursal_.direccion));
            }
            if (criteria.getDireccionGPS() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDireccionGPS(), Sucursal_.direccionGPS));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEstado(), Sucursal_.estado));
            }
            if (criteria.getFechaRegistro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaRegistro(), Sucursal_.fechaRegistro));
            }
            if (criteria.getOficinaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOficinaId(), root -> root.join(Sucursal_.oficinas, JoinType.LEFT).get(Oficina_.id))
                    );
            }
            if (criteria.getInventarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInventarioId(),
                            root -> root.join(Sucursal_.inventarios, JoinType.LEFT).get(Inventario_.id)
                        )
                    );
            }
            if (criteria.getEmpresaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEmpresaId(), root -> root.join(Sucursal_.empresa, JoinType.LEFT).get(Empresa_.id))
                    );
            }
            if (criteria.getInfoLegalId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInfoLegalId(),
                            root -> root.join(Sucursal_.infoLegals, JoinType.LEFT).get(InfoLegal_.id)
                        )
                    );
            }
            if (criteria.getUsuarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUsuarioId(), root -> root.join(Sucursal_.usuarios, JoinType.LEFT).get(Usuario_.id))
                    );
            }
            if (criteria.getEmpresaIdId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmpresaIdId(),
                            root -> root.join(Sucursal_.empresaIds, JoinType.LEFT).get(Empresa_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
