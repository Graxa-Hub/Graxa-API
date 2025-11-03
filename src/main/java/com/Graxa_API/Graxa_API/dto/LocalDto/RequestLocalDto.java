package com.Graxa_API.Graxa_API.dto.LocalDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para criação ou atualização de um local")
public record RequestLocalDto(

        @NotBlank(message = "O nome do local é obrigatório")
        @Schema(description = "Nome do local", example = "Arena Graxa")
        String nome,

        @NotNull(message = "O ID do endereço é obrigatório")
        @Schema(description = "ID do endereço vinculado ao local", example = "1")
        Long idEndereco,

        @NotNull(message = "A capacidade é obrigatória")
        @Min(value = 1, message = "A capacidade deve ser maior que zero")
        @Schema(description = "Capacidade máxima de pessoas no local", example = "1500")
        Integer capacidade
) {}
