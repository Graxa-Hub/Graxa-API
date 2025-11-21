package com.Graxa_API.Graxa_API.dto.AlocacaoDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para criação ou atualização de uma alocação de colaborador em um show")
public record RequestAlocacaoDto(

        @NotNull
        @Schema(description = "ID do show em que o colaborador será alocado", example = "1")
        Long showId,

        @NotNull
        @Schema(description = "ID do colaborador que será alocado", example = "5")
        Long colaboradorId
) {}
