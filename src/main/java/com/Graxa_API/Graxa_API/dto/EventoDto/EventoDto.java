package com.Graxa_API.Graxa_API.dto.EventoDto;

import com.Graxa_API.Graxa_API.Entity.Evento.EventoEntity;

import java.time.LocalDateTime;

public record EventoDto(
    Long id,
    String nomeEvento,
    LocalDateTime dataInicio,
    LocalDateTime dataFim,
    String tipoEvento

) {
    public static EventoDto toResumo(EventoEntity evento) {
        String tipoEvento = evento.getClass().getSimpleName().replace("Entity", "").toUpperCase();

        return new EventoDto(
                evento.getId(),
                evento.getNomeEvento(),
                evento.getDataInicio(),
                evento.getDataFim(),
                tipoEvento
        );
    }

}
