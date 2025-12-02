package com.Graxa_API.Graxa_API.dto.ShowDto;

import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import com.Graxa_API.Graxa_API.dto.LocalDto.ResponseLocalDto;

import java.time.LocalDateTime;
import java.util.List;


public record ResponseResumoShowDto(
        Long id,
        String nomeEvento,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        String descricao,
        ResponseLocalDto local
) {
    public static ResponseResumoShowDto toResponse(ShowEntity entity) {
        if (entity == null) return null;

        return new ResponseResumoShowDto(
                entity.getId(),
                entity.getNomeEvento(),
                entity.getDataInicio(),
                entity.getDataFim(),
                entity.getDescricao(),
                ResponseLocalDto.toResponse(entity.getLocal())
        );
    }

    public static List<ResponseResumoShowDto> toResponse(List<ShowEntity> entities) {
        return entities.stream()
                .map(ResponseResumoShowDto::toResponse)
                .toList();
    }
}