package com.Graxa_API.Graxa_API.dto.ViagemDto;

import com.Graxa_API.Graxa_API.Enums.TipoViagem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "DTO para criação ou atualização de uma viagem")
public record RequestViagemDto(

        @NotBlank(message = "O nome da viagem é obrigatório")
        @Schema(description = "Nome da viagem ou evento", example = "Turnê Nordeste 2025")
        String nomeEvento,

        @NotNull(message = "A data de início é obrigatória")
        @Schema(description = "Data e hora de início da viagem", example = "2025-11-05T08:00:00")
        LocalDateTime dataInicio,

        @NotNull(message = "A data de fim é obrigatória")
        @Schema(description = "Data e hora de término da viagem", example = "2025-11-10T20:00:00")
        LocalDateTime dataFim,

        @Schema(description = "Descrição opcional da viagem", example = "Deslocamento para shows em cidades do interior")
        String descricao,

        @NotNull(message = "O ID da turnê é obrigatório")
        @Schema(description = "ID da turnê vinculada à viagem", example = "1")
        Long turneId,

        @NotNull(message = "O tipo da viagem é obrigatório")
        @Schema(description = "Tipo da viagem (ex: aereo, terrestre)", example = "aereo")
        TipoViagem tipoViagem
) {}
