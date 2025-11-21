package com.Graxa_API.Graxa_API.dto.AlocacaoDto;

import com.Graxa_API.Graxa_API.Entity.AlocacaoEntity;
import com.Graxa_API.Graxa_API.Enums.StatusAlocacao;
import com.Graxa_API.Graxa_API.dto.ShowDto.ResponseShowDto;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.ResponseUsuarioDto;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseAlocacaoDto(
        Long id,
        ResponseShowDto show,
        ResponseUsuarioDto colaborador,
        StatusAlocacao status,
        boolean ativo,
        LocalDateTime dataHoraCriacao,
        LocalDateTime dataHoraResposta
) {
    public static ResponseAlocacaoDto toResponse(AlocacaoEntity entity) {
        return new ResponseAlocacaoDto(
                entity.getId(),
                ResponseShowDto.toResponse(entity.getShow()),
                ResponseUsuarioDto.toResponse(entity.getColaborador()),
                entity.getStatus(),
                entity.isAtivo(),
                entity.getDataHoraCriacao(),
                entity.getDataHoraResposta()
        );
    }

    public static List<ResponseAlocacaoDto> toResponse(List<AlocacaoEntity> entities) {
        return entities.stream()
                .map(ResponseAlocacaoDto::toResponse)
                .toList();
    }
}
