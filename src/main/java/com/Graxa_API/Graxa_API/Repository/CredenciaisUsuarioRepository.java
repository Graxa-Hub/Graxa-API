package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.CredenciaisUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredenciaisUsuarioRepository extends JpaRepository<CredenciaisUsuarioEntity, Long> {

    boolean existsByEmailIgnoreCase(String email);
    Optional<CredenciaisUsuarioEntity> findByEmail(String email);
    Optional<CredenciaisUsuarioEntity> findByNomeUsuario(String nomeUsuario);
    Optional<CredenciaisUsuarioEntity> findByNomeUsuarioOrEmail(String nome, String email);


}
