package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.AlocacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlocacaoRepository extends JpaRepository<AlocacaoEntity, Long> {

    List<AlocacaoEntity> findByShowId(Long showId);

    List<AlocacaoEntity> findByColaboradorId(Long colaboradorId);

    List<AlocacaoEntity> findByStatus(String status);
    List<AlocacaoEntity> findByShowIdAndColaboradorId(Long showId, Long colaboradorId);
    boolean existsByShowIdAndColaboradorId(Long showId, Long colaboradorId);
}
