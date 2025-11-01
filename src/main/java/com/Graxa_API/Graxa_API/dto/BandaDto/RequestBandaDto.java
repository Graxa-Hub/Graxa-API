package com.Graxa_API.Graxa_API.dto.BandaDto;

import com.Graxa_API.Graxa_API.Enums.Genero;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para criação de uma banda")
public record RequestBandaDto(

        @Schema(description = "Nome da banda", example = "Os Incríveis")
        @NotBlank
        String nome,

        @Schema(description = "Descrição da banda", example = "Banda de rock alternativo formada em São Paulo")
        @NotBlank
        String descricao,

        @Schema(description = "Gênero musical da banda", example = "ROCK")
        @NotBlank
        Genero genero

) {}
