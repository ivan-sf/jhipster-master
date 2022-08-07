package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Bodega;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface BodegaRepositoryWithBagRelationships {
    Optional<Bodega> fetchBagRelationships(Optional<Bodega> bodega);

    List<Bodega> fetchBagRelationships(List<Bodega> bodegas);

    Page<Bodega> fetchBagRelationships(Page<Bodega> bodegas);
}
