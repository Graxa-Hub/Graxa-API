package com.Graxa_API.Graxa_API.dto.ViagemDto;

import com.Graxa_API.Graxa_API.Enums.TipoViagem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RequestViagemDto(
        @NotBlank
        String nomeEvento,

        @NotNull
        LocalDateTime dataInicio,

        @NotNull
        LocalDateTime dataFim,

        String descricao,

        @NotNull
        Long turneId,

        @NotNull
        TipoViagem tipoViagem
) {
}
