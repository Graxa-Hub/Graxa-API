package com.Graxa_API.Graxa_API.dto.BandaDto;

import com.Graxa_API.Graxa_API.Entity.BandaEntity;
import com.Graxa_API.Graxa_API.Entity.UsuarioEntity;
import com.Graxa_API.Graxa_API.Enums.Genero;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.ResponseUsuarioDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ResponseBandaDto (
        @NotBlank Long id,
        @NotBlank String nome,
        @NotBlank String descricao,
        @NotBlank Genero genero,
        @NotEmpty List<ResponseUsuarioDto> integrantes
){
    public static ResponseBandaDto toResponse(BandaEntity entity){
        List<ResponseUsuarioDto> integrantesDto = entity.getIntegrantes()
                .stream()
                .map(ResponseUsuarioDto::toResponse)
                .toList();
        return new ResponseBandaDto(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getGenero(),
                integrantesDto
        );
    }

    public static List<ResponseBandaDto> toResponse(List<BandaEntity> bandas){
        return bandas.stream().map(ResponseBandaDto::toResponse).toList();
    }
}
