package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ColaboradorRepository extends JpaRepository<ColaboradorEntity, Long> {



    List<ColaboradorEntity> findByNomeContainingIgnoreCase(String nome);

    List<ColaboradorEntity> findByAtivoTrueOrderByNomeAsc();

    List<ColaboradorEntity> findByCpf(String cpf);

    boolean existsByCpfAllIgnoreCase(String cpf);

    List<ColaboradorEntity> findByTipoUsuarioIn(List<String> tipos);
}
