package com.Graxa_API.Graxa_API.dto.LocalDto;

import com.Graxa_API.Graxa_API.Entity.LocalEntity;
import com.Graxa_API.Graxa_API.dto.EnderecoDto.ResponseEnderecoDto;

import java.util.List;

public record ResponseLocalDto(
        Long id,
        String nome,
        ResponseEnderecoDto endereco,
        Integer capacidade
) {
    public static ResponseLocalDto toResponse(LocalEntity entity) {
        return new ResponseLocalDto(
                entity.getId(),
                entity.getNome(),
                ResponseEnderecoDto.toResponse(entity.getEndereco()),
                entity.getCapacidade()
        );
    }

    public static List<ResponseLocalDto> toResponse(List<LocalEntity> entities) {
        return entities.stream()
                .map(ResponseLocalDto::toResponse)
                .toList();
    }
}
