package com.Graxa_API.Graxa_API.dto.TurneDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RequestTurneDto(
        @NotBlank
        String nomeTurne,

        @NotNull
        LocalDateTime dataHoraInicioTurne,

        @NotNull
        LocalDateTime dataHoraFimTurne

) {
}
