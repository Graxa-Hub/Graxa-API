package com.Graxa_API.Graxa_API.dto.Logistica;

public record VooDTO(
        Long id,
        Long colaboradorId,
        String ciaAerea,
        String codigoVoo,
        String origem,
        String destino,
        String partida,
        String chegada
) {}