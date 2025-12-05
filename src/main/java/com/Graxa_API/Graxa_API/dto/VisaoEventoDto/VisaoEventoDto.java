package com.Graxa_API.Graxa_API.dto.VisaoEventoDto;

import java.util.List;

public record VisaoEventoDto(
        String artista,
        String turne,
        String dataInfo,
        Coords coords,
        String cidade,
        Integer progresso,
        List<AgendaItem> agenda
) {
    public record Coords(Double lat, Double lon) {}
    public record AgendaItem(String time, String title, String description, Boolean active) {}
}
