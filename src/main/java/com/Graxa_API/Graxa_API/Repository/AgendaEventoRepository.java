package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.Evento.AgendaEventoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendaEventoRepository extends JpaRepository<AgendaEventoEntity, Long> {

    // Lista eventos de um show ordenados por ordem e data/hora de início
    List<AgendaEventoEntity> findByShowIdOrderByOrdemAscDataHoraInicioAsc(Long showId);

    // Lista todos os eventos de um show sem ordenação específica
    List<AgendaEventoEntity> findByShowId(Long showId);
}
