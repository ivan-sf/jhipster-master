package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.InfoLegal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the InfoLegal entity.
 */
@Repository
public interface InfoLegalRepository
    extends InfoLegalRepositoryWithBagRelationships, JpaRepository<InfoLegal, Long>, JpaSpecificationExecutor<InfoLegal> {
    default Optional<InfoLegal> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<InfoLegal> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<InfoLegal> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
