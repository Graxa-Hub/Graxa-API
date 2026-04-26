package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.BandaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface BandaRepository extends JpaRepository<BandaEntity, Long> {
    boolean existsByNome(String nome);
    Page<BandaEntity> findByAtivoTrue(Pageable pageable);
    List<BandaEntity> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);
    Page<BandaEntity> findByAtivoTrueAndCriadoPorId(Long criadoPorId, Pageable pageable);
    boolean existsByNomeAndCriadoPorId(String nome, Long criadoPorId);
}
