package com.Graxa_API.Graxa_API.dto.BandaDto;

import com.Graxa_API.Graxa_API.Entity.BandaEntity;
import com.Graxa_API.Graxa_API.Enums.Genero;

import java.util.List;

public record ResponseBandaSimplificadaDto(
        Long id,
        String nome,
        String descricao,
        Genero genero,
        String nomeFoto,
        Boolean ativo
) {
    public static ResponseBandaSimplificadaDto toResponse(BandaEntity entity) {
        return new ResponseBandaSimplificadaDto(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getGenero(),
                entity.getNomeFoto(),
                entity.getAtivo()
        );
    }

    public static List<ResponseBandaSimplificadaDto> toResponse(List<BandaEntity> bandas) {
        return bandas.stream()
                .map(ResponseBandaSimplificadaDto::toResponse)
                .toList();
    }
}