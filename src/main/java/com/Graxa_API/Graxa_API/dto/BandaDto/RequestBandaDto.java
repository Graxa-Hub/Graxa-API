package com.Graxa_API.Graxa_API.dto.BandaDto;

import com.Graxa_API.Graxa_API.Enums.Genero;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para criação de uma banda")
public record RequestBandaDto(

        @Schema(description = "Nome da banda", example = "Os Incríveis")
        @NotBlank(message = "Nome da banda é obrigatório")
        String nome,

        @Schema(description = "Descrição da banda", example = "Banda de rock alternativo formada em São Paulo")
        @NotBlank(message = "Descrição da banda é obrigatória")
        String descricao,

        @Schema(description = "Gênero musical da banda", example = "ROCK")
        @NotNull(message = "Gênero musical é obrigatório")
        Genero genero,

        @Schema(description = "ID do representante da banda", example = "5")
        @NotNull(message = "Representante é obrigatório")
        Long representanteId

) {}
