package com.Graxa_API.Graxa_API.dto.TelefoneDto;

import com.Graxa_API.Graxa_API.Enums.TipoTelefone;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para cadastro de telefone do usuário")
public record RequestTelefoneDto(

        @Schema(description = "Tipo de telefone", example = "CELULAR")
        @NotNull(message = "Tipo de telefone obrigatório")
        TipoTelefone tipoTelefone,

        @Schema(description = "Número de telefone", example = "11987654321")
        @NotBlank(message = "Número de telefone obrigatório")
        String numeroTelefone

) {}
