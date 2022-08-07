package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.InfoLegal;
import com.mycompany.myapp.repository.InfoLegalRepository;
import com.mycompany.myapp.service.criteria.InfoLegalCriteria;
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
 * Service for executing complex queries for {@link InfoLegal} entities in the database.
 * The main input is a {@link InfoLegalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InfoLegal} or a {@link Page} of {@link InfoLegal} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InfoLegalQueryService extends QueryService<InfoLegal> {

    private final Logger log = LoggerFactory.getLogger(InfoLegalQueryService.class);

    private final InfoLegalRepository infoLegalRepository;

    public InfoLegalQueryService(InfoLegalRepository infoLegalRepository) {
        this.infoLegalRepository = infoLegalRepository;
    }

    /**
     * Return a {@link List} of {@link InfoLegal} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InfoLegal> findByCriteria(InfoLegalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InfoLegal> specification = createSpecification(criteria);
        return infoLegalRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link InfoLegal} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InfoLegal> findByCriteria(InfoLegalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InfoLegal> specification = createSpecification(criteria);
        return infoLegalRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InfoLegalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InfoLegal> specification = createSpecification(criteria);
        return infoLegalRepository.count(specification);
    }

    /**
     * Function to convert {@link InfoLegalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InfoLegal> createSpecification(InfoLegalCriteria criteria) {
        Specification<InfoLegal> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InfoLegal_.id));
            }
            if (criteria.getNit() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNit(), InfoLegal_.nit));
            }
            if (criteria.getRegimen() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRegimen(), InfoLegal_.regimen));
            }
            if (criteria.getResolucionPos() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResolucionPos(), InfoLegal_.resolucionPos));
            }
            if (criteria.getPrefijoPosInicial() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrefijoPosInicial(), InfoLegal_.prefijoPosInicial));
            }
            if (criteria.getPrefijoPosFinal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrefijoPosFinal(), InfoLegal_.prefijoPosFinal));
            }
            if (criteria.getResolucionFacElec() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResolucionFacElec(), InfoLegal_.resolucionFacElec));
            }
            if (criteria.getPrefijoFacElecFinal() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPrefijoFacElecFinal(), InfoLegal_.prefijoFacElecFinal));
            }
            if (criteria.getResolucionNomElec() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResolucionNomElec(), InfoLegal_.resolucionNomElec));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEstado(), InfoLegal_.estado));
            }
            if (criteria.getFechaRegistro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaRegistro(), InfoLegal_.fechaRegistro));
            }
            if (criteria.getEmpresaIdId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmpresaIdId(),
                            root -> root.join(InfoLegal_.empresaIds, JoinType.LEFT).get(Empresa_.id)
                        )
                    );
            }
            if (criteria.getSucursalId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSucursalId(),
                            root -> root.join(InfoLegal_.sucursals, JoinType.LEFT).get(Sucursal_.id)
                        )
                    );
            }
            if (criteria.getUsuarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUsuarioId(), root -> root.join(InfoLegal_.usuario, JoinType.LEFT).get(Usuario_.id))
                    );
            }
        }
        return specification;
    }
}
