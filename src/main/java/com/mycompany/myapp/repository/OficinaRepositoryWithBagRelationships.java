package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Oficina;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface OficinaRepositoryWithBagRelationships {
    Optional<Oficina> fetchBagRelationships(Optional<Oficina> oficina);

    List<Oficina> fetchBagRelationships(List<Oficina> oficinas);

    Page<Oficina> fetchBagRelationships(Page<Oficina> oficinas);
}
