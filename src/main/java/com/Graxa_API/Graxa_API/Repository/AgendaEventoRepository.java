package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.Evento.AgendaEventoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendaEventoRepository extends JpaRepository<AgendaEventoEntity, Long> {

    List<AgendaEventoEntity> findByShowIdOrderByOrdemAscDataHoraAsc(Long showId);
}
