package com.Graxa_API.Graxa_API.dto.Logistica;

import java.util.List;

public record LogisticaEventoResponseDTO(
        List<HotelDTO> hotels,
        List<VooDTO> voos,
        List<TransporteDTO> transportes,
        List<AgendaDTO> agenda
) {}
