package com.Graxa_API.Graxa_API.dto.TurneDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "DTO para criação de uma turnê")
public record RequestTurneDto(

        @Schema(description = "Nome da turnê", example = "Turnê Brasil 2025")
        @NotBlank
        String nomeTurne,

        @Schema(description = "Data e hora de início da turnê", example = "2025-11-10T20:00:00")
        @NotNull
        LocalDateTime dataHoraInicioTurne,

        @Schema(description = "Data e hora de encerramento da turnê", example = "2025-12-20T23:00:00")
        @NotNull
        LocalDateTime dataHoraFimTurne,

        @Schema(description = "Descrição da turnê", example = "Turnê especial com shows em várias cidades do Brasil")
        String descricao
) {}

