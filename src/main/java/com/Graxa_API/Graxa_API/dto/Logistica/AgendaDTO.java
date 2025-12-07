package com.Graxa_API.Graxa_API.dto.Logistica;

public record AgendaDTO(
        Long id,
        String titulo,
        String descricao,
        String dataHoraInicio,
        String dataHoraFim,
        Integer ordem,
        String tipo
) {}
