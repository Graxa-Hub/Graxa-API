package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.BandaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BandaRepository extends JpaRepository<BandaEntity, Long> {
    boolean existsByNome(String nome);
}
