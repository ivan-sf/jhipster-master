package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Empresa;
import com.mycompany.myapp.repository.EmpresaRepository;
import com.mycompany.myapp.service.criteria.EmpresaCriteria;
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
 * Service for executing complex queries for {@link Empresa} entities in the database.
 * The main input is a {@link EmpresaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Empresa} or a {@link Page} of {@link Empresa} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmpresaQueryService extends QueryService<Empresa> {

    private final Logger log = LoggerFactory.getLogger(EmpresaQueryService.class);

    private final EmpresaRepository empresaRepository;

    public EmpresaQueryService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    /**
     * Return a {@link List} of {@link Empresa} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Empresa> findByCriteria(EmpresaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Empresa> specification = createSpecification(criteria);
        return empresaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Empresa} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Empresa> findByCriteria(EmpresaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Empresa> specification = createSpecification(criteria);
        return empresaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmpresaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Empresa> specification = createSpecification(criteria);
        return empresaRepository.count(specification);
    }

    /**
     * Function to convert {@link EmpresaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Empresa> createSpecification(EmpresaCriteria criteria) {
        Specification<Empresa> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Empresa_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Empresa_.nombre));
            }
            if (criteria.getDireccion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDireccion(), Empresa_.direccion));
            }
            if (criteria.getDireccionGPS() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDireccionGPS(), Empresa_.direccionGPS));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Empresa_.email));
            }
            if (criteria.getCelular() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCelular(), Empresa_.celular));
            }
            if (criteria.getIndicativo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIndicativo(), Empresa_.indicativo));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEstado(), Empresa_.estado));
            }
            if (criteria.getFechaRegistro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaRegistro(), Empresa_.fechaRegistro));
            }
            if (criteria.getComponenteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getComponenteId(),
                            root -> root.join(Empresa_.componentes, JoinType.LEFT).get(Componente_.id)
                        )
                    );
            }
            if (criteria.getRolId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRolId(), root -> root.join(Empresa_.rols, JoinType.LEFT).get(Rol_.id))
                    );
            }
            if (criteria.getUsuarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUsuarioId(), root -> root.join(Empresa_.usuarios, JoinType.LEFT).get(Usuario_.id))
                    );
            }
            if (criteria.getSucursalId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSucursalId(), root -> root.join(Empresa_.sucursals, JoinType.LEFT).get(Sucursal_.id))
                    );
            }
            if (criteria.getSucursalIdId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSucursalIdId(),
                            root -> root.join(Empresa_.sucursalIds, JoinType.LEFT).get(Sucursal_.id)
                        )
                    );
            }
            if (criteria.getInfoLegalIdId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInfoLegalIdId(),
                            root -> root.join(Empresa_.infoLegalIds, JoinType.LEFT).get(InfoLegal_.id)
                        )
                    );
            }
            if (criteria.getUsuarioIdId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUsuarioIdId(),
                            root -> root.join(Empresa_.usuarioIds, JoinType.LEFT).get(Usuario_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
