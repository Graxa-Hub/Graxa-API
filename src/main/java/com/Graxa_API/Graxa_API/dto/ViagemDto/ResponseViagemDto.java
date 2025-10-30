package com.Graxa_API.Graxa_API.dto.ViagemDto;

import com.Graxa_API.Graxa_API.Entity.Evento.ViagemEntity;
import com.Graxa_API.Graxa_API.Enums.TipoViagem;
import com.Graxa_API.Graxa_API.dto.TurneDto.ResponseTurneDto;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseViagemDto(
        Long id,
        String nomeEvento,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        String descricao,
        TipoViagem tipoViagem,
        ResponseTurneDto turne
) {
    public static ResponseViagemDto toResponse(ViagemEntity entity) {
        return new ResponseViagemDto(
                entity.getId(),
                entity.getNomeEvento(),
                entity.getDataInicio(),
                entity.getDataFim(),
                entity.getDescricao(),
                entity.getTipoViagem(),
                ResponseTurneDto.toResponse(entity.getTurne())
        );
    }

    public static List<ResponseViagemDto> toResponse(List<ViagemEntity> entities) {
        return entities.stream()
                .map(ResponseViagemDto::toResponse)
                .toList();
    }
}
