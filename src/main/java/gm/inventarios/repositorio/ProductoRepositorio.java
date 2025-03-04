package gm.inventarios.repositorio;

import gm.inventarios.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface ProductoRepositorio extends JpaRepository<Producto, UUID> {
    Optional<Producto> findById(UUID id);
}

