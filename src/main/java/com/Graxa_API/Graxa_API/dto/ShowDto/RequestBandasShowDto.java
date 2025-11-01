package com.Graxa_API.Graxa_API.dto.ShowDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "DTO para adicionar bandas a um show existente")
public record RequestBandasShowDto(

        @NotNull(message = "O ID do show é obrigatório")
        @Schema(description = "ID do show ao qual as bandas serão adicionadas", example = "10")
        Long showId,

        @NotEmpty(message = "A lista de IDs das bandas não pode estar vazia")
        @Schema(description = "Lista de IDs das bandas que serão adicionadas ao show", example = "[3, 7, 12]")
        List<Long> bandasIds
) {}
