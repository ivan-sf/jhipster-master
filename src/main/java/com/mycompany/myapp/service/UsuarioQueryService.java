package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Usuario;
import com.mycompany.myapp.repository.UsuarioRepository;
import com.mycompany.myapp.service.criteria.UsuarioCriteria;
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
 * Service for executing complex queries for {@link Usuario} entities in the database.
 * The main input is a {@link UsuarioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Usuario} or a {@link Page} of {@link Usuario} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UsuarioQueryService extends QueryService<Usuario> {

    private final Logger log = LoggerFactory.getLogger(UsuarioQueryService.class);

    private final UsuarioRepository usuarioRepository;

    public UsuarioQueryService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Return a {@link List} of {@link Usuario} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Usuario> findByCriteria(UsuarioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Usuario> specification = createSpecification(criteria);
        return usuarioRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Usuario} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Usuario> findByCriteria(UsuarioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Usuario> specification = createSpecification(criteria);
        return usuarioRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UsuarioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Usuario> specification = createSpecification(criteria);
        return usuarioRepository.count(specification);
    }

    /**
     * Function to convert {@link UsuarioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Usuario> createSpecification(UsuarioCriteria criteria) {
        Specification<Usuario> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Usuario_.id));
            }
            if (criteria.getPrimerNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrimerNombre(), Usuario_.primerNombre));
            }
            if (criteria.getSegundoNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSegundoNombre(), Usuario_.segundoNombre));
            }
            if (criteria.getPrimerApellido() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrimerApellido(), Usuario_.primerApellido));
            }
            if (criteria.getSegundoApellido() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSegundoApellido(), Usuario_.segundoApellido));
            }
            if (criteria.getTipoDocumento() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTipoDocumento(), Usuario_.tipoDocumento));
            }
            if (criteria.getDocumento() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocumento(), Usuario_.documento));
            }
            if (criteria.getDocumentoDV() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocumentoDV(), Usuario_.documentoDV));
            }
            if (criteria.getEdad() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEdad(), Usuario_.edad));
            }
            if (criteria.getIndicativo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIndicativo(), Usuario_.indicativo));
            }
            if (criteria.getCelular() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCelular(), Usuario_.celular));
            }
            if (criteria.getDireccion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDireccion(), Usuario_.direccion));
            }
            if (criteria.getDireccionGps() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDireccionGps(), Usuario_.direccionGps));
            }
            if (criteria.getFechaRegistro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaRegistro(), Usuario_.fechaRegistro));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Usuario_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getRolId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getRolId(), root -> root.join(Usuario_.rol, JoinType.LEFT).get(Rol_.id)));
            }
            if (criteria.getInfoLegalId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInfoLegalId(),
                            root -> root.join(Usuario_.infoLegals, JoinType.LEFT).get(InfoLegal_.id)
                        )
                    );
            }
            if (criteria.getSucursalId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSucursalId(), root -> root.join(Usuario_.sucursals, JoinType.LEFT).get(Sucursal_.id))
                    );
            }
            if (criteria.getEmpresaIdId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmpresaIdId(),
                            root -> root.join(Usuario_.empresaIds, JoinType.LEFT).get(Empresa_.id)
                        )
                    );
            }
            if (criteria.getBodegaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBodegaId(), root -> root.join(Usuario_.bodegas, JoinType.LEFT).get(Bodega_.id))
                    );
            }
            if (criteria.getOficinaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOficinaId(), root -> root.join(Usuario_.oficinas, JoinType.LEFT).get(Oficina_.id))
                    );
            }
        }
        return specification;
    }
}
