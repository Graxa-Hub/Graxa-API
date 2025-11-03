package com.Graxa_API.Graxa_API.dto.BandaDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(description = "DTO para adicionar integrantes a uma banda")
public record RequestIntegrantesDto(

        @NotEmpty
        @Schema(description = "Lista de IDs dos usuários que serão adicionados como integrantes", example = "[1]")
        List<Long> integrantesIds
) {}
