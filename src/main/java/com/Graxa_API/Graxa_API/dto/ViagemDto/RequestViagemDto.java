package com.Graxa_API.Graxa_API.dto.ViagemDto;

import com.Graxa_API.Graxa_API.Enums.TipoViagem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RequestViagemDto(
        @NotBlank(message = "O nome da viagem é obrigatório")
        String nomeEvento,

        @NotNull(message = "A data de início é obrigatória")
        LocalDateTime dataInicio,

        @NotNull(message = "A data de fim é obrigatória")
        LocalDateTime dataFim,

        String descricao,

        @NotNull(message = "O ID da turnê é obrigatório")
        Long turneId,

        @NotNull(message = "O tipo da viagem é obrigatório")
        TipoViagem tipoViagem
) {
}
