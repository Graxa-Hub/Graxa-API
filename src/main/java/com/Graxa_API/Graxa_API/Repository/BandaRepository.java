package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.BandaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BandaRepository extends JpaRepository<BandaEntity, Long> {
    boolean existsByNome(String nome);
    Optional<List<BandaEntity>> findByAtivoTrue();
}
