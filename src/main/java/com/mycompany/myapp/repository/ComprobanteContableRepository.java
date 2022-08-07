package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ComprobanteContable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ComprobanteContable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComprobanteContableRepository
    extends JpaRepository<ComprobanteContable, Long>, JpaSpecificationExecutor<ComprobanteContable> {}
