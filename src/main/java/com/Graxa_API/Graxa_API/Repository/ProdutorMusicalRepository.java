package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.ProdutorMusicalEntity;

import org.springframework.data.jpa.repository.JpaRepository;  

import java.util.List;

public interface ProdutorMusicalRepository extends JpaRepository<ProdutorMusicalEntity, Long> {
    ProdutorMusicalEntity findByEmail(String email);
    List<ProdutorMusicalEntity> findByNomeContainingIgnoreCase(String nome);
    List<ProdutorMusicalEntity> findByAtivoTrueOrderByNomeAsc();
    List<ProdutorMusicalEntity> findByCpf(String cpf);

}
