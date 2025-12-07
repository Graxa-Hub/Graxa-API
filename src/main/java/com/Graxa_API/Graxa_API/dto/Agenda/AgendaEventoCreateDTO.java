package com.Graxa_API.Graxa_API.dto.Agenda;

import com.Graxa_API.Graxa_API.Enums.TipoAgendaItem;

import java.time.LocalDateTime;

public record AgendaEventoCreateDTO(
        Long showId,
        String titulo,
        String descricao,
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        Integer ordem,
        TipoAgendaItem tipo
) {}
