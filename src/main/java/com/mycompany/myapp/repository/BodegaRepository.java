package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Bodega;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Bodega entity.
 */
@Repository
public interface BodegaRepository
    extends BodegaRepositoryWithBagRelationships, JpaRepository<Bodega, Long>, JpaSpecificationExecutor<Bodega> {
    default Optional<Bodega> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Bodega> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Bodega> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
