package com.Graxa_API.Graxa_API.dto.ShowDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RequestShowDto(
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
    Long localId,

    @NotNull
    Long responsavelId
) {
}
