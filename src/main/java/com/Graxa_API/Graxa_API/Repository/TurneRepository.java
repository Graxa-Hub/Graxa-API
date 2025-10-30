package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.TurneEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TurneRepository extends JpaRepository<TurneEntity, Long> {
    boolean existsByNomeTurne(String nome);
    Optional<TurneEntity> findByNomeTurneAndAtivoTrue(String nomeTurne);

    Optional<TurneEntity> findByIdAndAtivoTrue(Long id);

    List<TurneEntity> findAllByAtivoTrue();
}
