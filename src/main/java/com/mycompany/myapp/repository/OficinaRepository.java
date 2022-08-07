package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Oficina;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Oficina entity.
 */
@Repository
public interface OficinaRepository
    extends OficinaRepositoryWithBagRelationships, JpaRepository<Oficina, Long>, JpaSpecificationExecutor<Oficina> {
    default Optional<Oficina> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Oficina> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Oficina> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
