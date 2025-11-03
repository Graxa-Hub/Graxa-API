package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShowRepository extends JpaRepository<ShowEntity, Long> {
    boolean existsByNomeEvento(String nomeEvento);
    List<ShowEntity> findByAtivoTrue();
}
