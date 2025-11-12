package com.Graxa_API.Graxa_API.dto.TurneDto;

import com.Graxa_API.Graxa_API.Entity.TurneEntity;
import com.Graxa_API.Graxa_API.dto.EventoDto.EventoDto;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseTurneDto(
        Long id,
        String nomeTurne,
        LocalDateTime dataHoraInicioTurne,
        LocalDateTime dataHoraFimTurne,
        String nomeImagem,
        String descricao,
        Long bandaId,
        String bandaNome
) {
    public static ResponseTurneDto toResponse(TurneEntity entity) {
        return new ResponseTurneDto(
                entity.getId(),
                entity.getNomeTurne(),
                entity.getDataHoraInicioTurne(),
                entity.getDataHoraFimTurne(),
                entity.getNomeImagem(),
                entity.getDescricao(),
                entity.getBanda() != null ? entity.getBanda().getId() : null,
                entity.getBanda() != null ? entity.getBanda().getNome() : null
        );
    }

    public static List<ResponseTurneDto> toResponse(List<TurneEntity> turnes) {
        return turnes.stream()
                .map(ResponseTurneDto::toResponse)
                .toList();
    }
}
