package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Empresa;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface EmpresaRepositoryWithBagRelationships {
    Optional<Empresa> fetchBagRelationships(Optional<Empresa> empresa);

    List<Empresa> fetchBagRelationships(List<Empresa> empresas);

    Page<Empresa> fetchBagRelationships(Page<Empresa> empresas);
}
