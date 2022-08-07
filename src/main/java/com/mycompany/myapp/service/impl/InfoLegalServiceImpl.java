package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.InfoLegal;
import com.mycompany.myapp.repository.InfoLegalRepository;
import com.mycompany.myapp.service.InfoLegalService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InfoLegal}.
 */
@Service
@Transactional
public class InfoLegalServiceImpl implements InfoLegalService {

    private final Logger log = LoggerFactory.getLogger(InfoLegalServiceImpl.class);

    private final InfoLegalRepository infoLegalRepository;

    public InfoLegalServiceImpl(InfoLegalRepository infoLegalRepository) {
        this.infoLegalRepository = infoLegalRepository;
    }

    @Override
    public InfoLegal save(InfoLegal infoLegal) {
        log.debug("Request to save InfoLegal : {}", infoLegal);
        return infoLegalRepository.save(infoLegal);
    }

    @Override
    public Optional<InfoLegal> partialUpdate(InfoLegal infoLegal) {
        log.debug("Request to partially update InfoLegal : {}", infoLegal);

        return infoLegalRepository
            .findById(infoLegal.getId())
            .map(existingInfoLegal -> {
                if (infoLegal.getNit() != null) {
                    existingInfoLegal.setNit(infoLegal.getNit());
                }
                if (infoLegal.getRegimen() != null) {
                    existingInfoLegal.setRegimen(infoLegal.getRegimen());
                }
                if (infoLegal.getResolucionPos() != null) {
                    existingInfoLegal.setResolucionPos(infoLegal.getResolucionPos());
                }
                if (infoLegal.getPrefijoPosInicial() != null) {
                    existingInfoLegal.setPrefijoPosInicial(infoLegal.getPrefijoPosInicial());
                }
                if (infoLegal.getPrefijoPosFinal() != null) {
                    existingInfoLegal.setPrefijoPosFinal(infoLegal.getPrefijoPosFinal());
                }
                if (infoLegal.getResolucionFacElec() != null) {
                    existingInfoLegal.setResolucionFacElec(infoLegal.getResolucionFacElec());
                }
                if (infoLegal.getPrefijoFacElecFinal() != null) {
                    existingInfoLegal.setPrefijoFacElecFinal(infoLegal.getPrefijoFacElecFinal());
                }
                if (infoLegal.getResolucionNomElec() != null) {
                    existingInfoLegal.setResolucionNomElec(infoLegal.getResolucionNomElec());
                }
                if (infoLegal.getEstado() != null) {
                    existingInfoLegal.setEstado(infoLegal.getEstado());
                }
                if (infoLegal.getFechaRegistro() != null) {
                    existingInfoLegal.setFechaRegistro(infoLegal.getFechaRegistro());
                }

                return existingInfoLegal;
            })
            .map(infoLegalRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InfoLegal> findAll(Pageable pageable) {
        log.debug("Request to get all InfoLegals");
        return infoLegalRepository.findAll(pageable);
    }

    public Page<InfoLegal> findAllWithEagerRelationships(Pageable pageable) {
        return infoLegalRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InfoLegal> findOne(Long id) {
        log.debug("Request to get InfoLegal : {}", id);
        return infoLegalRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InfoLegal : {}", id);
        infoLegalRepository.deleteById(id);
    }
}
