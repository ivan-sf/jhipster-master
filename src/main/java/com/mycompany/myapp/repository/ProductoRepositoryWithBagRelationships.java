package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Producto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ProductoRepositoryWithBagRelationships {
    Optional<Producto> fetchBagRelationships(Optional<Producto> producto);

    List<Producto> fetchBagRelationships(List<Producto> productos);

    Page<Producto> fetchBagRelationships(Page<Producto> productos);
}
