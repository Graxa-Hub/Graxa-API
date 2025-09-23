package com.Graxa_API.Graxa_API.dto.ProdutorMusical;
import com.Graxa_API.Graxa_API.Entity.ProdutorMusicalEntity;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.List;

public record ResponseProdutorMusicalDto(
     Long id,
     String nome,
     LocalDate dataNascimento,
     String email,
     String cpf,
     Boolean ativo
) {
    public static ResponseProdutorMusicalDto toResponse(ProdutorMusicalEntity produtorMusicalEntity) {

        return new ResponseProdutorMusicalDto(produtorMusicalEntity.getId(),produtorMusicalEntity.getNome(), produtorMusicalEntity.getDataNascimento(), produtorMusicalEntity.getEmail(), produtorMusicalEntity.getCpf(), produtorMusicalEntity.getAtivo());
    }

    public static List<ResponseProdutorMusicalDto> toResponse(List<ProdutorMusicalEntity> produtores) {
        return produtores.stream()
                .map(ResponseProdutorMusicalDto::toResponse)
                .toList();
    }
}
