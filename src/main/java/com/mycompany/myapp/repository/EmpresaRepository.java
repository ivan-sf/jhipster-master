package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Empresa;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Empresa entity.
 */
@Repository
public interface EmpresaRepository
    extends EmpresaRepositoryWithBagRelationships, JpaRepository<Empresa, Long>, JpaSpecificationExecutor<Empresa> {
    default Optional<Empresa> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Empresa> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Empresa> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct empresa from Empresa empresa left join fetch empresa.user",
        countQuery = "select count(distinct empresa) from Empresa empresa"
    )
    Page<Empresa> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct empresa from Empresa empresa left join fetch empresa.user")
    List<Empresa> findAllWithToOneRelationships();

    @Query("select empresa from Empresa empresa left join fetch empresa.user where empresa.id =:id")
    Optional<Empresa> findOneWithToOneRelationships(@Param("id") Long id);
}
