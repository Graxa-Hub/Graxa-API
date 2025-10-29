package com.Graxa_API.Graxa_API.dto.TurneDto;

import jakarta.validation.constraints.NotNull;

public record RequestEventoTurne(
        @NotNull
        Long turneId,

        @NotNull
        Long eventoId
) {
}
