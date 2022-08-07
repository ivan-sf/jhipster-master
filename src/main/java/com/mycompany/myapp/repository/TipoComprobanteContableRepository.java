package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TipoComprobanteContable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TipoComprobanteContable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoComprobanteContableRepository
    extends JpaRepository<TipoComprobanteContable, Long>, JpaSpecificationExecutor<TipoComprobanteContable> {}
