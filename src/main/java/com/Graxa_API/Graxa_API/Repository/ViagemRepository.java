package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.Evento.ViagemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViagemRepository  extends JpaRepository<ViagemEntity, Long> {
    boolean existsByNomeEvento(String nomeEvento);

    List<ViagemEntity> findByAtivoTrue();

    List<ViagemEntity> findByNomeEventoContainingIgnoreCaseAndAtivoTrue(String nomeEvento);
}
