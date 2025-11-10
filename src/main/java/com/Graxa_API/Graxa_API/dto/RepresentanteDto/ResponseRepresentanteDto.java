package com.Graxa_API.Graxa_API.dto.RepresentanteDto;

import com.Graxa_API.Graxa_API.Entity.Usuario.RepresentanteEntity;
import com.Graxa_API.Graxa_API.dto.BandaDto.ResponseBandaDto;

import java.util.List;

public record ResponseRepresentanteDto(
        Long id,
        String nome,
        String email,

        List<ResponseBandaSimplificadaDto> bandas // Lista simplificada para evitar recursão infinita
) {
    public static ResponseRepresentanteDto toResponse(RepresentanteEntity entity) {
        // Converte bandas para DTO simplificado (sem integrantes/representante)
        List<ResponseBandaSimplificadaDto> bandasDto = entity.getBandas() != null
                ? entity.getBandas().stream()
                .map(ResponseBandaSimplificadaDto::toResponse)
                .toList()
                : List.of();

        return new ResponseRepresentanteDto(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                bandasDto
        );
    }

    public static List<ResponseRepresentanteDto> toResponse(List<RepresentanteEntity> representantes) {
        return representantes.stream()
                .map(ResponseRepresentanteDto::toResponse)
                .toList();
    }

    // DTO interno simplificado para evitar recursão infinita
    public record ResponseBandaSimplificadaDto(
            Long id,
            String nome,
            String descricao,
            String nomeFoto
    ) {
        public static ResponseBandaSimplificadaDto toResponse(com.Graxa_API.Graxa_API.Entity.BandaEntity entity) {
            return new ResponseBandaSimplificadaDto(
                    entity.getId(),
                    entity.getNome(),
                    entity.getDescricao(),
                    entity.getNomeFoto()
            );
        }
    }
}