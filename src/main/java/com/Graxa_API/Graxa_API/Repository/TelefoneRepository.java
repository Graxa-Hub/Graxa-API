package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.TelefoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TelefoneRepository extends JpaRepository<TelefoneEntity, Long> {
    List<TelefoneEntity> findByUsuarioId(Long usuarioId);
}
