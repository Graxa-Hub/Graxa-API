package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {



    List<UsuarioEntity> findByNomeContainingIgnoreCase(String nome);

    List<UsuarioEntity> findByAtivoTrueOrderByNomeAsc();

    List<UsuarioEntity> findByCpf(String cpf);

    boolean existsByCpfAllIgnoreCase(String cpf);

    List<UsuarioEntity> findByTipoUsuarioIn(List<String> tipos);
}
