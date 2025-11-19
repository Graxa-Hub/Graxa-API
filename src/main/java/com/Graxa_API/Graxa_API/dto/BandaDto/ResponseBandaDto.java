package com.Graxa_API.Graxa_API.dto.BandaDto;

import com.Graxa_API.Graxa_API.Entity.BandaEntity;
import com.Graxa_API.Graxa_API.Enums.Genero;
import com.Graxa_API.Graxa_API.dto.ArtistaDto.ResponseArtistaDto;
import com.Graxa_API.Graxa_API.dto.RepresentanteDto.ResponseRepresentanteDto;

import java.util.List;
import java.util.Optional;

public record ResponseBandaDto(
        Long id,
        String nome,
        String descricao,
        Genero genero,
        String nomeFoto, // ✅ NOVO: Nome do arquivo da foto
        ResponseRepresentanteDto representante, // ✅ NOVO: Dados do representante
        List<ResponseArtistaDto> integrantes,
        Boolean ativo
) {
    public static ResponseBandaDto toResponse(BandaEntity entity) {
        List<ResponseArtistaDto> integrantesDto = entity.getIntegrantes()
                .stream()
                .map(ResponseArtistaDto::toResponse)
                .toList();

        // Converte representante para DTO (se existir)
        ResponseRepresentanteDto representanteDto = entity.getRepresentante() != null
                ? ResponseRepresentanteDto.toResponse(entity.getRepresentante())
                : null;

        return new ResponseBandaDto(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getGenero(),
                entity.getNomeFoto(), // ✅ NOVO
                representanteDto,     // ✅ NOVO
                integrantesDto,
                entity.getAtivo()
        );
    }

    public static List<ResponseBandaDto> toResponse(List<BandaEntity> bandas) {
        return bandas.stream()
                .map(ResponseBandaDto::toResponse)
                .toList();
    }


}