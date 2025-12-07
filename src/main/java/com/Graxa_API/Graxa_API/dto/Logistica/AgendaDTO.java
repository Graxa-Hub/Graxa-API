package com.Graxa_API.Graxa_API.dto.Logistica;

public record AgendaDTO(
        Long id,
        Long colaboradorId,
        String titulo,
        String descricao,
        String dataHora,
        Integer duracaoMinutos,
        Integer ordem
) {}