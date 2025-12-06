package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.Evento.HotelEventoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelEventoRepository extends JpaRepository<HotelEventoEntity, Long> {

    List<HotelEventoEntity> findByShowId(Long showId);
}
