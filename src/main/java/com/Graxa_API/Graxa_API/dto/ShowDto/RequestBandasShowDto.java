package com.Graxa_API.Graxa_API.dto.ShowDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RequestBandasShowDto(
        @NotNull(message = "O ID do show é obrigatório")
        Long showId,

        @NotEmpty(message = "A lista de IDs das bandas não pode estar vazia")
        List<Long> bandasIds
) {}
