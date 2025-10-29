package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.TurneEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurneRepository extends JpaRepository<TurneEntity, Long> {
    boolean existsByNomeTurne(String nome);
}
