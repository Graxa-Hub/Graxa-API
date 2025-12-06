package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.Evento.VooEventoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VooEventoRepository extends JpaRepository<VooEventoEntity, Long> {

    List<VooEventoEntity> findByShowId(Long showId);
}
