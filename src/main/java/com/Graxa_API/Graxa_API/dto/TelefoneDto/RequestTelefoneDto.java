package com.Graxa_API.Graxa_API.dto.TelefoneDto;

import com.Graxa_API.Graxa_API.Enums.TipoTelefone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RequestTelefoneDto(
        @NotNull(message = "Tipo de telefone obrigatório")
        TipoTelefone tipoTelefone,

        @NotBlank(message = "Número de telefone obrigatório")
        String numeroTelefone
) {}
