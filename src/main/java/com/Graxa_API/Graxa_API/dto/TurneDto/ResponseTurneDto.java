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
        List<EventoDto> eventos
) {
    public static ResponseTurneDto toResponse(TurneEntity turne) {
        List<EventoDto> eventos = turne.getEventos() != null
                ? turne.getEventos().stream()
                .map(EventoDto::toResumo)
                .toList()
                : List.of();

        return new ResponseTurneDto(
                turne.getId(),
                turne.getNomeTurne(),
                turne.getDataHoraInicioTurne(),
                turne.getDataHoraFimTurne(),
                eventos
        );
    }

    public static List<ResponseTurneDto> toResponse(List<TurneEntity> turnes) {
        return turnes.stream()
                .map(ResponseTurneDto::toResponse)
                .toList();
    }
}
