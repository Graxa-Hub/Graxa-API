package com.Graxa_API.Graxa_API.dto.Logistica;

public record TransporteDTO(
        Long id,
        Long colaboradorId,
        String tipo,
        String saida,
        String destino,
        String motorista,
        String observacao
) {}