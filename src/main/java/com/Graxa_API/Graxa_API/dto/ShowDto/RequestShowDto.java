package com.Graxa_API.Graxa_API.dto.ShowDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "DTO para criação ou atualização de um show")
public record RequestShowDto(

        @NotBlank
        @Schema(description = "Nome do evento", example = "Festival Graxa 2025")
        String nomeEvento,

        @NotNull
        @Schema(description = "Data e hora de início do show", example = "2025-11-01T18:00:00")
        LocalDateTime dataInicio,

        @NotNull
        @Schema(description = "Data e hora de término do show", example = "2025-11-01T22:00:00")
        LocalDateTime dataFim,

        @Schema(description = "Descrição opcional do evento", example = "Show especial com bandas independentes")
        String descricao,

        @NotNull
        @Schema(description = "ID da turnê associada ao show", example = "3")
        Long turneId,

        @NotNull
        @Schema(description = "ID do local onde o show será realizado", example = "7")
        Long localId,

        @NotNull
        @Schema(description = "ID do colaborador responsável pelo evento", example = "12")
        Long responsavelId
) {}
