package com.Graxa_API.Graxa_API.dto.TurneDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para vincular um evento a uma turnê")
public record RequestEventoTurne(

        @Schema(description = "ID da turnê que receberá o evento", example = "1")
        @NotNull
        Long turneId,

        @Schema(description = "ID do evento a ser vinculado à turnê", example = "1")
        @NotNull
        Long eventoId
) {}
