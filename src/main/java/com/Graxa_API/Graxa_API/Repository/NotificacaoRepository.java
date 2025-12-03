package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.NotificacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<NotificacaoEntity, Long> {
    List<NotificacaoEntity> findByColaboradorId(Long colaboradorId);
    List<NotificacaoEntity> findByColaboradorIdAndLidaFalse(Long colaboradorId);
    List<NotificacaoEntity> findByColaboradorIdOrderByDataCriacaoDesc(Long colaboradorId);
    List<NotificacaoEntity> findByColaboradorIdAndLidaFalseOrderByDataCriacaoDesc(Long colaboradorId);
    long countByColaboradorIdAndLidaFalse(Long colaboradorId);
}
