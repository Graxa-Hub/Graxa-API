package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.TurneEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TurneRepository extends JpaRepository<TurneEntity, Long> {
    boolean existsByNomeTurne(String nome);
    Optional<TurneEntity> findByNomeTurneAndAtivoTrue(String nomeTurne);
    Optional<TurneEntity> findByIdAndAtivoTrue(Long id);
    boolean existsByNomeTurneAndCriadoPorId(String nomeTurne, Long criadoPorId);
    Page<TurneEntity> findAllByAtivoTrue(Pageable pageable);
    Page<TurneEntity> findByBandaIdAndAtivoTrue(Long bandaId, Pageable pageable);
    Page<TurneEntity> findByAtivoTrueAndCriadoPorId(Long criadoPorId, Pageable pageable);
    // TurneRepository — adiciona esse método
    Optional<TurneEntity> findByNomeTurneAndAtivoTrueAndCriadoPorId(String nomeTurne, Long criadoPorId);
}
