package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.Evento.TransporteEventoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransporteEventoRepository extends JpaRepository<TransporteEventoEntity, Long> {

    List<TransporteEventoEntity> findByShowId(Long showId);
}
