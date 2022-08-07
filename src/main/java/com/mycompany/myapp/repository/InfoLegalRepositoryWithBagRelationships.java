package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.InfoLegal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface InfoLegalRepositoryWithBagRelationships {
    Optional<InfoLegal> fetchBagRelationships(Optional<InfoLegal> infoLegal);

    List<InfoLegal> fetchBagRelationships(List<InfoLegal> infoLegals);

    Page<InfoLegal> fetchBagRelationships(Page<InfoLegal> infoLegals);
}
