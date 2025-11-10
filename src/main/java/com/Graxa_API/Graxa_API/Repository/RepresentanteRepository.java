package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.Usuario.RepresentanteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepresentanteRepository extends JpaRepository<RepresentanteEntity, Long> {
    Optional<RepresentanteEntity> findByEmail(String email);

    Optional<RepresentanteEntity> findByNomeContainingIgnoreCase(String nome);

    boolean existsByEmail(String email);
}
